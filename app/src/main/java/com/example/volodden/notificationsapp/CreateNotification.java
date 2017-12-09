package com.example.volodden.notificationsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class CreateNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
    }

    public class NotificationsData {

        public NotificationsType type;
        public Date datetime;
        public String text;
        public String phoneNumber;

        NotificationsData(NotificationsType type, Date datetime, String text, String phoneNumber) {
            this.phoneNumber = phoneNumber;
            this.datetime = datetime;
            this.text = text;
            this.phoneNumber = phoneNumber;
        }
    }

    public enum NotificationsType {
        PushNotification,
        SMSNotification
    }
}
