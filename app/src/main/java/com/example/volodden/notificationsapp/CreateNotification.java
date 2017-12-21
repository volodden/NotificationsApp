package com.example.volodden.notificationsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreateNotification extends AppCompatActivity {

    private static NotificationsData Data;
    static final String NAME = "name", TEXT = "text", PHONE = "phone", TYPE = "type", YEAR = "year",
                        MONTH = "month", DAY = "day", HOUR = "hour", MINUTE = "minute";

    public CreateNotification()
    {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(NAME, Data.name);
            outState.putString(TEXT, Data.text);
            outState.putString(PHONE, Data.phoneNumber);
            outState.putString(TYPE, typeToStr(Data.type));
            outState.putInt(YEAR, Data.datetime.getYear());
            outState.putInt(MONTH, Data.datetime.getMonth());
            outState.putInt(DAY, Data.datetime.getDay());
            outState.putInt(HOUR, Data.datetime.getHours());
            outState.putInt(MINUTE, Data.datetime.getMinutes());
            super.onSaveInstanceState(outState);
        }
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null)
//        {
//            super.onRestoreInstanceState(savedInstanceState);
//            Data.name = savedInstanceState.getString(NAME);
//            Data.phoneNumber = savedInstanceState.getString(PHONE);
//            Data.text = savedInstanceState.getString(TEXT);
//            Data.type = strToType(savedInstanceState.getString(TYPE));
//            Data.datetime = new Date(savedInstanceState.getInt(YEAR), savedInstanceState.getInt(MONTH), savedInstanceState.getInt(DAY),
//                    savedInstanceState.getInt(HOUR), savedInstanceState.getInt(MINUTE));
//        }
//        //UpdateFormfromData();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState != null)
        {
            if(extras != null)
                SetAllFromExtra();
            else
            {
                super.onRestoreInstanceState(savedInstanceState);
                Data = new NotificationsData();
                Data.name = savedInstanceState.getString(NAME);
                Data.phoneNumber = savedInstanceState.getString(PHONE);
                Data.text = savedInstanceState.getString(TEXT);
                Data.type = strToType(savedInstanceState.getString(TYPE));
                Data.datetime = new Date(savedInstanceState.getInt(YEAR), savedInstanceState.getInt(MONTH), savedInstanceState.getInt(DAY),
                        savedInstanceState.getInt(HOUR), savedInstanceState.getInt(MINUTE));
                SetFormFromData();
            }
        }
        else
        {
            InitializeData();
            //SetFormFromData();
        }
    }

//    protected void onSave() {
//        Intent intent = new Intent();
//        //intent.putExtra("type", type);
//        //intent.putExtra("text", text);
//        //intent.putExtra("datetime", datetime);
//        //intent.putExtra("number", number);
//        setResult(RESULT_OK, intent);
//        finish();
//    }

    public void InitializeData()
    {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        Date date = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
        String text = ((EditText)findViewById(R.id.Text)).getText().toString();
        String phone = ((EditText)findViewById(R.id.Phone)).getText().toString();
        String name = ((EditText)findViewById(R.id.Name)).getText().toString();
        //RadioButton rbPush = (RadioButton)findViewById(R.id.rbPush);
        //RadioButton rbSms = (RadioButton)findViewById(R.id.rbSms);
        Data = new NotificationsData(name, NotificationsType.PushNotification, date, text, phone);
    }

//    public void UpdateData()
//    {
//        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
//        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
//        RadioButton rbPush = (RadioButton)findViewById(R.id.rbPush);
//        //RadioButton rbSms = (RadioButton)findViewById(R.id.rbSms);
//        Data.datetime = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
//        Data.text = ((EditText)findViewById(R.id.Text)).getText().toString();
//        Data.phoneNumber = ((EditText)findViewById(R.id.Phone)).getText().toString();
//        Data.name = ((EditText)findViewById(R.id.Name)).getText().toString();
//        if (rbPush.isChecked())
//            Data.type = NotificationsType.PushNotification;
//        else
//            Data.type = NotificationsType.SMSNotification;
//    }

    public void SetAllFromExtra()
    {
        Bundle extras = getIntent().getExtras();
        String name_ = (String) extras.get("name");
        NotificationsType type = (NotificationsType) extras.get("type");
        String text_ = (String) extras.get("text");
        Date datetime = (Date) extras.get("datetime");
        String number = (String) extras.get("phonenumber");


        Data = new NotificationsData(name_, type, datetime, text_, number);

        ((EditText)findViewById(R.id.Name)).setText(name_);
        ((EditText)findViewById(R.id.Text)).setText(text_);
        ((EditText)findViewById(R.id.Phone)).setText(number);
        ((DatePicker)findViewById(R.id.datePicker)).updateDate(datetime.getYear(), datetime.getMonth(), datetime.getDay());
        ((TimePicker)findViewById(R.id.timePicker)).setHour(datetime.getHours());
        ((TimePicker)findViewById(R.id.timePicker)).setMinute(datetime.getMinutes());
        if (type == NotificationsType.PushNotification)
            ((RadioButton) findViewById(R.id.rbPush)).setChecked(true);
        else if (type == NotificationsType.SMSNotification)
            ((RadioButton) findViewById(R.id.rbSms)).setChecked(true);
    }

    public void SetFormFromData()
    {
        ((EditText)findViewById(R.id.Name)).setText(Data.name);
        ((EditText)findViewById(R.id.Text)).setText(Data.text);
        ((EditText)findViewById(R.id.Phone)).setText(Data.phoneNumber);
        ((DatePicker)findViewById(R.id.datePicker)).updateDate(Data.datetime.getYear(), Data.datetime.getMonth(), Data.datetime.getDay());
        ((TimePicker)findViewById(R.id.timePicker)).setHour(Data.datetime.getHours());
        ((TimePicker)findViewById(R.id.timePicker)).setMinute(Data.datetime.getMinutes());
        if (Data.type == NotificationsType.PushNotification)
            ((RadioButton) findViewById(R.id.rbPush)).setChecked(true);
        else if (Data.type == NotificationsType.SMSNotification)
            ((RadioButton) findViewById(R.id.rbSms)).setChecked(true);
    }

    public static class NotificationsData {

        public NotificationsType type;
        public Date datetime;
        public String text;
        public String phoneNumber;
        public String name;

        NotificationsData(String name, NotificationsType type, Date datetime, String text, String phoneNumber) {
            this.type = type;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.datetime = datetime;
            this.text = text;
            //давать id при создании класса!
        }
        NotificationsData()
        {
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



    public void onButtonSms(View view)
    {
        findViewById(R.id.Phone).setVisibility(View.VISIBLE);
    }

    public void onButtonPush(View view)
    {
        findViewById(R.id.Phone).setVisibility(View.INVISIBLE);
    }

    public void onButtonCreate(View view)
    {
        //UpdateData();

    }

    public void onButtonBack(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
