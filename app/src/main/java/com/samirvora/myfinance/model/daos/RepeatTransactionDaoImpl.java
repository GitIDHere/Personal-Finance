package com.samirvora.myfinance.model.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.samirvora.myfinance.model.daos.dao_interfaces.RepeatTransactionDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.RepeatTransactionEnum;
import com.samirvora.myfinance.model.pojos.RepeatTransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by samirv on 30/08/2016.
 */
public class RepeatTransactionDaoImpl implements RepeatTransactionDao<RepeatTransaction> {

    private DatabaseCrud mDBHelper;

    private String mTableName = RepeatTransactionEnum.TABLE_NAME;
    private String mRepeatID = RepeatTransactionEnum.ID;
    private String mTransactionID = RepeatTransactionEnum.TRANSACTION_ID;
    private String mRepeatTransactMoneyAmt = RepeatTransactionEnum.AMOUNT;
    private String mRepeatTransactDate = RepeatTransactionEnum.REPEAT_DATE;

    private String mRepeatTransactMonth = RepeatTransactionEnum.MONTH;
    private String mRepeatTransactYear = RepeatTransactionEnum.YEAR;


    public RepeatTransactionDaoImpl(DatabaseCrud dbHelper){
        this.mDBHelper = dbHelper;
    }

    @Override
    public long createRow(RepeatTransaction repeatTransaction) {
        ContentValues repeatTransactionCV = new ContentValues();
        repeatTransactionCV.put(mTransactionID, repeatTransaction.getTransactionID());
        repeatTransactionCV.put(mRepeatTransactMoneyAmt, repeatTransaction.getRepeatTransactAmount());
        repeatTransactionCV.put(mRepeatTransactDate, repeatTransaction.getRepeatTransactDate());
        return mDBHelper.createRow(mTableName, repeatTransactionCV);
    }

    @Override
    public RepeatTransaction readRow(long rowID) {
        Cursor result = mDBHelper.readRows(
                mTableName,
                new String[]{
                        mRepeatID,
                        mTransactionID,
                        mRepeatTransactDate,
                        mRepeatTransactMoneyAmt
                },
                mRepeatID+" = ?",
                new String[]{
                        Long.toString(rowID)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            RepeatTransaction repeatTransaction =  new RepeatTransactionImpl(
                    result.getLong(0),
                    result.getLong(1),
                    result.getString(3),
                    result.getInt(2)
            );

            result.close();
            return repeatTransaction;

        }else{
            return null;
        }
    }

    @Override
    public List<RepeatTransaction> readAllRow() {
        Cursor result = mDBHelper.readRows(
                mTableName,
                new String[]{
                        mRepeatID,
                        mTransactionID,
                        mRepeatTransactDate,
                        mRepeatTransactMoneyAmt
                },
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(result != null){

            List<RepeatTransaction> resultList = new ArrayList<>();

            do{
                RepeatTransaction repeatTransaction = new RepeatTransactionImpl(
                        result.getLong(0),
                        result.getLong(1),
                        result.getString(3),
                        result.getInt(2)
                );

                resultList.add(repeatTransaction);

            }while(result.moveToNext());

            result.close();

            return resultList;
        }else{
            return null;
        }
    }

    @Override
    public boolean deleteRow(long rowID) {
        int deletedRows = mDBHelper.deleteRow(
                mTableName,
                mRepeatID+" = ?",
                new String[]{
                        Long.toString(rowID)
                }
        );

        return (deletedRows > 0 );
    }


    @Override
    public boolean deleteRowsByTransaction(long transactionID){
        int deletedRows = mDBHelper.deleteRow(
                mTableName,
                mTransactionID+" = ?",
                new String[]{
                        Long.toString(transactionID)
                }
        );

        return (deletedRows > 0 );
    }


    @Override
    public int updateRow(RepeatTransaction repeatTransaction) {
        ContentValues repeatTransactionCV = new ContentValues();
        repeatTransactionCV.put(mTransactionID, repeatTransaction.getTransactionID());
        repeatTransactionCV.put(mRepeatTransactMoneyAmt, repeatTransaction.getRepeatTransactAmount());
        repeatTransactionCV.put(mRepeatTransactDate, repeatTransaction.getRepeatTransactDate());

        //Update the transaction row
        return mDBHelper.updateRow(
                mTableName,
                repeatTransactionCV,
                mRepeatID + " = ? ",
                new String[]{
                        Long.toString(repeatTransaction.getID())
                }
        );
    }
}
