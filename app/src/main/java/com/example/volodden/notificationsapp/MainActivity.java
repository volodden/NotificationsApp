package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SEND_REQUEST = 42;

    private ArrayList<CreateNotification.NotificationsData> notifications;
    private ArrayAdapter<CreateNotification.NotificationsData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateNotification.NotificationsData notification = createNewNotification(null);

                //TODO test
                CreateNotification.NotificationsData notification = new CreateNotification.NotificationsData("КОТИКИ",
                        CreateNotification.NotificationsType.PushNotification,
                        null, "ololo", null);

                if (notification != null) {
                    notifications.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Cache.createInstance();
        if( Cache.getInstance().loadDataFromStorage(getApplicationContext()) ) {
            Log.i("CRT_C", "Cache exist");
            notifications = Cache.getInstance().getAllData();
            Log.i("CRT_C", "Yes, load from memory");
        } else {
            Log.i("CRT_C", "Cache no exist");
            notifications = new ArrayList<CreateNotification.NotificationsData>();
            notifications.add(new CreateNotification.NotificationsData("Рыжик",
                CreateNotification.NotificationsType.PushNotification,
                null, "ololo", null));
            notifications.add(new CreateNotification.NotificationsData("Барсик",
                CreateNotification.NotificationsType.PushNotification,
                null, "ololo", null));
            notifications.add(new CreateNotification.NotificationsData("Мурзик",
                CreateNotification.NotificationsType.PushNotification,
                null, "ololo", null));
        }

        final ListView notificationList = (ListView) findViewById(R.id.notification_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notifications);

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                TextView tw = (TextView) v;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You click on " + String.valueOf(tw.getText().toString() + " " + String.valueOf(position)), Toast.LENGTH_SHORT);
                toast.show();

                CreateNotification.NotificationsData notification = createNewNotification(notifications.get(position));
                if( notification != null ) {
                    notifications.add(notification);
                    notifications.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // устанавливаем для списка адаптер
        notificationList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        Log.i("DST_C", "Destroy");
        Cache.getInstance().setAllData(notifications);
        if( Cache.getInstance().saveDataInStorage(this) ) {
            Log.i("DST_C", "Cashe saved");
        } else {
            Log.i("DST_C", "Savecache error...");
        }
        super.onDestroy();
    }

    protected CreateNotification.NotificationsData createNewNotification(CreateNotification.NotificationsData data) {

        Intent intent = new Intent(this, CreateNotification.class);
        if (data != null) {
            CreateNotification.NotificationsType type = data.type;
            String text = data.text;
            Date datetime = data.datetime;
            String number = data.phoneNumber;
            String name = data.name;
            intent.putExtra(CreateNotification.type_text, type);
            intent.putExtra(CreateNotification.text_text, text);
            intent.putExtra(CreateNotification.date_text, datetime);
            intent.putExtra(CreateNotification.phone_text, number);
            intent.putExtra(CreateNotification.name_text, name);
        }

        startActivityForResult(intent, SEND_REQUEST);

        return nd;
    }

    private CreateNotification.NotificationsData nd;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( data == null || requestCode != SEND_REQUEST || resultCode != RESULT_OK) {
            nd = null;
            return;
        }
        Bundle extras = getIntent().getExtras();
        String name = (String) extras.get(CreateNotification.name_text);
        CreateNotification.NotificationsType type = (CreateNotification.NotificationsType) extras.get(CreateNotification.type_text);
        String text = (String) extras.get(CreateNotification.text_text);
        Date datetime = (Date) extras.get(CreateNotification.date_text);
        String number = (String) extras.get(CreateNotification.phone_text);
        nd = new CreateNotification.NotificationsData(name, type, datetime, text, number);
    }
}
