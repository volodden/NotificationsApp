package com.example.volodden.notificationsapp;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Cache {

    private Map<String, CreateNotification.NotificationsData> cache; //А нужен ли нам LruCache, когда хватит просто Map?

    private final int CACHE_SIZE = 1000;
    private final String FILE_NAME = "kdscache.txt";

    private static Cache _instance;

    public static void createInstance() {
        if( null == _instance ) {
            _instance = new Cache();
            _instance.init();
        }
    }

    static public Cache instance() {
        return _instance;
    }

    Cache() {
        init();
    }

    private void init() {
        cache = new HashMap<String, CreateNotification.NotificationsData>(CACHE_SIZE);
    }

    public CreateNotification.NotificationsData getData(String key) {
        return cache.get(key); // null if (key, value) doesn't exist
    }

    public void setData(String key, CreateNotification.NotificationsData value) {
        cache.put(key, value);
    }

    // В onCreate MainActivity:
    // Cache.createInstance();
    // Cache.instance().loadDataFromStorage(getApplicationContext());
    public boolean loadDataFromStorage(Context context) {

        StringBuilder sb = new StringBuilder();

        File file = new File(context.getFilesDir(), FILE_NAME);
        if( !file.exists() ) {
            return false;
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while( (s = in.readLine()) != null ) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
            return false;
        }

        JSONArray json;
        try {
            json = new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
            return false;
        }

        init();

        for( int i = 0; i < json.length(); ++i ) {
            try {
                JSONObject data = json.getJSONObject(i);
                String key = data.getString(Helper.key);
                String text = data.getString(Helper.text);
                String phone = data.getString(Helper.phone);

                String dateString = data.getString(Helper.time);
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                Date date = format.parse(dateString);

                Date time = new Date();
                CreateNotification.NotificationsType type = CreateNotification.strToType(data.getString(Helper.text));



                //TODO
                //name


                CreateNotification.NotificationsData description =
                        new CreateNotification.NotificationsData("NAME", type, time, text, phone);

                //Создать новое уведомление.
            } catch (JSONException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
                return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //Вызывать в onDestroy MainActivity
    public void saveDataInStorage(Context context) throws RuntimeException {

        JSONArray json = new JSONArray();
        for( String key : cache.keySet() ) {
            try {
                JSONObject obj = new JSONObject();
                obj.put(Helper.key, key);
                CreateNotification.NotificationsData data = cache.get(key);
                obj.put(Helper.time, data.datetime);
                obj.put(Helper.phone, data.phoneNumber);
                obj.put(Helper.text, data.text);
                obj.put(Helper.type, data.type);
                json.put(obj);
            }
            catch (JSONException e) {
                //Log.i("CACHE_EXCPT", "JSON Exception " + e.toString());
                e.printStackTrace();
                return;
            }
        }

        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            if( !file.exists() ) {
                if( !file.createNewFile() ) {
                    return;
                    //throw new RuntimeException("File isn't created");
                }
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                out.print(json.toString());
            }
            finally {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}

class Helper {
    final static String key = "key";
    final static String time = "time";
    final static String phone = "phone";
    final static String text = "description";
    final static String type = "type";
}
