package com.samirvora.myfinance.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.AddTransactionPresenter;
import com.samirvora.myfinance.presenter.presenters.AddTransactionPresenterImpl;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;
import com.samirvora.myfinance.view.activities.activity_interface.TransactionView;
import com.samirvora.myfinance.view.dialog.CategorySelectDialog;
import com.samirvora.myfinance.view.dialog.CategorySelectDialog.CategorySelectListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by James on 05/11/2016.
 */
public class AddTransactionActivity extends AppCompatActivity implements TransactionView,
        CategorySelectListener, OnCheckedChangeListener, OnDateSetListener, View.OnFocusChangeListener {

    private Context mCtx;

    //Should this be Category?
    private AddTransactionPresenter<Category> addTransactionPresenter;
    private CategorySelectDialog categorySelectDialog;

    private Category mChosenCategory;
    private String mTransactionType;
    private ArrayAdapter<CharSequence> repeatAmountAdapter;
    private ArrayAdapter<CharSequence> repeatPeriodAdapter;

    /** VIEWS **/
    private RadioButton mExpenseRB;
    private RadioButton mIncomeRB;

    private EditText mTitleET;
    private EditText mAmountET;

    //CategoryImpl
    private EditText mCategoryNameET;

    //Repeat
    private SwitchCompat mRepeatSwitch;
    private LinearLayout mRepeatSelectionParentV;
    private Spinner mRepeatAmt;
    private Spinner mRepeatPeriod;
    private Button mRepeatEndDate;

    //DatePicker
    private DatePickerDialog mDatePickerDialog;

    private Button mSubmitBTN;


    //TEMP
    private DateHelper mDateHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        mCtx = this;

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.addTransactionToolbar);
        setSupportActionBar(myChildToolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(myChildToolbar != null)
            myChildToolbar.setTitleTextColor(Color.WHITE);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        /** VIEWS **/

        //Type
        mExpenseRB = (RadioButton) findViewById(R.id.expenseRadio);
        mIncomeRB = (RadioButton) findViewById(R.id.incomeRadio);

        //Title and Amount
        mTitleET = (EditText) findViewById(R.id.transactionTitle);
        mAmountET = (EditText) findViewById(R.id.transaction_amount);

        //CategoryImpl
        mCategoryNameET = (EditText) findViewById(R.id.categoryName);

        //Repeat
        mRepeatSwitch = (SwitchCompat) findViewById(R.id.repeat_transaction_switch);
        mRepeatSelectionParentV = (LinearLayout) findViewById(R.id.repeatSelectionContainer);
        mRepeatAmt = (Spinner) findViewById(R.id.repeatAmt);
        mRepeatPeriod = (Spinner) findViewById(R.id.repeatPeriod);
        mRepeatEndDate = (Button) findViewById(R.id.repeatEndDate);

        //Submit button
        mSubmitBTN = (Button) findViewById(R.id.insertTransaction);

        /** VIEWS **/

        addTransactionPresenter = new AddTransactionPresenterImpl(this);

        categorySelectDialog = new CategorySelectDialog();

        mDateHelper = DateHelper.getInstance();
        mDatePickerDialog = new DatePickerDialog(this,
                this,
                mDateHelper.getCurrentYear(),
                mDateHelper.getCurrentMonth() -1,
                mDateHelper.getCurrentDay()
        );


        repeatAmountAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_amount, android.R.layout.simple_spinner_item);
        repeatAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatAmt.setAdapter(repeatAmountAdapter);

        repeatPeriodAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_period, android.R.layout.simple_spinner_item);
        repeatPeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatPeriod.setAdapter(repeatPeriodAdapter);


        /** LISTENERS **/
        mExpenseRB.setOnClickListener(transactionTypeSelectListener);
        mIncomeRB.setOnClickListener(transactionTypeSelectListener);

        mAmountET.setOnFocusChangeListener(this);

        mCategoryNameET.setOnClickListener(categorySelectListener);

        mRepeatSwitch.setOnCheckedChangeListener(this);

        mRepeatEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mSubmitBTN.setOnClickListener(onSubmitListener);
    }

    /** GETTERS **/

    @Override
    public String getTransactTitle() throws NullPointerException{
        checkIfNullTextView(mTitleET, "", "Title must not be empty");
        return mTitleET.getText().toString();
    }

    @Override
    public String getTransactAmount() throws NullPointerException{
        checkIfNullTextView(mAmountET, "", "Amount must not be empty");
        return CurrencyHelper.clearCurrencyFormat(mAmountET.getText().toString());
    }

    @Override
    public String getTransactionType() throws NullPointerException{
        if(mTransactionType == null){
            mIncomeRB.setError("Please select a transaction type");
            throw new NullPointerException();
        }
        return mTransactionType;
    }

    @Override
    public Category getCategory()throws NullPointerException {
        checkIfNullTextView(mCategoryNameET, "", "Please select a category");
        return mChosenCategory;
    }

    @Override
    public boolean getIsRepeatActive() {
        return mRepeatSwitch.isChecked();
    }

    @Override
    public String getRepeatAmount(){
        return mRepeatAmt.getSelectedItem().toString();
    }

    @Override
    public String getRepeatPeriod(){
        return mRepeatPeriod.getSelectedItem().toString();
    }

    @Override
    public String getRepeatEndDate()throws NullPointerException{
        checkIfNullTextView(mRepeatEndDate, "DD/MM/YYYY", "Please choose an end date");
        return  mRepeatEndDate.getText().toString();
    }


    private void checkIfNullTextView (TextView v, String ignoreCase, String errorMsg){
        if (v.getText().toString().trim().equalsIgnoreCase(ignoreCase)) {
            v.setError(errorMsg);
            throw new NullPointerException();
        }
    }

    /** GETTERS **/




    /** SETTERS **/

    public void setTransactionType(String transactionType){
        mIncomeRB.setError(null);//Remove error
        this.mTransactionType = transactionType;
    }

    @Override
    public void setCategoryImage(int imageID) {
        mCategoryNameET.setCompoundDrawablesWithIntrinsicBounds(imageID, 0, 0, 0);
    }

    @Override
    public void setCategoryName(String categoryName) {
        mCategoryNameET.setError(null);
        mCategoryNameET.setText(categoryName);
    }

    @Override
    public void setCategoryData(List<Category> categoryData) {
        categorySelectDialog.setDialogData(categoryData);
    }

    /** SETTERS **/




    /** HANDLERS **/

    @Override
    public void onDialogPositiveClick(Category category) {
        mChosenCategory = category;
        setCategoryName(mChosenCategory.getName());
        setCategoryImage(mChosenCategory.getImg());
    }

    @Override
    public void onDialogNegativeCLick() {}

    /** HANDLERS **/





    /** LISTENERS **/

    private View.OnClickListener transactionTypeSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RadioButton selectedRadioButton = (RadioButton)v;

            int transactTypeViewID = selectedRadioButton.getId();

            setTransactionType(selectedRadioButton.getText().toString());

            //Reset the displayed category image
            setCategoryName("");
            setCategoryImage(0);

            //Set the category select data list
            addTransactionPresenter.setCategoryData(transactTypeViewID);
        }
    };

    private View.OnClickListener categorySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearEditTextFocus();
            hideSoftKeyboard((Activity)mCtx);

            if(mTransactionType == null){
                mIncomeRB.setError("Please select a transaction type");
            }else{
                categorySelectDialog.show(getSupportFragmentManager(), "CategorySelectDialog");
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mRepeatSelectionParentV.setVisibility(View.VISIBLE);
        }else{
            mRepeatSelectionParentV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        mDatePickerDialog.updateDate(year, monthOfYear, dayOfMonth);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

        SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.VIEW_DATE_FORMAT, Locale.UK);
        mRepeatEndDate.setText(sdf.format(calendar.getTime()));
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText amountET = (EditText)v;
        if(!hasFocus){
            String userAmount = amountET.getText().toString();

            switch(userAmount){
                case "£":
                case "":
                    amountET.setText("");
                    break;
                default:
                    amountET.setText(CurrencyHelper.formatStringToCurrencyString(userAmount));
            }
        }
    }



    //Submit button
    private View.OnClickListener onSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearEditTextFocus();
            hideSoftKeyboard((Activity) mCtx);
            boolean result = addTransactionPresenter.submitTransaction();
            if(result){
                Intent intent = new Intent(mCtx, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }
    };
    /** LISTENERS **/


    private void clearEditTextFocus(){
        mAmountET.clearFocus();
        mTitleET.clearFocus();
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
