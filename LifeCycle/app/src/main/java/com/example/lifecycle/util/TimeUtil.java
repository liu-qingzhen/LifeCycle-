package com.example.lifecycle.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class TimeUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static Calendar mCalendar = Calendar.getInstance();
    private static String[] weekStrings = new String[]{"Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static String[] rWeekStrings = new String[]{"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public static String getCurTime(){
        SimpleDateFormat dFormat = new SimpleDateFormat("HH:mm");
        String time = dFormat.format(System.currentTimeMillis());
        return time;
    }

  //get week time
    public static String getWeekStr(String dateStr){

        String todayStr = dateFormat.format(mCalendar.getTime());

        if(todayStr.equals(dateStr)){
            return getCurTime();
        }

        Calendar preCalendar = Calendar.getInstance();
        preCalendar.add(Calendar.DATE, -1);
        String yesterdayStr = dateFormat.format(preCalendar.getTime());
        if(yesterdayStr.equals(dateStr)){
            return "Yesterday";
        }

        int w = 0;
        try {
            Date date = dateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0){
                w = 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rWeekStrings[w];
    }


    public static int getCurrentDay() {
        return mCalendar.get(Calendar.DATE);
    }


    public static String getCurrentDate() {
        String currentDateStr = dateFormat.format(mCalendar.getTime());
        return currentDateStr;
    }

    public static List<Integer> dateListToDayList(List<String> dateList) {
        Calendar calendar = Calendar.getInstance();
        List<Integer> dayList = new ArrayList<>();
        for (String date : dateList) {
            try {
                calendar.setTime(dateFormat.parse(date));
                int day = calendar.get(Calendar.DATE);
                dayList.add(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dayList;
    }


    // get recent seven days according to current day
    public static List<String> getBeforeDateListByNow(){
        List<String> weekList = new ArrayList<>();

        for (int i = -6; i <= 0; i++) {
            //Sunday is the first day
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            String date = dateFormat.format(calendar.getTime());
            weekList.add(date);
        }
        return weekList;
    }


    public static String getCurWeekDay(String curDate){
        int w = 0;
        try {
            Date date = dateFormat.parse(curDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0){
                w = 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekStrings[w];
    }
}
