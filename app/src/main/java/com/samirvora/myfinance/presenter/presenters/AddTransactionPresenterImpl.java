package com.samirvora.myfinance.presenter.presenters;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.daos.TotalTransactionDaoImpl;
import com.samirvora.myfinance.model.daos.dao_interfaces.TotalTransactionDao;
import com.samirvora.myfinance.model.database.DBHelper;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.pojos.RepeatDaysImpl;
import com.samirvora.myfinance.model.pojos.RepeatTransactionImpl;
import com.samirvora.myfinance.model.pojos.TotalTransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.TotalTransaction;
import com.samirvora.myfinance.model.services.TransactionServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.CategoryService;
import com.samirvora.myfinance.model.services.CategoryServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.RepeatTransactionService;
import com.samirvora.myfinance.model.services.RepeatTransactionServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.TransactionService;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.AddTransactionPresenter;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;
import com.samirvora.myfinance.view.activities.activity_interface.TransactionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 05/11/2016.
 */
public class AddTransactionPresenterImpl implements AddTransactionPresenter<Category>{

    private TransactionView mTransactionView;

    private TransactionService mTransactionService;
    private TotalTransactionDao<TotalTransaction> mTotalTransactionDao;
    private RepeatTransactionService mRepeatTransactionService;
    private CategoryService<Category> mCategoryService;

    //Temp
    private DateHelper mDateHelper;

    private DatabaseCrud mDBHelper;

    public AddTransactionPresenterImpl(TransactionView transactionView){
        this.mTransactionView = transactionView;

        mDBHelper = DBHelper.getInstance();

        this.mTransactionService = new TransactionServiceImpl(mDBHelper);
        this.mTotalTransactionDao = new TotalTransactionDaoImpl(mDBHelper);
        this.mRepeatTransactionService = new RepeatTransactionServiceImpl(mDBHelper);
        this.mCategoryService = new CategoryServiceImpl(mDBHelper);

        //Temp
        this.mDateHelper = DateHelper.getInstance();
    }


    @Override
    public void setCategoryData(int vID) {
        switch (vID){
        case R.id.expenseRadio:
            mTransactionView.setCategoryData(mCategoryService.getExpenseCategoryData());
            break;
        case R.id.incomeRadio:
            mTransactionView.setCategoryData(mCategoryService.getIncomeCategoryData());
            break;
        default:
            mTransactionView.setCategoryData(mCategoryService.getExpenseCategoryData());
        }
    }

    /*
    type
    Title
    amount
    category
     */

    @Override
    public boolean submitTransaction(){

        /** TRANSACTION **/
        String title;
        String transactionType;
        int transactionAmount;
        Category category;

        try{
            transactionType = mTransactionView.getTransactionType();
            title = mTransactionView.getTransactTitle();
            transactionAmount = CurrencyHelper.covertStringToInt(mTransactionView.getTransactAmount());
            category = mTransactionView.getCategory();
        }catch (NullPointerException e){
            return false;
        }
        /** TRANSACTION **/


        /** INSERT CATEGORY **/
        long categoryID = mCategoryService.insertCategory(category);
        /** INSERT CATEGORY **/


        /** INSERT / UPDATE TOTAL TRANSACTION **/

        /** TOTAL TRANSACTION **/
        int month = mDateHelper.getCurrentMonth();
        int year = mDateHelper.getCurrentYear();

        int currentTotalTransaction = mTotalTransactionDao.getTotalTransForMonth(month, year, transactionType);

        TotalTransactionImpl totalTransaction = new TotalTransactionImpl();
        totalTransaction.setMonth(month);// Current month
        totalTransaction.setYear(year);// Current Year
        totalTransaction.setType(transactionType);
        /** TOTAL TRANSACTION **/



        /** INSERT REPEAT TRANSACTION **/
        RepeatDays repeatDays = null;

        if(mTransactionView.getIsRepeatActive()){

            repeatDays = new RepeatDaysImpl();

            SimpleDateFormat dbSimpleDateFormat = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.UK);

            int repeatAmount = Integer.parseInt(mTransactionView.getRepeatAmount());
            String repeatPeriod = mTransactionView.getRepeatPeriod();
            double days;

            /** String repeatStartDate = dbSimpleDateFormat.format(calendar.getTime()); **/
            String repeatStartDate = mDateHelper.getTodaysDateAsString();

            String repeatEndDate;
            try{
                repeatEndDate = mTransactionView.getRepeatEndDate();
            }catch (NullPointerException e){
                return false;
            }

            switch(repeatPeriod){
                case "Week(s)":
                    days = repeatAmount * DateHelper.DAYS_IN_WEEK;
                    break;
                case "Month(s)":
                    days = repeatAmount * DateHelper.DAYS_IN_MONTH;
                    break;
                case "Year(s)":
                    days = repeatAmount * DateHelper.DAYS_IN_YEAR;
                    break;
                default:
                    days = repeatAmount;
            }

            /** Format the end date to the DB format **/
            try {
                SimpleDateFormat viewSimpleDateFormat = new SimpleDateFormat(DateHelper.VIEW_DATE_FORMAT, Locale.UK);
                Date viewDateFormat = viewSimpleDateFormat.parse(repeatEndDate);
                repeatEndDate = dbSimpleDateFormat.format(viewDateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

            repeatDays.setRepeatPeriod(repeatPeriod);
            repeatDays.setRepeatAmount(repeatAmount);
            repeatDays.setRepeatStartDate(repeatStartDate);
            repeatDays.setRepeatEndDate(repeatEndDate);
            repeatDays.setRepeatDays((int)Math.round(days));
        }





        /** INSERTION **/

            /** INSERT TRANSACTION **/
            long transactionID = mTransactionService.createTransaction(title, transactionType, transactionAmount, categoryID);
            if(transactionID == -1){
                return false;
            }


            /** INSERT TOTAL TRANSACTION **/
            if(currentTotalTransaction == 0){//Create new total transaction
                totalTransaction.setTotal(transactionAmount);
                if(mTotalTransactionDao.createRow(totalTransaction) == -1){
                    return false;
                }
            }else{//update current total transaction
                int newTotal = currentTotalTransaction + transactionAmount;
                totalTransaction.setTotal(newTotal);
                if(mTotalTransactionDao.updateRow(totalTransaction) == 0){
                    return false;
                }
            }
            /** INSERT TOTAL TRANSACTION **/



            if(repeatDays != null){

                /** INSERT REPEAT DAYS **/
                repeatDays.setTransactionID(transactionID);
                long repeatDaysResult = mRepeatTransactionService.insertRepeatDaysRow(repeatDays);
                if(repeatDaysResult == -1){
                    return false;
                }
                /** INSERT REPEAT DAYS **/

                /** INSERT REPEAT TRANSACTION **/
                long repeatTransactionResult = mRepeatTransactionService.insertRepeatTransactionRow(new RepeatTransactionImpl(
                        transactionID,
                        repeatDays.getRepeatStartDate(),
                        transactionAmount
                ));

                if(repeatTransactionResult == -1){
                    return false;
                }
                /** INSERT REPEAT TRANSACTION **/
            }



        return true;
    }

}
