package com.samirvora.myfinance.model.pojos;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.TotalTransaction;

/**
 * Created by samirv on 30/08/2016.
 */
public class TotalTransactionImpl implements TotalTransaction {

    private long mID;
    private int mTotal;
    private int mMonth;
    private int mYear;
    private String mType;

    public TotalTransactionImpl(){}

    public TotalTransactionImpl(int total, int month, int year, String type){
        this.mTotal = total;
        this.mMonth = month;
        this.mYear = year;
        this.mType = type;
    }

    public TotalTransactionImpl(long id, int total, int month, int year, String type){
        this.mID = id;
        this.mTotal = total;
        this.mMonth = month;
        this.mYear = year;
        this.mType = type;
    }

    @Override
    public long getID() {
        return mID;
    }
    @Override
    public void setID(long id) {
        this.mID = id;
    }

    @Override
    public int getTotal() {
        return mTotal;
    }
    @Override
    public void setTotal(int total) {
        this.mTotal = total;
    }

    @Override
    public int getMonth() {
        return mMonth;
    }
    @Override
    public void setMonth(int month) {
        this.mMonth = month;
    }

    @Override
    public int getYear() {
        return mYear;
    }
    @Override
    public void setYear(int year) {
        this.mYear = year;
    }

    @Override
    public String getType() {
        return mType;
    }
    @Override
    public void setType(String type) {
        this.mType = type;
    }
}
