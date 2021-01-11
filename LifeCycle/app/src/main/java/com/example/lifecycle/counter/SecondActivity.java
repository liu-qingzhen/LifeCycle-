package com.example.lifecycle.counter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.R;
import com.example.lifecycle.View.CircleTimeView;

public class SecondActivity extends AppCompatActivity {


    private ImageButton workStop;
    private CircleTimeView circleTimeView;
    private NotificationManager notificationManager;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        workStop =  findViewById(R.id.work_stop);
        circleTimeView =  findViewById(R.id.CircleTimeView1);
        circleTimeView.setLongtime(AppApplication.getInstances().getWork().getWorklong(),
                AppApplication.getInstances().getWork().getCurrentlong(), Color.parseColor("#FFFFFF"));
        setHandler();
        workStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });;
        setHandler();

    }
    // get circleview status
    protected void setHandler(){

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1001){
                    Toast.makeText( getApplicationContext(),"Succeed!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        circleTimeView.setTHandler(mHandler);
        mHandler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        String id = "channel_002";
        String name = "name";
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this,mChannel.getId())
                        .setChannelId(id)
                        .setContentTitle("Reminder")
                        .setContentText("Keep focused!")
                        .setSmallIcon(R.mipmap.ic_launcher_round).build();
            } else {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"defult")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentTitle("Reminder")
                    .setContentText("Keep focused!")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setOngoing(true)
                    .setChannelId(id);//

            notification = notificationBuilder.build();
        }
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
