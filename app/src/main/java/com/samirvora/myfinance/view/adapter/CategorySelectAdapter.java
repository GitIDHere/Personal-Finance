package com.samirvora.myfinance.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by James on 08/10/2016.
 */
public class CategorySelectAdapter extends CustomBaseAdapter<Category> {

    private List<Category> mCategoryImplData;

    public CategorySelectAdapter() {
        mCategoryImplData = new ArrayList<>();
    }

    @Override
    public void setData(List<Category> categories){
        mCategoryImplData = categories;
        notifyDataSetChanged();
    }

    @Override
    public void clearList() {
        mCategoryImplData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCategoryImplData.size();
    }

    @Override
    public Category getItem(int position) {
        return mCategoryImplData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Category categoryImpl = getItem(position);

        MyViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
            viewHolder.mCategoryName = (TextView)convertView.findViewById(R.id.categoryName);
            viewHolder.mCategoryImg = (ImageView)convertView.findViewById(R.id.categoryImage);
            viewHolder.mRadioButton = (RadioButton)convertView.findViewById(R.id.categoryRadioButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.mCategoryName.setText(categoryImpl.getName());
        viewHolder.mCategoryImg.setImageResource(categoryImpl.getImg());

        return convertView;
    }

    private static class MyViewHolder{
        protected TextView mCategoryName;
        protected ImageView mCategoryImg;
        protected RadioButton mRadioButton;
    }

}
