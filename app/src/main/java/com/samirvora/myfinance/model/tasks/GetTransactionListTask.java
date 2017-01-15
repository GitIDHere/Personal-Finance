package com.samirvora.myfinance.model.tasks;

import android.database.Cursor;
import android.os.AsyncTask;

import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.CategoryEnum;
import com.samirvora.myfinance.model.enums.RepeatDaysEnum;
import com.samirvora.myfinance.model.enums.RepeatTransactionEnum;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.TransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samirv on 15/09/2016.
 */
public class GetTransactionListTask extends AsyncTask<String, String, List<Transaction> > {

    private OnTransactionListResponse<Transaction> responseCaller = null;
    private String mDateMaxRange;
    private String mDateMinRange;

    private DatabaseCrud mDBHelper;

    private int totalExpense = 0;
    private int totalIncome = 0;

    public GetTransactionListTask(DatabaseCrud dbHandler, OnTransactionListResponse<Transaction> responseCaller, String minDate, String maxDate) {
        super();
        this.responseCaller = responseCaller;
        this.mDateMaxRange = maxDate;
        this.mDateMinRange = minDate;

        this.mDBHelper = dbHandler;
    }

    @Override
    protected List<Transaction> doInBackground(String... params) {

        Cursor result = mDBHelper.rawQuery(
                "SELECT trans.*," +
                        " cat."+ CategoryEnum.IMG +", "+
                        " cat."+ CategoryEnum.NAME +", "+
                        " reptrans."+ RepeatTransactionEnum.AMOUNT+" AS r_money_amt, "+
                        " reptrans."+ RepeatTransactionEnum.REPEAT_DATE+" AS r_date, "+
                        " repdays."+ RepeatDaysEnum.REPEAT_END_DATE+" AS rep_end_date " +

                        " FROM "+ TransactionEnum.TABLE_NAME+" AS trans "+
                        " LEFT JOIN "+ CategoryEnum.TABLE_NAME+" AS cat ON trans."+TransactionEnum.CATEGORY+" = cat."+CategoryEnum.ID +
                        " LEFT JOIN "+ RepeatDaysEnum.TABLE_NAME+" AS repdays ON trans."+TransactionEnum.ID+" = repdays."+RepeatDaysEnum.TRANSACTION_ID +
                        " LEFT JOIN "+ RepeatTransactionEnum.TABLE_NAME+" AS reptrans ON trans."+TransactionEnum.ID+" = reptrans."+RepeatTransactionEnum.TRANSACTION_ID+

                        " WHERE "+
                        //Where repeat is not null and the repeat date is within range
                        "( (reptrans."+RepeatTransactionEnum.REPEAT_DATE+" IS NOT NULL) AND (reptrans."+RepeatTransactionEnum.REPEAT_DATE+" >= '"+mDateMinRange+"' AND reptrans."+RepeatTransactionEnum.REPEAT_DATE+" <= '"+mDateMaxRange+"')"    +")"

                        +" OR "+
                        //Where repeat is null and the transaction date is in range
                        "(reptrans."+RepeatTransactionEnum.REPEAT_DATE+" IS NULL) AND (trans."+TransactionEnum.DATE_POSTED+" >= '"+mDateMinRange+"' AND trans."+TransactionEnum.DATE_POSTED+" <= '"+mDateMaxRange+"')"
        );

        if(result == null){
            System.out.println("Result is null");
            return null;
        }

        List<Transaction> transactionList = new ArrayList<>();

        do{
            long transID = result.getLong(0);
            String transTitle = result.getString(1);
            int transAmt = result.getInt(2);
            long transCateID = result.getLong(3);
            String transDatePosted = result.getString(4);
            String transType = result.getString(5);

            int categoryImg = result.getInt(6);
            String categoryName = result.getString(7);

            int repeatTransAmt = result.getInt(8);
            String repeatTransDate = result.getString(9);
            String repeatDaysEndDate = result.getString(10);

            TransactionImpl transaction = new TransactionImpl(
                    transID,
                    transTitle,
                    transAmt,
                    transCateID,
                    transDatePosted,
                    transType
            );

            if(transaction.getType().equals(TransactionEnum.TYPE_EXPENSE)){
                totalExpense += transAmt;
            }else{
                totalIncome += transAmt;
            }

            transaction.setCategoryImg(categoryImg);
            transaction.setCategoryName(categoryName);

            if(repeatDaysEndDate != null && repeatTransDate == null){//It is the first transaction
                transaction.setRepeat(true);
                transaction.setRepeatEndDate(repeatDaysEndDate);

            }else if(repeatDaysEndDate == null && repeatTransDate != null){
                transaction.setRepeat(false);
                transaction.setDatePosted(repeatTransDate);

            }else if(repeatTransDate != null){//It is a repeat

                transaction.setDatePosted(repeatTransDate);
                transaction.setAmount(repeatTransAmt);

                transaction.setRepeat(true);
                transaction.setRepeatEndDate(repeatDaysEndDate);
            }



            transactionList.add(transaction);

        }while(result.moveToNext());

        result.close();

        return transactionList;
    }

    @Override
    protected void onPostExecute(List<Transaction> result) {
        super.onPostExecute(result);
        responseCaller.onTransactionListCreateResponse(result, totalExpense, totalIncome);
    }

}
