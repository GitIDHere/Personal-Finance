package com.samirvora.myfinance.presenter.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.TransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private final int MAX_TYPE_COUNT = 2;
    private final int TYPE_TRANSACTION = 0;
    private final int TYPE_SEPARATOR = 1;

    //NO OBJECT
    private List<Object> mData = new ArrayList<>();
    private SimpleDateFormat mViewSDF;
    private SimpleDateFormat mDbSDF;

    public TransactionListViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mViewSDF = new SimpleDateFormat(DateHelper.VIEW_DATE_FORMAT, Locale.ENGLISH);
        mDbSDF = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.ENGLISH);
    }

    public void clearList(){
        mData.clear();
        notifyDataSetChanged();
    }


    public void setData(List<Object> list){
        if(list == null)
            return;

        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if(mData.get(position) instanceof TransactionImpl){
            return TYPE_TRANSACTION;
        }

        return TYPE_SEPARATOR;
    }

    @Override
    public int getViewTypeCount() {
        return MAX_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        int rowType = getItemViewType(position);

        if(row == null){//init the view

            holder = new ViewHolder();

            switch (rowType) {
                case TYPE_TRANSACTION:
                    row = inflater.inflate(R.layout.listview_generic, null);

                    holder.title = (TextView)row.findViewById(R.id.listTransactTitle);
                    holder.amount = (TextView)row.findViewById(R.id.listTransactAmount);

                    holder.categoryImg = (ImageView)row.findViewById(R.id.categoryIcon);
                    holder.categoryName = (TextView)row.findViewById(R.id.categoryName);

                    holder.endDateLabel = (TextView) row.findViewById(R.id.repeatEndDateLabel);
                    holder.endDate = (TextView) row.findViewById(R.id.repeatEndDate);
                    holder.repeatIcon = (ImageView) row.findViewById(R.id.repeatIcon);

                    break;
                case TYPE_SEPARATOR:
                    row = inflater.inflate(R.layout.listview_header, null);
                    holder.datePosted = (TextView)row.findViewById(R.id.textSeparator);
            }

            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        switch (rowType) {
            case TYPE_TRANSACTION:
                Transaction transaction = (Transaction) mData.get(position);

                /** Design for specific type of transaction **/
                if(transaction.getType().equals(TransactionEnum.TYPE_EXPENSE)){
                    holder.categoryImg.setBackgroundResource(R.drawable.shape_list_category_expense_background);
                }else{
                    holder.categoryImg.setBackgroundResource(R.drawable.shape_list_category_income_background);
                }

                holder.title.setText(transaction.getTitle());
                holder.amount.setText(CurrencyHelper.convertIntToCurrencyString(transaction.getAmount()));

                holder.categoryImg.setColorFilter(ContextCompat.getColor(parent.getContext(), R.color.white));
                holder.categoryImg.setImageResource(transaction.getCategoryImg());
                holder.categoryName.setText(transaction.getCategoryName());

                if(transaction.isRepeat()) {
                    holder.repeatIcon.setImageResource(R.drawable.icon_repeat);

                    try{
                        holder.endDateLabel.setText(R.string.listview_repeat_end);
                        Date originalDate = mDbSDF.parse(transaction.getRepeatEndDate());
                        holder.endDate.setText(mViewSDF.format(originalDate));
                    }catch (ParseException ignored){}

                }else{
                    holder.repeatIcon.setImageResource(android.R.color.transparent);
                    holder.endDateLabel.setText("");
                    holder.endDate.setText("");
                }

                break;
            case TYPE_SEPARATOR:
                holder.datePosted.setText((String)mData.get(position));
        }

        return row;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView amount;
        public TextView datePosted;
        public TextView endDateLabel;
        public TextView endDate;

        public ImageView categoryImg;
        public TextView categoryName;

        public ImageView repeatIcon;
    }


}
