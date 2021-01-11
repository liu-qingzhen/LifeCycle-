package com.example.lifecycle.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;


import com.example.lifecycle.AppApplication;
import com.example.lifecycle.MainActivity;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Step;
import com.example.lifecycle.constant.Constant;
import com.example.lifecycle.util.TimeUtil;
import com.higyon.myapplication.appcomm.greendao.StepDao;

import java.text.SimpleDateFormat;
import java.util.Date;



public class StepService extends Service implements SensorEventListener {


    private static String CURRENT_DATE;
    private int CURRENT_STEP;

    private static int saveInterval = 3000;  //time interval to save data
    private SensorManager sensorManager;
    private StepDao stepDataDao;//Database
    private static int stepSensor = -1;//step counter : 0-counter 1-detector
    private BroadcastReceiver broadcastReceiver;
    private TimeCount timeCount;
    private Messenger messenger = new Messenger(new MessengerHandler());//send message to get steps from Stepservice
    private boolean hasRecord;//if there is record today
    private int hasStepCount;//steps has been count
    private int previousStepCount;//steps before next record
    private Notification.Builder builder;//For notification
    private NotificationManager notificationManager;
    private Intent notiIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                getStepCounter();
            }
        }).start();
        startTimeCount();
        initData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //set foreground to keep from being killed
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        builder = new Notification.Builder(this.getApplicationContext());
        notiIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, notiIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Steps Today"+CURRENT_STEP)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("How's it going?");
        Notification stepNotification = builder.build();

        notificationManager.notify(110,stepNotification);
        // notify id and the notification to use
        startForeground(110, stepNotification);
        return START_STICKY;// rebuild and start service after service stop
    }


    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_MAINACTIVITY:
                    try {
                        //send steps for mainactivity to handle
                        Messenger messenger = msg.replyTo;
                        Message replyToMainMsg = Message.obtain(null, Constant.MSG_FROM_SERVICE);
                        Bundle bundle = new Bundle();
                        bundle.putInt("steps", CURRENT_STEP);
                        replyToMainMsg.setData(bundle);
                        messenger.send(replyToMainMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }



    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        //sensor will not save data after shutdown.
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case Intent.ACTION_SCREEN_OFF:
                        saveInterval = 10000;
                        break;
                    case Intent.ACTION_SHUTDOWN:
                        saveStepData();
                        break;
                    case Intent.ACTION_USER_PRESENT:
                        saveInterval = 3000;
                        break;
                    case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                        saveStepData();
                        break;
                    case Intent.ACTION_DATE_CHANGED:
                    case Intent.ACTION_TIME_CHANGED:
                    case Intent.ACTION_TIME_TICK:
                        saveStepData();
                        isNewDay();
                        break;
                    default:
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
    }


    private void initData() {
        CURRENT_DATE = TimeUtil.getCurrentDate();
        stepDataDao = AppApplication.getDaoSession().getStepDao();
       //get today's data to show
        Step entity = stepDataDao.queryBuilder().where(StepDao.Properties.Date.eq(CURRENT_DATE)).unique();
        if (entity == null)   {
            CURRENT_STEP = 0;
        } else {
            CURRENT_STEP = Integer.parseInt(entity.getStep());
        }
    }


    //when time passes 00:00
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) ||
                !CURRENT_DATE.equals(TimeUtil.getCurrentDate())) {
            initData();
        }
    }


    //get sensors
    private void getStepCounter() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        // step counter sensor available after android 4.4
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        }
    }



    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensor = 0;
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensor = 1;
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    //count steps
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensor == 0) {
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                int thisStepCount = tempStep - hasStepCount;
                CURRENT_STEP += (thisStepCount - previousStepCount);
                previousStepCount = thisStepCount;
            }
        } else if (stepSensor == 1) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    // for database to save steps, save steps every saveinterval
    private void startTimeCount() {
        timeCount = new TimeCount(saveInterval, 1000);
        timeCount.start();
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            timeCount.cancel();
            saveStepData();
            startTimeCount();
        }
    }



    private void saveStepData() {
        //query data
        Step stepentity = stepDataDao.queryBuilder().where(StepDao.Properties.Date.eq(CURRENT_DATE)).unique();
        if (stepentity == null) {
            stepentity = new Step();
            stepentity.setDate(CURRENT_DATE);
            stepentity.setStep(String.valueOf(CURRENT_STEP));
            stepDataDao.insert(stepentity);
        }
        else {
            stepentity.setStep(String.valueOf(CURRENT_STEP));
            stepDataDao.update(stepentity);
        }

        builder.setContentIntent(PendingIntent.getActivity(this, 0, notiIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Steps Today"+CURRENT_STEP)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("How's it going?");

        Notification stepNotification = builder.build();
        notificationManager.notify(110,stepNotification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
