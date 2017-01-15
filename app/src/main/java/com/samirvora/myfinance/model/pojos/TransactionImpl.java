package com.samirvora.myfinance.model.pojos;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;

/**
 * Created by James on 21/08/2016.
 */
public class TransactionImpl implements Transaction {


    private long mID; // 0
    private String mTitle; // 1
    private int mAmount; // 2
    private long mCategoryID; // 3
    private String mDatePosted; // 4
    private String mType; // 5

    private boolean mIsRepeat = false;
    private String mRepeatEndDate;

    private int mCategoryImg;
    private String mCategoryName;



    public TransactionImpl(){}

    public TransactionImpl(long id, String title, int amount, long categoryID, String datePosted, String type){
        this.mID = id;
        this.mTitle = title;
        this.mAmount = amount;
        this.mCategoryID = categoryID;
        this.mDatePosted = datePosted;
        this.mType = type;
    }

    public TransactionImpl(String title, int amount, long categoryID, String datePosted, String type){
        this.mTitle = title;
        this.mAmount = amount;
        this.mCategoryID = categoryID;
        this.mDatePosted = datePosted;
        this.mType = type;
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
    public long getCategoryID() {
        return mCategoryID;
    }
    @Override
    public void setCategoryID(long categoryID) {
        mCategoryID = categoryID;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int getAmount() {
        return mAmount;
    }
    @Override
    public void setAmount(int amount) {
        mAmount = amount;
    }

    @Override
    public String getDatePosted() {
        return mDatePosted;
    }
    @Override
    public void setDatePosted(String datePosted) {
        this.mDatePosted = datePosted;
    }

    @Override
    public String getType() {
        return mType;
    }
    @Override
    public void setType(String type) {
        mType = type;
    }





    @Override
    public boolean isRepeat() {
        return mIsRepeat;
    }
    @Override
    public void setRepeat(boolean isRepeat) {
        this.mIsRepeat = isRepeat;
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
    public int getCategoryImg() {
        return mCategoryImg;
    }
    @Override
    public void setCategoryImg(int categoryImg) {
        this.mCategoryImg = categoryImg;
    }

    @Override
    public String getCategoryName() {
        return mCategoryName;
    }
    @Override
    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }


    @Override
    public String toString() {
        return "mID: "+getID()+" | mTitle: "+getTitle()+" | mAmount: "+getAmount()+" | mCategoryID: "+getCategoryID()+" | mDatePosted: "+getDatePosted()+" | mType: "+getType();
    }
}
