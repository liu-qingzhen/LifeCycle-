package com.example.lifecycle.counter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lifecycle.AppApplication;
import com.example.lifecycle.R;
import com.example.lifecycle.bean.Time;
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

public class TotalTimeActivity extends AppCompatActivity {

    private BarChart mBarChart;
    private BarData mBarData;
    private TimeDao timeDao;

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

        // y axis
        ArrayList<BarEntry> yValues = new ArrayList<>();
        //x axis
        final ArrayList<String> xValues = new ArrayList<>();

        List<Time> mTime = new ArrayList<>();
        timeDao = AppApplication.getDaoSession().getTimeDao();
        mTime = timeDao.loadAll();
        //add task time
        for (int x = 0; x < mTime.size(); x++)
        {
            float y = mTime.get(x).getTime();
            yValues.add(new BarEntry(x, y));
        }
        //get task name
        for(int i = 0; i<mTime.size() ; i++)
        {
            xValues.add(mTime.get(i).getName());
        }
        XAxis xl = mBarChart.getXAxis();
        //set task time
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

        mBarChart.animateY(1000);
        // set y axis data
        BarDataSet barDataSet = new BarDataSet(yValues,"Recent Seven Days");


        mBarData = new BarData(barDataSet);
    }

    private void initBarChart() {
        mBarChart.setData(mBarData);
    }

}
