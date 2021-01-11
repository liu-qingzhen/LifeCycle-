package com.example.lifecycle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.lifecycle.AlarmClock.Alarm;
import com.example.lifecycle.AlarmClock.ClocktimeActivity;
import com.example.lifecycle.Footprinting.MapsActivity;
import com.example.lifecycle.bean.Step;
import com.example.lifecycle.View.calender.CalendarView;
import com.example.lifecycle.bean.Time;
import com.example.lifecycle.constant.Constant;
import com.example.lifecycle.counter.ChartActivity;
import com.example.lifecycle.counter.CounterActivity;
import com.example.lifecycle.service.StepService;
import com.example.lifecycle.util.StepCheckUtil;
import com.example.lifecycle.util.TimeUtil;
import com.higyon.myapplication.appcomm.greendao.StepDao;
import com.higyon.myapplication.appcomm.greendao.TimeDao;
import com.higyon.myapplication.appcomm.greendao.clocktimeDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements android.os.Handler.Callback {
    private LinearLayout calendarLayout;
    private RelativeLayout counterLayout;
    private RelativeLayout steplayout, alarmLayout;
    private TextView kmTimeTv;
    private TextView totalKmTv;
    private TextView stepsTimeTv;
    private TextView totalStepsTv;
    private TextView supportTv;
    private TextView focusTimeTv;



    public static int screenWidth, screenHeight;
    private CalendarView calenderView;

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<Step> stepEntityList = new ArrayList<>();
    private StepDao stepDataDao;
    private TimeDao timeDataDao;
    private clocktimeDao clockDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initData();
        initListener();
        onClickListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_step:
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_counter:
                Intent intent1 = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_clock:
                Intent intent2 = new Intent(MainActivity.this, Alarm.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        calendarLayout =  findViewById(R.id.calendar_layout);//layout to put the calendar
        kmTimeTv = findViewById(R.id.km_Time_tv);
        totalKmTv =  findViewById(R.id.km_tv);
        stepsTimeTv =  findViewById(R.id.steps_time_tv);
        totalStepsTv =  findViewById(R.id.steps_tv);
        supportTv =  findViewById(R.id.is_support_tv);
        focusTimeTv = findViewById(R.id.focus_time_tv) ;
        counterLayout =findViewById(R.id.counter_layout);
        alarmLayout =findViewById(R.id.alarm_layout) ;
        steplayout=findViewById(R.id.step_layout);
        curSelDate = TimeUtil.getCurrentDate();

    }

    private void initData() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();


        calenderView = new CalendarView(this);
        calendarLayout.addView(calenderView);//set calendar
        //If the device support counting step
        if (StepCheckUtil.isSupportStepCountSensor(this)) {
            getRecordList();
            supportTv.setVisibility(View.GONE);
            setDatas();
            setupService();
        } else {
            totalStepsTv.setText("0");
            supportTv.setVisibility(View.VISIBLE);
        }
    }


    private void initListener() {
        calenderView.setOnCalenderItemClickListener(new CalendarView.CalendarItemClickListener() {
            @Override
            public void onClickToRefresh(int position, String curDate) {
                //get selected date
                curSelDate = curDate;
                //set data of selected date
                setDatas();
            }
        });
    }


    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Messenger messenger;

    //Start service
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


    private TimerTask timerTask;
    private Timer timer;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            //Set timer, update steps every 3 seconds
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {//sen message to service to send out steps
                        messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_MAINACTIVITY);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void setDatas() {

        Step stepEntity = stepDataDao.queryBuilder().where(StepDao.Properties.Date.eq(curSelDate)).unique();

        List<Time> listtime = timeDataDao.queryBuilder().where(TimeDao.Properties.Date.like(curSelDate)).list();
        int totaltime = 0;

        if(listtime!= null){
            for(int i=0;i<listtime.size();i++){
                totaltime = totaltime+listtime.get(i).getTime();
            }
            focusTimeTv.setText(String.valueOf(totaltime));
        }else {
            focusTimeTv.setText("0");
        }

        if (stepEntity != null) {
            int steps = Integer.parseInt(stepEntity.getStep());

            totalStepsTv.setText(String.valueOf(steps));
            totalKmTv.setText(countTotalKM(steps));
        } else {
            totalStepsTv.setText("0");
            totalKmTv.setText("0");
        }

        String time = TimeUtil.getWeekStr(curSelDate);
        kmTimeTv.setText(time);
        stepsTimeTv.setText(time);
    }

    private String countTotalKM(int steps) {
        double totalMeters = steps * 0.7;
        return df.format(totalMeters / 1000);
    }


// get history data
    private void getRecordList() {
        stepDataDao = AppApplication.getDaoSession().getStepDao();
        //stepDataDao.deleteAll();
        timeDataDao = AppApplication.getDaoSession().getTimeDao();
        //timeDataDao.deleteAll();
        clockDataDao = AppApplication.getDaoSession().getClocktimeDao();
        //clockDataDao.deleteAll();
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.loadAll());

    }
    private void onClickListener() {
        counterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("extra_data",curSelDate);
                startActivity(intent);

            }
        });
        steplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, MapsActivity.class);
                intent1.putExtra("extra_data",curSelDate);
                startActivity(intent1);

            }
        });
        alarmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, ClocktimeActivity.class);
                startActivity(intent2);

            }
        });
    }
// handle message from stepservice
    @Override
    public boolean handleMessage(Message msg) {
                switch (msg.what) {

                    case Constant.MSG_FROM_SERVICE:
                        if (curSelDate.equals(TimeUtil.getCurrentDate())) {
                            int steps = msg.getData().getInt("steps");
                            totalStepsTv.setText(String.valueOf(steps));
                            totalKmTv.setText(countTotalKM(steps));
                        }
                        break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //there will be problem if bound many times.
        if (isBind) this.unbindService(conn);
    }
}
