package com.samirvora.myfinance.model.pojos;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatTransaction;
/*
 * Created by James on 27/08/2016.
 */
public class RepeatTransactionImpl implements RepeatTransaction {

    private long mRepeatID;
    private Long mTransactionID;
    private String mRepeatTransactDate;
    private int mRepeatTransactAmount;


    public RepeatTransactionImpl(){}

    public RepeatTransactionImpl(long id, long transID, String date, int amt){
        this.mRepeatID = id;
        this.mTransactionID = transID;
        this.mRepeatTransactDate = date;
        this.mRepeatTransactAmount = amt;
    }

    public RepeatTransactionImpl(long transID, String date, int amt){
        this.mTransactionID = transID;
        this.mRepeatTransactDate = date;
        this.mRepeatTransactAmount = amt;
    }


    @Override
    public long getID() {
        return mRepeatID;
    }
    @Override
    public void setID(long repeatTransactID) {
        this.mRepeatID = repeatTransactID;
    }

    @Override
    public long getTransactionID() {
        return mTransactionID;
    }
    @Override
    public void setTransactionID(long transactionID) {
        this.mTransactionID = transactionID;
    }

    @Override
    public String getRepeatTransactDate() {
        return mRepeatTransactDate;
    }
    @Override
    public void setRepeatTransactDate(String repeatTransactDate) {
        this.mRepeatTransactDate = repeatTransactDate;
    }

    @Override
    public int getRepeatTransactAmount() {
        return mRepeatTransactAmount;
    }
    @Override
    public void setRepeatTransactAmount(int amount) {
        this.mRepeatTransactAmount = amount;
    }

}
