package com.samirvora.myfinance.model.pojos;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;

/*
 * Created by James on 08/10/2016.
 */
public class CategoryImpl implements Category {

    private long mID; // 0
    private int mImg; // 1
    private String mName; // 2


    public CategoryImpl(long id, String name, int img) {
        this.mID = id;
        this.mName = name;
        this.mImg = img;
    }

    public CategoryImpl(String name, int img) {
        this.mName = name;
        this.mImg = img;
    }

    public CategoryImpl() {
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
    public int getImg() {
        return mImg;
    }

    @Override
    public void setImg(int img) {
        this.mImg = img;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        this.mName = name;
    }
}