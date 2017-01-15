package com.samirvora.myfinance.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.view.adapter.CategorySelectAdapter;

import java.util.List;


/**
 * Created by James on 08/10/2016.
 */
public class CategorySelectDialog extends CustomDialogFragment<Category>{

    private CategorySelectAdapter mCategoryLVAdapter;
    private List<Category> mCategoryImplData;

    private int mChosenPosition = -1;

    private View mCategoryListViewLayout;
    private ListView mCategoryListV;

    private CategorySelectListener mCatSelectListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mCategoryLVAdapter = new CategorySelectAdapter();
    }

    @Override
    public void setDialogData(List<Category> data) {
        mCategoryImplData = data;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCategoryListViewLayout = getActivity().getLayoutInflater().inflate(R.layout.listview_category, null);
        mCategoryListV = (ListView) mCategoryListViewLayout.findViewById(R.id.categoryListV);

        mCategoryListV.setAdapter(mCategoryLVAdapter);
        mCategoryListV.setOnItemClickListener(listViewItemClickListener);

        mCategoryLVAdapter.setData(mCategoryImplData);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setView(mCategoryListViewLayout);

        alertBuilder.setMessage(R.string.select_category_dialog_title);

        alertBuilder.setPositiveButton(R.string.select_category_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mChosenPosition != -1)
                    mCatSelectListener.onDialogPositiveClick(mCategoryLVAdapter.getItem(mChosenPosition));
            }
        });

        alertBuilder.setNegativeButton(R.string.select_category_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mCatSelectListener.onDialogNegativeCLick();
            }
        });

        return alertBuilder.create();
    }




    public interface CategorySelectListener {
        void onDialogPositiveClick(Category category);
        void onDialogNegativeCLick();
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CategorySelectListener so we can send events to the host
            mCatSelectListener = (CategorySelectListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()+ " must implement CategorySelectListener");
        }
    }

    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            for (int i = 0; i < parent.getChildCount(); i++) {
                ((RadioButton)(parent.getChildAt(i)).findViewById(R.id.categoryRadioButton)).setChecked(false);
            }

            mChosenPosition = position;

            RadioButton radioButton = (RadioButton) view.findViewById(R.id.categoryRadioButton);
            radioButton.setChecked(true);
        }
    };


}
