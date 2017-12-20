package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private ArrayList<Pair<String, CreateNotification.NotificationsData>> notifications;
    private ArrayAdapter<Pair<String, CreateNotification.NotificationsData>> adapter;

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
                Pair<String, CreateNotification.NotificationsData> notification = createNewNotification(null);
                if (notification != null) {
                    notifications.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //ToDo
        //После считывания добавить в массив.
        //Cache.createInstance();
        //notifications = Cache.instance().loadDataFromStorage(getApplicationContext());
        //if( notifications == null ) {
            notifications = new ArrayList<Pair<String, CreateNotification.NotificationsData>>();
            //notifications.add("Рыжик");
            //notifications.add("Барсик");
            //notifications.add("Мурзик");
        //}

        ListView notificationList = (ListView) findViewById(R.id.notification_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notifications);

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                TextView tw = (TextView) v;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You click on " + String.valueOf(tw.getText().toString() + " " + String.valueOf(position)), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // устанавливаем для списка адаптер
        notificationList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        Cache.instance().saveDataInStorage(this);
        super.onDestroy();
    }

    //На каждом уведомлении будет кнопка "просмотра", которая открывает CreateNotificationActivity.

    protected Pair<String, CreateNotification.NotificationsData> createNewNotification(CreateNotification.NotificationsData data) {

        Intent intent = new Intent(this, CreateNotification.class);
        if (data != null) {
            CreateNotification.NotificationsType type = data.getType();
            String text = data.getText();
            Date datetime = data.getDatetime();
            String number = data.getPhoneNumber();
            intent.putExtra("type", type);
            intent.putExtra("text", text);
            intent.putExtra("datetime", datetime);
            intent.putExtra("number", number);
        }
        startActivityForResult(intent, SEND_REQUEST);

        //if( получить данные обратно (были ли какие-то изменения) ) {
        //  if( data != null ) {
        //      удалить старое уведомление
        //  }
        //  notifications.add(...);
        //  updateDisplay();
        //}

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( data == null || requestCode != SEND_REQUEST) {
            return;
        }

    }
}
