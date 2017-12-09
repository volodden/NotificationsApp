package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public HashMap<Date, CreateNotification.NotificationsData> notifications;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                createNewNotification(null);
            }
        });

        //ToDo
        //считывание из памяти списка уведомлений
    }

    @Override
    protected void onDestroy() {
        //ToDo
        //Запись данных в память
        super.onDestroy();
    }

    //На каждом уведомлении будет кнопка "просмотра", которая открывает CreateNotificationActivity.

    protected void createNewNotification(CreateNotification.NotificationsData data) {

        if (data != null) {
            //передать данные для создания Activity
        }
        Intent intent = new Intent(this, CreateNotification.class);
        startActivity(intent);

        //if( получить данные обратно (были ли какие-то изменения) ) {
        //  if( data != null ) {
        //      удалить старое уведомление
        //  }
        //  notifications.add(...);
        //  updateDisplay();
        //}
    }
}
