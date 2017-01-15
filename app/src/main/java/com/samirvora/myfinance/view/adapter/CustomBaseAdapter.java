package com.samirvora.myfinance.view.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by James on 05/11/2016.
 */
public abstract class CustomBaseAdapter<T> extends BaseAdapter{

    abstract void setData(List<T> dataList);
    abstract void clearList();

}
