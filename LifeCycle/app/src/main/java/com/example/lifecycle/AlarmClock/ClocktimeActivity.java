package com.example.lifecycle.AlarmClock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Time;
import com.example.lifecycle.bean.clocktime;
import com.example.lifecycle.util.TimeUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.higyon.myapplication.appcomm.greendao.TimeDao;
import com.higyon.myapplication.appcomm.greendao.clocktimeDao;

import java.util.ArrayList;
import java.util.List;

public class ClocktimeActivity extends AppCompatActivity {

    private BarChart mBarChart;
    private BarData mBarData;
    private BarData mBarData2;
    private com.higyon.myapplication.appcomm.greendao.clocktimeDao clocktimeDao;
    private String date;
    private String CurrentDate = TimeUtil.getCurrentDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart);

        initView();
        initData();
        initBarChart();
    }

    private void initView() {
        mBarChart = findViewById(R.id.barchart);
    }

    private void initData() {

        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<BarEntry> yValues2 = new ArrayList<>();

        final ArrayList<String> xValues = new ArrayList<>();

        List<clocktime> mTime = new ArrayList<>();
        clocktimeDao = AppApplication.getDaoSession().getClocktimeDao();
        mTime = clocktimeDao.loadAll();

        for (int x = 0; x < mTime.size(); x++) {
            // 2.0 ----xValues.add(String.valueOf(i));
            float y = Integer.valueOf(mTime.get(x).getGetUptime().replace(":",""));
            float y2 = Integer.valueOf(mTime.get(x).getSleeptime().replace(":",""));
            yValues.add(new BarEntry(x, y));
            yValues.add(new BarEntry(x, y2));

        }
//        for (int x = 0; x < mTime.size(); x++) {
////            // 2.0 ----xValues.add(String.valueOf(i));
////            float y = Integer.parseInt(mTime.get(x).getSleeptime().replace(":",""));
////            yValues2.add(new BarEntry(x, y));
////        }
        for(int i = 0; i<mTime.size() ; i++)
        {
            xValues.add(mTime.get(i).getDate());
        }
        XAxis xl = mBarChart.getXAxis();
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("TAG", "----->getFormattedValue: " + value);
                int v = (int) value;
                if (v <= xValues.size() && v >= 0) {
                    String st = xValues.get(v);
                    String tim1 = "";
                    tim1 = st;
                    return tim1;
                } else {
                    return null;
                }
            }
        };
        xl.setValueFormatter(formatter);
        xl.setDrawGridLines(false);
        xl.setLabelCount(xValues.size());

        mBarChart.animateY(2000);
        // y axis data
        BarDataSet barDataSet = new BarDataSet(yValues,"LifeCycleTime" );
        mBarData = new BarData(barDataSet);

    }

    private void initBarChart() {
        mBarChart.setData(mBarData);

    }


}