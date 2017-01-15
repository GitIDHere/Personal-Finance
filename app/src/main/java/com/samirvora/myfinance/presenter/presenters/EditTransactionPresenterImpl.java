package com.samirvora.myfinance.presenter.presenters;


import com.samirvora.myfinance.model.database.DBHelper;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.RepeatDaysImpl;
import com.samirvora.myfinance.model.pojos.RepeatTransactionImpl;
import com.samirvora.myfinance.model.pojos.TransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.model.services.TransactionServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.CategoryService;
import com.samirvora.myfinance.model.services.CategoryServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.RepeatTransactionService;
import com.samirvora.myfinance.model.services.RepeatTransactionServiceImpl;
import com.samirvora.myfinance.model.services.service_interface.TransactionService;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.EditTransactionPresenter;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;
import com.samirvora.myfinance.view.activities.activity_interface.EditTransactionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James on 12/11/2016.
 */
public class EditTransactionPresenterImpl implements EditTransactionPresenter {

    private EditTransactionView editTransactionView;

    private TransactionService mTransactionService;
    private RepeatTransactionService mRepeatTransactionService;
    private CategoryService<Category> mCategoryService;

    private long mTransactionID;
    private long mRepeatDaysID;
    private String mRepeatStartDate;
    private String mOriginalCategoryName;

    private DatabaseCrud mDBHelper;
    private DateHelper mDateHelper;

    private boolean mIsRepeatOriginalActive;

    public EditTransactionPresenterImpl(EditTransactionView editTransactionView, long transactionID, String categoryName, boolean isRepeatActive){
        this.mTransactionID = transactionID;
        this.mOriginalCategoryName = categoryName;

        this.editTransactionView = editTransactionView;

        this.mDBHelper = DBHelper.getInstance();
        this.mDateHelper = DateHelper.getInstance();

        this.mTransactionService = new TransactionServiceImpl(mDBHelper);
        this.mRepeatTransactionService = new RepeatTransactionServiceImpl(mDBHelper);
        this.mCategoryService = new CategoryServiceImpl(mDBHelper);

        mIsRepeatOriginalActive = isRepeatActive;

        if(mIsRepeatOriginalActive){
            //Get the repeat data
            RepeatDays repeatDays = mRepeatTransactionService.getRepeatDaysByTransID(mTransactionID);
            mRepeatDaysID = repeatDays.getID();
            mRepeatStartDate = repeatDays.getRepeatStartDate();
            editTransactionView.setRepeatAmount(Integer.toString(repeatDays.getRepeatAmount()));
            editTransactionView.setRepeatPeriod(repeatDays.getRepeatPeriod());
        }

    }


    @Override
    public void setCategoryData(String transactionType){
        switch (transactionType){
            case TransactionEnum.TYPE_EXPENSE:
                editTransactionView.setCategoryData(mCategoryService.getExpenseCategoryData());
                break;
            case TransactionEnum.TYPE_INCOME:
                editTransactionView.setCategoryData(mCategoryService.getIncomeCategoryData());
                break;
        }
    }



    @Override
    public boolean submitEditTransaction() {


        /** INSERT CATEGORY IF NEW **/
        Category category = editTransactionView.getCategory();
        if(!category.getName().equals(mOriginalCategoryName)){//A new category was selected
            long categoryID = mCategoryService.insertCategory(category);
            category.setID(categoryID);
        }
        /** INSERT CATEGORY IF NEW **/



        /** UPDATE TRANSACTION **/
        Transaction transaction = new TransactionImpl();

        String title;
        String amount;

        try{
            title = editTransactionView.getTransactTitle();
            amount = editTransactionView.getTransactAmount();
        }catch (NullPointerException e){
            return false;
        }

        transaction.setID(mTransactionID);
        transaction.setTitle(title);
        transaction.setAmount(CurrencyHelper.covertStringToInt(amount));
        transaction.setCategoryID(category.getID());
        transaction.setDatePosted(editTransactionView.getDatePosted());
        transaction.setType(editTransactionView.getTransactionType());

        /** UPDATE TRANSACTION **/





        /** UPDATE REPEAT DAYS + TRANSACTION **/
        RepeatDays repeatDays = null;

        if(editTransactionView.getIsRepeatActive()){ //Repeat is active

            repeatDays = new RepeatDaysImpl();

            //Check if the user has started a new repeat or edited an old one

            String repeatPeriod = editTransactionView.getRepeatPeriod();
            int repeatAmount = Integer.parseInt(editTransactionView.getRepeatAmount());

            String endDate;
            try{
                endDate = editTransactionView.getRepeatEndDate();
            }catch (NullPointerException e){
                return false;
            }

            repeatDays.setRepeatPeriod(repeatPeriod);
            repeatDays.setRepeatAmount(repeatAmount);
            repeatDays.setTransactionID(mTransactionID);

            double days;
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
            repeatDays.setRepeatDays((int)Math.round(days));

            try{
                SimpleDateFormat originalFormat = new SimpleDateFormat(DateHelper.VIEW_DATE_FORMAT, Locale.ENGLISH);
                Date originalDate = originalFormat.parse(endDate);
                SimpleDateFormat requiredFormat = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.ENGLISH);
                String dbFormattedEndDate = requiredFormat.format(originalDate);
                repeatDays.setRepeatEndDate(dbFormattedEndDate);
            }catch (ParseException e){
                return false;
            }

            if(mRepeatDaysID == 0){ //Insert new repeat
                repeatDays.setRepeatStartDate(mDateHelper.getTodaysDateAsString());

            }else{ //Edited an old one
                repeatDays.setID(mRepeatDaysID);
                repeatDays.setRepeatStartDate(mRepeatStartDate);
            }

        }
        /** UPDATE REPEAT DAYS + TRANSACTION **/




        /** INSERTION **/

        int transactionRowsUpdated = mTransactionService.updateTransaction(transaction);
        if(transactionRowsUpdated == 0){
            System.out.println("failed: transactionRowsUpdated");
            return false;
        }

        if(repeatDays != null){ /// Is repeat active?

            if(mRepeatDaysID == 0){//Insert new repeat days row

                /** INSERT REPEAT DAYS **/
                long repeatDaysRow = mRepeatTransactionService.insertRepeatDaysRow(repeatDays);

                if(repeatDaysRow == 0){
                    System.out.println("Inserting repeat days fail");
                    return false;
                }
                /** INSERT REPEAT DAYS **/

                /** INSERT REPEAT TRANSACTION **/
                long repeatTransactionRow = mRepeatTransactionService.insertRepeatTransactionRow(
                        new RepeatTransactionImpl(
                                repeatDays.getTransactionID(),
                                repeatDays.getRepeatStartDate(),
                                transaction.getAmount()
                        )
                );

                if(repeatTransactionRow == 0){
                    System.out.println("Inserting repeat transaction insert failed");
                    return false;
                }
                /** INSERT REPEAT TRANSACTION **/


            }else{//update repeat days row

                int repeatRowsUpdated = mRepeatTransactionService.updateRepeatDays(repeatDays);
                if(repeatRowsUpdated == 0){
                    System.out.println("repeatRowsUpdated fail");
                    return false;
                }

            }

        }

        /** DELETE REPEAT TRANSACTION **/
        if(mIsRepeatOriginalActive && !editTransactionView.getIsRepeatActive()){
            boolean isRepeatDaysDeleted = mRepeatTransactionService.removeRepeatDays(mRepeatDaysID);
            if(!isRepeatDaysDeleted){
                return false;
            }
        }
        /** DELETE REPEAT TRANSACTION **/

        return true;
    }


    public boolean deleteTransaction(){
        mTransactionService.deleteTransaction(mTransactionID);
        mRepeatTransactionService.deleteRepeatDays(mRepeatDaysID);
        mRepeatTransactionService.deleteRepeatTransactionByTransID(mTransactionID);
        return true;
    }


}
