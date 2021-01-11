package com.example.lifecycle.View.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lifecycle.R;
import com.example.lifecycle.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;



public class CalendarView extends RelativeLayout {

    private List<Integer> dayList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();

    protected List<CalendarItem> itemViewList = new ArrayList<>();
    protected Context mContext;
    protected LinearLayout calenderViewLl;
    protected int curPosition;

    public CalendarView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_view_layout, this);
        calenderViewLl = view.findViewById(R.id.calender_view);
        getTimeList();
        initItemViews();
    }


    private void getTimeList() {
        dateList.addAll(TimeUtil.getBeforeDateListByNow());
        dayList.addAll(TimeUtil.dateListToDayList(dateList));
    }

    private void initItemViews() {
        for (int i = 0; i < dateList.size(); i++) {
            int day = dayList.get(i);
            String curItemDate = dateList.get(i);
            final CalendarItem itemView;
            if(day == TimeUtil.getCurrentDay()){
                itemView = new CalendarItem(mContext, "today", String.valueOf(day), i, curItemDate);
            }else{
                itemView = new CalendarItem(mContext, TimeUtil.getCurWeekDay(curItemDate), String.valueOf(day), i, curItemDate);
            }

            itemViewList.add(itemView);
            calenderViewLl.addView(itemView);

            itemView.setOnCalenderItemClick(new CalendarItem.OnCalenderItemClick() {
                @Override
                public void onCalenderItemClick() {
                    curPosition = itemView.getPosition();
                    setCheckedColor(curPosition);// If selected, set color on it

                    //OnClick
                    if (calenderItemClickListener != null) {
                        calenderItemClickListener.onClickToRefresh(curPosition,dateList.get(curPosition));
                    }
                }
            });
        }

        setCheckedColor(6);//Set Today's color visible

    }

    private void setCheckedColor(int position) {
        for (int i = 0; i < itemViewList.size(); i++) {
            if (position == i) {
                itemViewList.get(i).setChecked(true);
            } else {
                itemViewList.get(i).setChecked(false);
            }
        }
    }

    private CalendarItemClickListener calenderItemClickListener;



    public interface CalendarItemClickListener {
        void onClickToRefresh(int position, String curDate);
    }

    public void setOnCalenderItemClickListener(CalendarItemClickListener calenderClickListener) {
        this.calenderItemClickListener = calenderClickListener;
    }
}
