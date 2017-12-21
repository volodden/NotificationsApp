package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class CreateNotification extends AppCompatActivity {

    NotificationsData data;

    public CreateNotification() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        //получение всех объектов активити

        Bundle extras = getIntent().getExtras();
        if( extras != null ) {
            String name = (String) extras.get(name_text);
            NotificationsType type = (NotificationsType) extras.get(type_text);
            String text = (String) extras.get(text_text);
            Date datetime = (Date) extras.get(date_text);
            String number = (String) extras.get(phone_text);
            NotificationsData nd = new NotificationsData(name, type, datetime, text, number);

            //ToDo
            //Вставить значения
        }

        //сделать лисенеры на все элементы
        //обработка кнопки "назад"
        //обработка кнопки "сохранить"
    }

    protected void onSave() {
        Intent intent = new Intent();
        //intent.putExtra("type", type);
        //intent.putExtra("text", text);
        //intent.putExtra("datetime", datetime);
        //intent.putExtra("number", number);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class NotificationsData {

        public String name;
        public NotificationsType type;
        public Date datetime;
        public String text;
        public String phoneNumber;

        NotificationsData(String name, NotificationsType type, Date datetime, String text, String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.datetime = datetime;
            this.text = text;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum NotificationsType {
        PushNotification,
        SMSNotification
    }

    public final static String name_text = "name";
    public final static String type_text = "type";
    public final static String date_text = "datetime";
    public final static String text_text = "text";
    public final static String phone_text = "phone";

    public final static String pushText = "push";
    public final static String smsText = "sms";

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
