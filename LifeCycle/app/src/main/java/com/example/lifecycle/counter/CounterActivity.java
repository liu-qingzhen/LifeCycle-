package com.example.lifecycle.counter;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lifecycle.AlarmClock.Alarm;
import com.example.lifecycle.AppApplication;
import com.example.lifecycle.MainActivity;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Time;
import com.example.lifecycle.util.CommonUtil;
import com.example.lifecycle.util.TimeUtil;
import com.higyon.myapplication.appcomm.greendao.TimeDao;

public class CounterActivity extends AppCompatActivity {

    private ImageButton Chart;
    private Button set_time;
    private AlertDialog dialog = null;
    private TextView workTime1;
    private EditText TaskName;
    private RelativeLayout start;
    private RelativeLayout set_work;
    private  int WorkTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        createOnClickListener();

        Chart =  findViewById(R.id.Total_time);
        Chart.setOnClickListener(onclicklistener);
        TaskName = findViewById(R.id.work_name);
        set_work =  findViewById(R.id.setwork);
        set_work.setOnClickListener(onclicklistener);
        set_time =  findViewById(R.id.set_time);
        set_time.setOnClickListener(onclicklistener);
        start =  findViewById(R.id.start);
        start.setOnClickListener(onclicklistener);
        workTime1 =  findViewById(R.id.work_time1);
        workTime1.setText(AppApplication.getInstances().getWork().getWorklong() + "minutes");
    }
    public void createOnClickListener(){
        onclicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dispenser(view.getId());
            }
        };
    }

    private View.OnClickListener onclicklistener = null;
    //
    public void  Dispenser(int id){
        if(id == R.id.start){
            startWork();
        }
        else if(id == R.id.setwork || id == R.id.set_time){
            createAlertDialog();
        }
        else if(id == R.id.Total_time){
            toTotalTimeActivity();
        }
    }


    final View.OnClickListener yes = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SeekBar seekbar =  dialog.findViewById(R.id.seekbar1);
            WorkTime = seekbar.getProgress();
            AppApplication.getInstances().getWork().setWorklong(CommonUtil.MinutesToSeconds(WorkTime));
            workTime1.setText(WorkTime + "minutes");
            dialog.cancel();
        }

    };

    public void createAlertDialog() {

        dialog = DialogDefault.createAlertDialog(CounterActivity.this,
                "Set the time", R.layout.activity_main_dialog,
                yes, no);
        SeekBar seekbar=  dialog.findViewById(R.id.seekbar1);
        seekbar.setProgress(CommonUtil.SecondsToMinutes(AppApplication.getInstances().getWork().getWorklong()));
    }

    final View.OnClickListener no = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.cancel();
        }
    };

    public void startWork(){
        String taskname = TaskName.getText().toString().trim();
        String date = TimeUtil.getCurrentDate();
        TimeDao mTimeDao =AppApplication.getDaoSession().getTimeDao();
        Time mTime = new Time(null,date,taskname, WorkTime);
        mTimeDao.insert(mTime);
        AppApplication.getInstances().getWork().setWorkname(taskname);

        Intent intent = new Intent(CounterActivity.this, SecondActivity.class);
        startActivity(intent);
        CounterActivity.this.finish();
    }


    public void toTotalTimeActivity(){
        Intent intent = new Intent(CounterActivity.this, TotalTimeActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_step:
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_counter:
                Intent intent1 = new Intent(CounterActivity.this, CounterActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_clock:
                Intent intent2 = new Intent(CounterActivity.this, Alarm.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
