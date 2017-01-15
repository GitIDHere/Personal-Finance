package com.samirvora.myfinance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 29/10/2016.
 */
public class DateHelper {

    public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    public static final String VIEW_DATE_FORMAT = "dd/MM/yyyy";

    private SimpleDateFormat mSDF;
    private Calendar mCalendar;

    private int CURRENT_DAY = 02;
    private int CURRENT_MONTH  = 02;
    private int CURRENT_YEAR = 2016;
    private String TODAYS_DATE;

    public static final int DAYS_IN_WEEK = 7;
    public static final double DAYS_IN_MONTH = 30.4167;
    public static final double DAYS_IN_YEAR = 365.25;


    /* SINGLETON CONSTRUCTOR */
    private static DateHelper mDateHelperInstance;

    public static DateHelper getInstance(){
        if(mDateHelperInstance == null){
            mDateHelperInstance = new DateHelper();
        }
        return mDateHelperInstance;
    }

    private DateHelper(){
        mCalendar = Calendar.getInstance();
        mSDF = new SimpleDateFormat(DB_DATE_FORMAT, Locale.UK);

//        CURRENT_DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
//        CURRENT_MONTH = mCalendar.get(Calendar.MONTH) + 1;
//        CURRENT_YEAR = mCalendar.get(Calendar.YEAR);

        //TODAYS_DATE = CURRENT_DAY+"/"+CURRENT_MONTH+"/"+CURRENT_YEAR;

        try{
            mCalendar.setTime(mSDF.parse(CURRENT_YEAR+"-"+CURRENT_MONTH+"-"+CURRENT_DAY));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    /* SINGLETON CONSTRUCTOR*/



    public Date getTodaysDateAsDate(){
        return mCalendar.getTime();
    }

    public String getTodaysDateAsString(){
        return mSDF.format(mCalendar.getTime());
    }

    public int getCurrentDay(){
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentMonth(){
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    public int getCurrentYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    public void setYear(int year){
        mCalendar.set(Calendar.YEAR, year);
    }

    public void setMonth(int month){
        mCalendar.set(Calendar.MONTH, (month - 1));
    }

    public void setDay(int day){
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
    }



    public String getDateFromCalendar(Calendar date){
        return mSDF.format(date.getTime());
    }





    public Date getDateFromString(String date){
        try{
            return mSDF.parse(date);
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }

    }


    public int getDaysBetweenDates(String startDate, String endDate){

        Calendar sDate = getCalendarFromDate(startDate);
        Calendar eDate = getCalendarFromDate(endDate);

        int daysBetween = 0;

        while (sDate.compareTo(eDate) < 0) {
            daysBetween++;
            sDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return daysBetween;
    }


    public Calendar getCalendarFromDate(String date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(getDateFromString(date));
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second
        return cal;                                  // return the date part
    }

    public int compareStringDates(String firstDate, String secondDate){
        Calendar currentDate = getCalendarFromDate(firstDate);
        Calendar endMonth = getCalendarFromDate(secondDate);
        return currentDate.compareTo(endMonth);

        // 0 = equal
        // 1 = if this cal is AFTER the other
        // -1 = if this cal is BEFORE the other
    }




}
