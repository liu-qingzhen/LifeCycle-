package com.example.lifecycle.counter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Time;
import com.example.lifecycle.util.TimeUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.higyon.myapplication.appcomm.greendao.TimeDao;

import java.util.ArrayList;
import java.util.List;
// Barchart API
public class ChartActivity extends AppCompatActivity {

    private BarChart mBarChart;
    private BarData mBarData;
    private TimeDao timeDao;
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
        //x axis data
        final ArrayList<String> xValues = new ArrayList<>();
        // y axis data
        ArrayList<BarEntry> yValues = new ArrayList<>();
        //get current date
        Intent intent = getIntent();
        date = intent.getStringExtra("extra_data");
        List<Time> mTime = new ArrayList<>();
        //get data base
        timeDao = AppApplication.getDaoSession().getTimeDao();
        mTime = timeDao.queryBuilder().where(TimeDao.Properties.Date.like(date)).list();
        //add task time
        for (int x = 0; x < mTime.size(); x++) {
            float y = mTime.get(x).getTime();
            yValues.add(new BarEntry(x, y));
        }
        //get task name
        for(int i = 0; i<mTime.size() ; i++)
        {
            xValues.add(mTime.get(i).getName());
        }
        XAxis xl = mBarChart.getXAxis();
        //set task name
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
        xl.setLabelCount(mTime.size());

        mBarChart.animateY(2000);
        BarDataSet barDataSet = new BarDataSet(yValues,date );
        mBarData = new BarData(barDataSet);
    }

    private void initBarChart() {
        mBarChart.setData(mBarData);
    }

}