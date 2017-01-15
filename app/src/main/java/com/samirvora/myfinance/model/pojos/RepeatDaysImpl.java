package com.samirvora.myfinance.model.pojos;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;

/**
 * Created by James on 21/08/2016.
 */
public class RepeatDaysImpl implements RepeatDays {

    private long mID; // 0
    private long mTransactionID; // 1
    private int mRepeatAmount; // 2
    private String mRepeatPeriod; // 3
    private int mRepeatDays; // 4

    private String mRepeatStartDate; // 5
    private String mRepeatEndDate; // 6

    public RepeatDaysImpl(){}

    public RepeatDaysImpl(long transID, int repatAmount,
                          String repeatPeriod, int repeatDays,
                          String startDate, String endDate){
        this.mTransactionID = transID;
        this.mRepeatAmount = repatAmount;
        this.mRepeatPeriod = repeatPeriod;
        this.mRepeatDays = repeatDays;
        this.mRepeatStartDate = startDate;
        this.mRepeatEndDate = endDate;
    }


    public RepeatDaysImpl(long id, long transID, int repatAmount,
                          String repeatPeriod, int repeatDays,
                          String startDate, String endDate){
        this.mID = id;
        this.mTransactionID = transID;
        this.mRepeatAmount = repatAmount;
        this.mRepeatPeriod = repeatPeriod;
        this.mRepeatDays = repeatDays;
        this.mRepeatStartDate = startDate;
        this.mRepeatEndDate = endDate;
    }


    @Override
    public long getID() {
        return mID;
    }
    @Override
    public void setID(long id) {
        mID = id;
    }

    @Override
    public long getTransactionID() {
        return mTransactionID;
    }
    @Override
    public void setTransactionID(long transactionID) {
        mTransactionID = transactionID;
    }

    @Override
    public int getRepeatAmount() {
        return mRepeatAmount;
    }
    @Override
    public void setRepeatAmount(int repeat_amount) {
        mRepeatAmount = repeat_amount;
    }

    @Override
    public String getRepeatPeriod() {
        return mRepeatPeriod;
    }
    @Override
    public void setRepeatPeriod(String repeat_period) {
        mRepeatPeriod = repeat_period;
    }

    @Override
    public int getRepeatDays() {
        return mRepeatDays;
    }
    @Override
    public void setRepeatDays(int repeat_days) {
        mRepeatDays = repeat_days;
    }

    @Override
    public String getRepeatEndDate() {
        return mRepeatEndDate;
    }
    @Override
    public void setRepeatEndDate(String repeatEndDate) {
        this.mRepeatEndDate = repeatEndDate;
    }

    @Override
    public String getRepeatStartDate() {
        return mRepeatStartDate;
    }
    @Override
    public void setRepeatStartDate(String repeatStartDate) {
        this.mRepeatStartDate = repeatStartDate;
    }


    @Override
    public String toString() {
       return "ID: "+getID()+" | mTransactionID: "+getTransactionID()+" | mRepeatAmount: "+
                getRepeatAmount()+" | mRepeatPeriod: "+getRepeatPeriod()+
                " | mRepeatDays: "+getRepeatDays()+" | mRepeatStartDate: "+getRepeatStartDate()+
                " | mRepeatEndDate: "+getRepeatEndDate();
    }

}
