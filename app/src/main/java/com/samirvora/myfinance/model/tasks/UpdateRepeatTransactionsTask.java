package com.samirvora.myfinance.model.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import com.samirvora.myfinance.model.daos.RepeatTransactionDaoImpl;
import com.samirvora.myfinance.model.daos.TotalTransactionDaoImpl;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.RepeatDaysEnum;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.RepeatTransactionImpl;
import com.samirvora.myfinance.model.pojos.TotalTransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatTransaction;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.TotalTransaction;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.util.DateHelper;
import com.samirvora.myfinance.util.ThisApplication;

import java.util.Calendar;



/**
 * Created by James on 17/09/2016.
 */
public class UpdateRepeatTransactionsTask extends AsyncTask<Void, String, Void> {

    private final String LAST_UPDATE_SPREF_KEY = "LAST_UPDATE";

    private DateHelper mDateHelper;
    private DatabaseCrud mDBHelper;

    private OnTransactionListResponse<Transaction> responseCaller = null;
    private String mDateMaxRange;
    private String mDateMinRange;

    /** DAO **/
    private RepeatTransactionDaoImpl mRepeatTransactionDaoImpl;
    private TotalTransactionDaoImpl mTotalTransactionDaoImpl;


    public UpdateRepeatTransactionsTask(DatabaseCrud dbHelper, OnTransactionListResponse<Transaction> responseCaller, String minDate, String maxDate){
        super();

        mDBHelper = dbHelper;

        this.responseCaller = responseCaller;
        this.mDateMaxRange = maxDate;
        this.mDateMinRange = minDate;

        //Temp
        mDateHelper = DateHelper.getInstance();

        mRepeatTransactionDaoImpl = new RepeatTransactionDaoImpl(mDBHelper);
        mTotalTransactionDaoImpl = new TotalTransactionDaoImpl(mDBHelper);
    }

    @Override
    protected Void doInBackground(Void... params) {

        String todaysDate = mDateHelper.getTodaysDateAsString();

        String lastUpdateDate = getLastUpdateDate(LAST_UPDATE_SPREF_KEY);

        if(lastUpdateDate != null){

            //Check if last update date matches today's date
            if(!lastUpdateDate.equals(todaysDate)){
                int daysTillLastUpdate = mDateHelper.getDaysBetweenDates(lastUpdateDate, todaysDate);
                Calendar cal = mDateHelper.getCalendarFromDate(lastUpdateDate);
                checkRepeatTransactions(daysTillLastUpdate, cal);
            }

        }

        //update the last app update date
        setLastUpdateDate(LAST_UPDATE_SPREF_KEY, todaysDate);

        return null;
    }


    private void checkRepeatTransactions(int days, Calendar currentDateCal){

        Cursor resultCursor = getRepeatTransactions();

        if(resultCursor != null){

            for (int i = 1; i <= days; i++) {

                currentDateCal.add(Calendar.DAY_OF_MONTH, 1);
                String currentDate = mDateHelper.getDateFromCalendar(currentDateCal);

                do{
                    long transactionID = resultCursor.getLong(0);
                    int transactionAmount = resultCursor.getInt(1);
                    String transactionType = resultCursor.getString(2);

                    int repeatDays = resultCursor.getInt(3); //Repeat Days
                    String repeatStartDate = resultCursor.getString(4); //Repeat Days
                    String repeatEndDate = resultCursor.getString(5); //Repeat Days

                    String repeatPostDate;
                    int month;
                    int year;

                    if(mDateHelper.compareStringDates(currentDate, repeatEndDate) <= 0){

                        int dayRange = mDateHelper.getDaysBetweenDates(repeatStartDate, currentDate); //0

                        month = currentDateCal.get(Calendar.MONTH) + 1;
                        year = currentDateCal.get(Calendar.YEAR);
                        repeatPostDate = currentDate;

                        if (dayRange % repeatDays == 0){
                            System.out.println("ACTUAL REPEAT");

                            RepeatTransaction repeatTransaction = new RepeatTransactionImpl(
                                    transactionID,
                                    repeatPostDate,
                                    transactionAmount
                                    );
                            mRepeatTransactionDaoImpl.createRow(repeatTransaction);

                            System.out.println();
                            int monthTotal = mTotalTransactionDaoImpl.getTotalTransForMonth(month, year, transactionType);

                            TotalTransaction totalTransaction = new TotalTransactionImpl();
                            totalTransaction.setMonth(month);
                            totalTransaction.setYear(year);
                            totalTransaction.setType(transactionType);

                            if(monthTotal == 0){
                                //Insert new total transaction
                                totalTransaction.setTotal(transactionAmount);
                                mTotalTransactionDaoImpl.createRow(totalTransaction);

                            }else{
                                //Update total transaction
                                int newTotal = monthTotal + transactionAmount;
                                totalTransaction.setTotal(newTotal);
                                mTotalTransactionDaoImpl.updateRow(totalTransaction);
                            }

                        }

                    }

                }while(resultCursor.moveToNext());

                resultCursor.moveToFirst();

            }

            setLastUpdateDate(LAST_UPDATE_SPREF_KEY, mDateHelper.getDateFromCalendar(currentDateCal));

            resultCursor.close();
        }

    }

    //TransactionImpl
    private String mTransTableName = TransactionEnum.TABLE_NAME;
    private String mTransIDCol = TransactionEnum.ID;
    private String mTransAmountCol = TransactionEnum.AMOUNT;
    private String mTransactionType = TransactionEnum.TYPE;

    //Repeat Days
    private String repeatDaysTableName = RepeatDaysEnum.TABLE_NAME;
    private String repeatDaysColName = RepeatDaysEnum.DAYS;
    private String repeatDaysTransactionID = RepeatDaysEnum.TRANSACTION_ID;
    private String repeatStartDateColName = RepeatDaysEnum.REPEAT_START_DATE;
    private String repeatEndDateColName = RepeatDaysEnum.REPEAT_END_DATE;


    private Cursor getRepeatTransactions(){

        Cursor transactCursor = mDBHelper.rawQuery(
            "SELECT " +
                    " transact."+mTransIDCol+", transact."+mTransAmountCol+", transact."+mTransactionType+", "+
                    " repeat."+repeatDaysColName+", repeat."+repeatStartDateColName+", repeat."+repeatEndDateColName+

            " FROM  "+ repeatDaysTableName+" AS repeat "+

                    "INNER JOIN "+mTransTableName+" AS transact ON repeat."+repeatDaysTransactionID+" = transact."+mTransIDCol
        );

        if(transactCursor != null){
            return transactCursor;
        }

        return null;
    }

    private void setLastUpdateDate(String sharedPrefKey, String lastUpdateDate){
        SharedPreferences sharedPref = ThisApplication.getContext().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedPrefKey, lastUpdateDate);
        editor.apply();
    }

    private String getLastUpdateDate(String sharedPrefKey){
        SharedPreferences sharedPref = ThisApplication.getContext().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        return sharedPref.getString(sharedPrefKey, null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        new GetTransactionListTask(mDBHelper, responseCaller, mDateMinRange, mDateMaxRange).execute();
    }

}
