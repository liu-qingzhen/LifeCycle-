package com.example.lifecycle.AlarmClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.counter.CounterActivity;
import com.example.lifecycle.MainActivity;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.clocktime;
import com.example.lifecycle.util.TimeUtil;
import com.higyon.myapplication.appcomm.greendao.clocktimeDao;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {
    private TimePicker timepicker;
    private  clocktimeDao cd = AppApplication.getDaoSession().getClocktimeDao();
    private String minString;
    private String hourSrring;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        RelativeLayout setalarmlayout = findViewById(R.id.clocklayout);

        timepicker = findViewById(R.id.timepicker);
        timepicker.setIs24HourView(true);

        final Calendar c = Calendar.getInstance();
        setalarmlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                //save the time you selected in timepicker to a calendar
                c.set(Calendar.HOUR_OF_DAY,timepicker.getHour());
                c.set(Calendar.MINUTE,timepicker.getMinute());
                c.set(Calendar.SECOND,0);
                Intent intent = new Intent (Alarm.this,AlarmReceiver.class);
                //intent.setAction("111111");
                PendingIntent pi = PendingIntent.getBroadcast(Alarm.this,1,intent,0);
                alarm.setExact(AlarmManager.RTC_WAKEUP,
                        c.getTimeInMillis(),pi);
                Toast toast = Toast.makeText(Alarm.this,"Alarm Set", Toast.LENGTH_SHORT);
                toast.show();
                minString = String.valueOf(timepicker.getCurrentMinute());
                hourSrring = String.valueOf(timepicker.getCurrentHour());
                if(timepicker.getCurrentMinute()<10){
                    minString = "0"+ minString;
                    if(timepicker.getCurrentMinute()==0){
                        minString = "0"+minString;
                    }
                }

                String getup = String.valueOf(hourSrring+":"+minString);
                String sleep = TimeUtil.getCurTime();
                clocktime clocktime1 = new clocktime(null,TimeUtil.getCurrentDate(),getup,sleep);
                cd.insert(clocktime1);

            }

        });
    }

//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //To cancel the alarm
//                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
//                PendingIntent pi = PendingIntent.getBroadcast(
//                        MainActivity.this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                alarm.cancel(pi);
//                Toast toast = Toast.makeText(MainActivity.this,"Cancel", Toast.LENGTH_SHORT);
//                toast.show();
//
//
//            }
//
//        });
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = new MenuInflater(this);
    menuInflater.inflate(R.menu.menu,menu);
    return super.onCreateOptionsMenu(menu);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_step:
                Intent intent = new Intent(Alarm.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_counter:
                Intent intent1 = new Intent(Alarm.this, CounterActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_clock:
                Intent intent2 = new Intent(Alarm.this, Alarm.class);
                startActivity(intent2);

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent (Alarm.this,AlarmAlert.class);
    }




    }