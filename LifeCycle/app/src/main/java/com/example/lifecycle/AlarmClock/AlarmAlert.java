
package com.example.lifecycle.AlarmClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecycle.R;

public class AlarmAlert extends Activity implements SensorEventListener {
    private MediaPlayer mediaPlayer;
    private SensorManager mSensorManager;
    private Vibrator vibrator;
    private TextView Shake_Text,clockwords;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_alert);
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.clock);
        mediaPlayer.start();
        new AlertDialog.Builder(AlarmAlert.this)
                .setIcon(R.drawable.ic_launcher_background)
                .setIcon(R.mipmap.ic_small_blue_rectangular)
                .setTitle("Alarm on")
                .setMessage("Shake Your Phone!")
                .setPositiveButton("fine. ╮(╯▽╰)╭"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);


        Shake_Text =  findViewById(R.id.shake_text);
        clockwords=findViewById(R.id.clockwords);
        Shake_Text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Shake_Text.setText("Shake Your Phone!");
                num = 0;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {

            if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math.abs(values[2]) > 15)) {
                num++;
                Shake_Text.setText(25-num+" times" + " left");
                if(num<5){
                   clockwords.setText("Come on !! Get UP !!");
                }
                else if(num<15){
                    clockwords.setText("You are Closer！！");
                    }
                else if(num<26){
                    clockwords.setText("The dawn of victory is just ahead！！");
                }
                if(num>=25){
                    mediaPlayer.stop();
                    Toast toast = Toast.makeText(AlarmAlert.this,"YOU SUCCEEDED!", Toast.LENGTH_SHORT);
                    toast.show();
                    num = 0;
                    AlarmAlert.this.finish();
                }
                vibrator.vibrate(500);
            }
        }
    }
}

