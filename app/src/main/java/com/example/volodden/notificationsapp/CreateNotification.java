package com.example.volodden.notificationsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.util.HashMap;

public class CreateNotification extends AppCompatActivity {

    public CreateNotification() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
    }

    public static class NotificationsData {

        public NotificationsType type;
        public Date datetime;
        public String text;
        public String phoneNumber;

        NotificationsData(NotificationsType type, Date datetime, String text, String phoneNumber) {
            this.phoneNumber = phoneNumber;
            this.datetime = datetime;
            this.text = text;
            this.phoneNumber = phoneNumber;

            //давать id при создании класса!
        }
    }

    public enum NotificationsType {
        PushNotification,
        SMSNotification
    }

    private final static String pushText = "push";
    private final static String smsText = "sms";

    public static String typeToStr(NotificationsType type) {
        switch( type ) {
            case PushNotification: return pushText;
            case SMSNotification: return smsText;
        }
        //exception!
        return "";
    }

    public static NotificationsType strToType(String type) {
        if( type.equals(pushText) ) {
            return NotificationsType.PushNotification;
        }
        if( type.equals(smsText) ) {
            return NotificationsType.SMSNotification;
        }
        //exception!
        return NotificationsType.PushNotification;
    }
}
