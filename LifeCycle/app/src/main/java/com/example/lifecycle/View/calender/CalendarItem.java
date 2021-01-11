package com.example.lifecycle.View.calender;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lifecycle.MainActivity;
import com.example.lifecycle.R;



/**
 * 查看一周内每天的历史纪录日历Item布局
 */

public class CalendarItem extends RelativeLayout {
    private static final String TAG = "RecordsCalenderItemView";

    private Context mContext;

    private LinearLayout itemLl;
    private View lineView;
    private TextView weekTv;
    private RelativeLayout dateRl;
    private TextView dateTv;
    private String weekStr, dateStr;
    private int position;
    protected String curItemDate;


    OnCalenderItemClick itemClick = null;

    public interface OnCalenderItemClick {
        public void onCalenderItemClick();
    }

    public void setOnCalenderItemClick(OnCalenderItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public CalendarItem(Context context, String week, String date, int position, String curItemDate) {
        super(context);
        this.mContext = context;
        this.weekStr = week;
        this.dateStr = date;
        this.position = position;
        this.curItemDate = curItemDate;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.records_calender_item_view, this);
        itemLl =  itemView.findViewById(R.id.records_calender_item_ll);
        weekTv =  itemView.findViewById(R.id.records_calender_item_week_tv);
        lineView = itemView.findViewById(R.id.calendar_item_line_view);
        dateRl =  itemView.findViewById(R.id.records_calender_item_date_rl);
        dateTv =  itemView.findViewById(R.id.records_calender_item_date_tv);



        weekTv.setTextSize(15);
        lineView.setVisibility(GONE);//The line between calendar week and date

        weekTv.setText(weekStr);
        dateTv.setText(dateStr);
        //set width of each item
        itemView.setLayoutParams(new LayoutParams((MainActivity.screenWidth) / 7,
                ViewGroup.LayoutParams.MATCH_PARENT));

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onCalenderItemClick();
            }
        });

    }

    //return ID of current item
    public int getPosition() {
        return position;
    }

    public void setChecked(boolean checkedFlag) {

        if (checkedFlag) {
            // after being selected
            weekTv.setTextColor(getResources().getColor(R.color.main_text_color));
            dateTv.setTextColor(getResources().getColor(R.color.white));
            dateRl.setBackgroundResource(R.mipmap.ic_blue_round_bg);
        } else {
            // without being selected
            weekTv.setTextColor(getResources().getColor(R.color.gray_default_dark));
            dateTv.setTextColor(getResources().getColor(R.color.gray_default_dark));
            dateRl.setBackgroundColor(Color.TRANSPARENT);
        }

    }
}
