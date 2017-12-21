package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Date;

public class CreateNotification extends AppCompatActivity {

    private NotificationsData data;

    public CreateNotification() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if( outState != null ) {
            outState.putString(name_text, data.name);
            outState.putString(text_text, data.text);
            outState.putString(phone_text, data.phoneNumber);
            outState.putString(type_text, typeToStr(data.type));
            //ToDo
            //date
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        String name = null;
        String typeString = null;
        String text = null;
        String dateString = null;
        String phone = null;

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        EditText editTextName = (EditText) findViewById(R.id.name);
        EditText editTextPhone = (EditText) findViewById(R.id.phone);
        EditText editTextMSG = (EditText) findViewById(R.id.msgtext);

        Log.i("CRN", "Start");
        if( savedInstanceState == null )
        {
            Log.i("CRN", "Start savedInstance null");
            Bundle extras = getIntent().getExtras();
            if( extras != null ) {
                Log.i("CRN", "Start Bundle not null");
                name = extras.getString(name_text);
                typeString = extras.getString(type_text);
                text = extras.getString(text_text);
                dateString = extras.getString(date_text);
                phone = setPhone(extras.getString(phone_text));
            } else {
                choiseRadioButton(pushText);
                return;
            }
        } else {
            name = savedInstanceState.getString(name_text);
            typeString = savedInstanceState.getString(type_text);
            text = savedInstanceState.getString(text_text);
            dateString = savedInstanceState.getString(CreateNotification.date_text);
            phone = setPhone(savedInstanceState.getString(CreateNotification.phone_text));
        }

        editTextName.setText(name);
        editTextMSG.setText(text);

        editTextName.setSelection(editTextName.getText().length());

        NotificationsType type = CreateNotification.strToType(typeString);

        //ToDo
        Date datetime = null;
        //timePicker.set()
        //datePicker.set()

        data = new NotificationsData(name, type, datetime, text, phone);
    }

    public static String setPhone(String number) {
        if( number.equals(null_text) ) {
            return "";
        } else {
            return number;
        }
    }

    private void choiseRadioButton(String button) {
        if( button.equals(pushText) ) {
            findViewById(R.id.phone).setVisibility(View.INVISIBLE);
            data.type = NotificationsType.PushNotification;
            return;
        }
        if( button.equals(smsText) ) {
            findViewById(R.id.phone).setVisibility(View.VISIBLE);
            data.type = NotificationsType.SMSNotification;
            return;
        }
        //exception
        return;
    }

    public static class NotificationsData {

        public String name;
        public NotificationsType type;
        public Date datetime;
        public String text;
        public String phoneNumber;

        NotificationsData(String name, NotificationsType type, Date datetime, String text, String phoneNumber) {
            this.name = name;
            this.type = type;
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
    public final static String null_text = "null";

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

    public void onButtonSms(View view)
    {
        choiseRadioButton(smsText);
    }

    public void onButtonPush(View view)
    {
        choiseRadioButton(pushText);
    }

    public void onButtonCreate(View view)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(name_text, data.name);
        returnIntent.putExtra(text_text, data.text);
        returnIntent.putExtra(phone_text, (data.phoneNumber == null ? null_text : data.phoneNumber));
        returnIntent.putExtra(type_text, typeToStr(data.type));
        returnIntent.putExtra(date_text, data.datetime.toString());
        setResult(MainActivity.RESULT_OK, returnIntent);
        finish();
    }

    public void onButtonBack(View view)
    {
        Intent returnIntent = new Intent();
        setResult(MainActivity.RESULT_CANCEL, returnIntent);
        finish();
    }
}
