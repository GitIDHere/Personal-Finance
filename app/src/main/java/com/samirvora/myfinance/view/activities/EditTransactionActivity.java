package com.samirvora.myfinance.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.CategoryImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.presenter.presenters.EditTransactionPresenterImpl;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.EditTransactionPresenter;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;
import com.samirvora.myfinance.view.activities.activity_interface.EditTransactionView;
import com.samirvora.myfinance.view.dialog.CategorySelectDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by James on 12/11/2016.
 */
public class EditTransactionActivity extends AppCompatActivity implements EditTransactionView,
        CategorySelectDialog.CategorySelectListener, DatePickerDialog.OnDateSetListener,
        View.OnFocusChangeListener{

    public static final String TRANS_ID = "transaction_ID"; // 1
    public static final String TRANS_TYPE = "transaction_type"; // 2
    public static final String TRANS_TITLE = "transaction_title"; // 3
    public static final String TRANS_AMOUNT = "transaction_amount"; // 4
    public static final String TRANS_CATEGORY_ID = "category_id"; // 5
    public static final String TRANS_DATE_POSTED = "date_posted"; // 6
    public static final String TRANS_CATEGORY_IMG = "transaction_category_img"; // 7
    public static final String TRANS_CATEGORY_NAME = "transaction_category_name"; // 8
    public static final String TRANS_IS_REPEAT = "transaction_is_repeat"; // 9
    public static final String TRANS_REPEAT_END_DATE = "transaction_end_date"; // 10

    private Context mCtx;
    private Toolbar mToolbar;
    private Category mChosenCategory;
    private CategorySelectDialog categorySelectDialog;
    private EditTransactionPresenter mEditTransactionPresenter;

    private DateHelper mDateHelper;

    private String mDatePosted;
    private String mTransactionType;



    private ArrayAdapter<CharSequence> repeatAmountAdapter;
    private ArrayAdapter<CharSequence> repeatPeriodAdapter;

    /** VIEWS **/
    private RadioGroup mTransactionTypeRG;
    private EditText mTitleET;

    private EditText mAmountET;

    private EditText mCategoryNameET;

    //Repeat
    private SwitchCompat mRepeatSwitch;
    private LinearLayout mRepeatSelectionParentV;
    private Spinner mRepeatAmt;
    private Spinner mRepeatPeriod;
    private TextView mRepeatEndDate;

    //DatePicker
    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;
    SimpleDateFormat mSimpleDateFormat;

    private Button mSubmitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        mToolbar = (Toolbar) findViewById(R.id.editTransactionToolbar);
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mToolbar != null)
            mToolbar.setTitleTextColor(Color.WHITE);



        this.mCtx = this;
        this.mDateHelper = DateHelper.getInstance();
        this.mCalendar = Calendar.getInstance();
        this.mSimpleDateFormat = new SimpleDateFormat(DateHelper.VIEW_DATE_FORMAT, Locale.UK);

        /** Set the views **/
        this.mTransactionTypeRG = (RadioGroup) findViewById(R.id.transactionRadioGroup);
        this.mTitleET = (EditText) findViewById(R.id.transactionTitle);

        this.mAmountET = (EditText) findViewById(R.id.transaction_amount);

        this.mCategoryNameET = (EditText) findViewById(R.id.categoryName);

        this.mRepeatSwitch = (SwitchCompat) findViewById(R.id.repeat_transaction_switch);
        this.mRepeatSelectionParentV = (LinearLayout) findViewById(R.id.repeatSelectionContainer);
        this.mRepeatAmt = (Spinner) findViewById(R.id.repeatAmt);
        this.mRepeatPeriod = (Spinner) findViewById(R.id.repeatPeriod);
        this.mRepeatEndDate = (TextView) findViewById(R.id.repeatEndDate);

        this.mSubmitButton = (Button) findViewById(R.id.saveEditButton);
        /** Set the views **/


        Intent intent = getIntent();
        String transactionType = intent.getStringExtra(TRANS_TYPE);
        int categoryImg = intent.getIntExtra(TRANS_CATEGORY_IMG, R.drawable.shape_category_empty);
        String categoryName = intent.getStringExtra(TRANS_CATEGORY_NAME);
        String repeatEndDate = intent.getStringExtra(TRANS_REPEAT_END_DATE);

        setTransactionType(transactionType);
        setTitle(intent.getStringExtra(TRANS_TITLE));
        setAmount(intent.getStringExtra(TRANS_AMOUNT));
        setDatePosted(intent.getStringExtra(TRANS_DATE_POSTED));
        setCategoryImage(categoryImg);
        setCategoryName(categoryName);
        setRepeatCheckBox(intent.getBooleanExtra(TRANS_IS_REPEAT, false));

        mChosenCategory = new CategoryImpl(intent.getLongExtra(TRANS_CATEGORY_ID, 0), categoryName, categoryImg);

        //Disable the Transaction Type radio buttons
        disableChildRadioButtons(mTransactionTypeRG);


        /** Repeat Amount and Period value adapters **/
        repeatAmountAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_amount, android.R.layout.simple_spinner_item);
        repeatAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatAmt.setAdapter(repeatAmountAdapter);

        repeatPeriodAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_period, android.R.layout.simple_spinner_item);
        repeatPeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatPeriod.setAdapter(repeatPeriodAdapter);

        this.mEditTransactionPresenter = new EditTransactionPresenterImpl(this, intent.getLongExtra(TRANS_ID, 0), categoryName, getIsRepeatActive());

        if(repeatEndDate != null){

            SimpleDateFormat originalFormat = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.ENGLISH);

            try{
                Date originalDate = originalFormat.parse(repeatEndDate);
                String endDateFormatted = mSimpleDateFormat.format(originalDate);

                setRepeatEndDate(endDateFormatted);

                Date endDate = mSimpleDateFormat.parse(endDateFormatted);
                mCalendar.setTime(endDate);

            }catch (ParseException e){
                e.printStackTrace();
            }

        }

        mDatePickerDialog = new DatePickerDialog(this,
                this,
                mDateHelper.getCurrentYear(),
                mDateHelper.getCurrentMonth() - 1,
                mDateHelper.getCurrentDay()
        );

        categorySelectDialog = new CategorySelectDialog();
        mEditTransactionPresenter.setCategoryData(transactionType);


        /** Listeners **/

        mAmountET.setOnFocusChangeListener(this);

        this.mCategoryNameET.setOnClickListener(categorySelectListener);

        mRepeatSwitch.setOnCheckedChangeListener(toggleRepeatViewListener);

        mRepeatEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        this.mSubmitButton.setOnClickListener(onSubmitListener);
        /** Listeners **/
    }


    
    /** SETTERS **/

    @Override
    public void setTitle(String title){
        this.mTitleET.setText(title);
    }

    @Override
    public void setAmount(String amount){
        String formattedAmt = CurrencyHelper.formatStringToCurrencyString(amount);
        this.mAmountET.setText(formattedAmt);
    }

    @Override
    public void setDatePosted(String datePosted) {
        this.mDatePosted = datePosted;
    }

    @Override
    public void setCategoryData(List<Category> categoryData) {
        categorySelectDialog.setDialogData(categoryData);
    }

    @Override
    public void setCategoryImage(int img){
        //Sets the image on the left side of the Edit Text
        mCategoryNameET.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
    }

    @Override
    public void setCategoryName(String name){
        this.mCategoryNameET.setText(name);
    }

    @Override
    public void setTransactionType(String transactionType) {

        this.mTransactionType = transactionType;

        if(transactionType.equals(TransactionEnum.TYPE_EXPENSE)){
            this.mTransactionTypeRG.check(R.id.expenseRadio);
        }else{
            this.mTransactionTypeRG.check(R.id.incomeRadio);
        }
    }

    @Override
    public void setRepeatCheckBox(boolean isRepeat){
        this.mRepeatSwitch.setChecked(isRepeat);
        setRepeatViewVisibility(isRepeat);
    }

    @Override
    public void setRepeatAmount(String amount){
        int position = repeatAmountAdapter.getPosition(amount);
        this.mRepeatAmt.setSelection(position);
    }

    @Override
    public void setRepeatPeriod(String period){
        int position = repeatPeriodAdapter.getPosition(period);
        this.mRepeatPeriod.setSelection(position);
    }

    @Override
    public void setRepeatEndDate(String endDate){
        this.mRepeatEndDate.setText(endDate);
    }


    private void setRepeatViewVisibility(boolean isVisible){
        if(isVisible){
            mRepeatSelectionParentV.setVisibility(View.VISIBLE);
        }else{
            mRepeatSelectionParentV.setVisibility(View.GONE);
        }
    }

    private void disableChildRadioButtons(RadioGroup radioGroup){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setClickable(false);
        }
    }
    /** SETTERS **/





    /** GETTER **/
    @Override
    public String getTransactTitle() throws NullPointerException{
        checkIfNullTextView(mTitleET, "", "Please insert a title");
        return this.mTitleET.getText().toString();
    }

    @Override
    public String getTransactAmount() throws NullPointerException{
        checkIfNullTextView(mAmountET, "", "Please insert an amount");
        return this.mAmountET.getText().toString();
    }

    @Override
    public String getTransactionType(){
        return this.mTransactionType;
    }

    @Override
    public Category getCategory(){
        return mChosenCategory;
    }

    @Override
    public String getDatePosted(){
        return this.mDatePosted;
    }

    /** REPEAT **/

    @Override
    public boolean getIsRepeatActive() {
        return mRepeatSwitch.isChecked();
    }

    public String getRepeatAmount(){
        return mRepeatAmt.getSelectedItem().toString();
    }

    public String getRepeatPeriod(){
        return mRepeatPeriod.getSelectedItem().toString();
    }

    public String getRepeatEndDate() throws NullPointerException{
        checkIfNullTextView(mRepeatEndDate, "DD/MM/YYYY", "Please choose an end date");
        return mRepeatEndDate.getText().toString();
    }


    private void checkIfNullTextView (TextView v, String ignoreCase, String errorMsg){
        if (v.getText().toString().trim().equalsIgnoreCase(ignoreCase)) {
            v.setError(errorMsg);
            throw new NullPointerException();
        }
    }
    /** GETTER **/











    /** LISTENERS **/

    private View.OnClickListener onSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearEditTextFocus();

            boolean successful = mEditTransactionPresenter.submitEditTransaction();
            if(successful){
                Intent intent = new Intent(mCtx, MainActivity.class);
                finish();
                startActivity(intent);
            }else{
                //show error
                System.out.println("ERROR EDITING TRANSACTION");
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener toggleRepeatViewListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setRepeatViewVisibility(isChecked);
        }
    };

    private View.OnClickListener categorySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearEditTextFocus();
            hideSoftKeyboard((Activity) mCtx);
            categorySelectDialog.show(getSupportFragmentManager(), "CategorySelectDialog");
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        mDatePickerDialog.updateDate(year, monthOfYear, dayOfMonth);

        mCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

        mRepeatEndDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
    }

    @Override
    public void onDialogPositiveClick(Category category) {
        mChosenCategory = category;
        setCategoryImage(mChosenCategory.getImg());
        setCategoryName(mChosenCategory.getName());
    }

    @Override
    public void onDialogNegativeCLick() {}

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

    /** LISTENERS **/



    /****** ACTION BAR ******/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_transaction_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteTransaction:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.deleteAlertTitle)
                    .setMessage(R.string.deleteAlertMsg);

                builder.setPositiveButton(R.string.deleteAlertPos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(mEditTransactionPresenter.deleteTransaction()){
                            Intent intent = new Intent(mCtx, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton(R.string.deleteAlertNeg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });

                builder.show();

                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /****** ACTION BAR ******/


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

}
