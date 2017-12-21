package com.example.volodden.notificationsapp;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Cache {

    private ArrayList<CreateNotification.NotificationsData> cache; //А нужен ли нам LruCache, когда хватит просто Map?

    private final int CACHE_SIZE = 1000;
    private final String FILE_NAME = "cache.txt";

    private static Cache instance;

    public static void createInstance() {
        if( instance == null ) {
            instance = new Cache();
        }
    }

    static public Cache getInstance() {
        return instance;
    }

    Cache() {
        init();
    }

    private void init() {
        cache = new ArrayList<CreateNotification.NotificationsData>(CACHE_SIZE);
    }

    public CreateNotification.NotificationsData getData(int position) {
        return cache.get(position);
    }

    public ArrayList<CreateNotification.NotificationsData> getAllData() {
        return cache;
    }

    public void setData(CreateNotification.NotificationsData value) {
        cache.add(value);
    }

    public void setAllData(ArrayList<CreateNotification.NotificationsData> data) {
        cache = data;
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
                String nameString = data.getString(CreateNotification.name_text);
                String typeString = data.getString(CreateNotification.type_text);
                String textString = data.getString(CreateNotification.text_text);
                String phoneString = data.getString(CreateNotification.phone_text);
                String dateString = data.getString(CreateNotification.date_text);

                Date date;
                if( !dateString.equals("null") ) { //TODO убрать
                    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    date = format.parse(dateString);
                } else {
                    date = null;
                }

                if( phoneString.equals("null") ) {
                    phoneString = null;
                }
                CreateNotification.NotificationsType type = CreateNotification.strToType(typeString);

                CreateNotification.NotificationsData description =
                        new CreateNotification.NotificationsData(nameString, type, date, textString, phoneString);

                cache.add(description);
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
    public boolean saveDataInStorage(Context context) throws RuntimeException {

        JSONArray json = new JSONArray();
        for( int i = 0; i < cache.size(); ++i ) {
            try {
                JSONObject obj = new JSONObject();
                obj.put(CreateNotification.name_text, cache.get(i).name);
                obj.put(CreateNotification.type_text, CreateNotification.typeToStr(cache.get(i).type));
                obj.put(CreateNotification.text_text, cache.get(i).text);


                //TODO test
                obj.put(CreateNotification.date_text, "null");//cache.get(i).datetime.toString());


                if( cache.get(i).phoneNumber != null ) {
                    obj.put(CreateNotification.phone_text, cache.get(i).phoneNumber);
                } else {
                    obj.put(CreateNotification.phone_text, "null");
                }
                json.put(obj);
            }
            catch (JSONException e) {
                //Log.i("CACHE_EXCPT", "JSON Exception " + e.toString());
                e.printStackTrace();
                return false;
            }
        }

        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            if( !file.exists() ) {
                if( !file.createNewFile() ) {
                    return false;
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

        Log.i("CACHE", json.toString());
        return  true;
    }
}
