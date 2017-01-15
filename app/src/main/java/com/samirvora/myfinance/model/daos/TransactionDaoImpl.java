package com.samirvora.myfinance.model.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.samirvora.myfinance.model.daos.dao_interfaces.TransactionDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.TransactionEnum;
import com.samirvora.myfinance.model.pojos.TransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by samirv on 30/08/2016.
 */
public class TransactionDaoImpl implements TransactionDao<Transaction>{

    private DatabaseCrud mDBHelper;

    private String mTransTableName = TransactionEnum.TABLE_NAME;
    private String mTransIDCol = TransactionEnum.ID;
    private String mTransTitleCol = TransactionEnum.TITLE;
    private String mTransAmountCol = TransactionEnum.AMOUNT;
    private String mTransCategoryCol = TransactionEnum.CATEGORY;
    private String mTransDatePostedCol = TransactionEnum.DATE_POSTED;
    private String mTransTypeCol = TransactionEnum.TYPE;


    public TransactionDaoImpl(DatabaseCrud dbHelper){
        this.mDBHelper = dbHelper;
    }


    //The problem is I don't know when to close the DB connection
    @Override
    public long createRow(Transaction transactItem) {
        ContentValues transactionCV = new ContentValues();
        transactionCV.put(mTransTitleCol, transactItem.getTitle());
        transactionCV.put(mTransAmountCol, transactItem.getAmount());
        transactionCV.put(mTransCategoryCol, transactItem.getCategoryID());
        transactionCV.put(mTransDatePostedCol, transactItem.getDatePosted());
        transactionCV.put(mTransTypeCol, transactItem.getType());
        return mDBHelper.createRow(mTransTableName, transactionCV);
    }

    @Override
    public Transaction readRow(long transactionID) {
        Cursor result = mDBHelper.readRows(
                mTransTableName,
                new String[]{
                        mTransIDCol,
                        mTransTitleCol,
                        mTransAmountCol,
                        mTransCategoryCol,
                        mTransDatePostedCol,
                        mTransTypeCol
                },
                mTransIDCol+" = ?",
                new String[]{
                        Long.toString(transactionID)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            Transaction transaction =  new TransactionImpl(
                    result.getLong(0),
                    result.getString(1),
                    result.getInt(2),
                    result.getLong(3),
                    result.getString(4),
                    result.getString(5)
            );

            result.close();
            return transaction;

        }else{
            return null;
        }
    }

    @Override
    public List<Transaction> readAllRow() {
        Cursor result = mDBHelper.readRows(
                mTransTableName,
                new String[]{
                        mTransIDCol,
                        mTransTitleCol,
                        mTransAmountCol,
                        mTransCategoryCol,
                        mTransDatePostedCol,
                        mTransTypeCol
                },
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(result != null){

            List<Transaction> resultList = new ArrayList<>();

            do{
                Transaction transaction = new TransactionImpl(
                        result.getLong(0),
                        result.getString(1),
                        result.getInt(2),
                        result.getLong(3),
                        result.getString(4),
                        result.getString(5)
                );

                resultList.add(transaction);

            }while(result.moveToNext());

            result.close();

            return resultList;
        }else{
            return null;
        }
    }

    @Override
    public boolean deleteRow(long id) {
        int deletedRows = mDBHelper.deleteRow(
                mTransTableName,
                mTransIDCol+" = ?",
                new String[]{
                        Long.toString(id)
                }
        );

        return (deletedRows > 0 );
    }

    @Override
    public int updateRow(Transaction transactItem) {
        ContentValues transactionCV = new ContentValues();
        transactionCV.put(mTransTitleCol, transactItem.getTitle());
        transactionCV.put(mTransAmountCol, transactItem.getAmount());
        transactionCV.put(mTransCategoryCol, transactItem.getCategoryID());
        transactionCV.put(mTransDatePostedCol, transactItem.getDatePosted());
        transactionCV.put(mTransTypeCol, transactItem.getType());

        //Update the transaction row
        return mDBHelper.updateRow(
                mTransTableName,
                transactionCV,
                mTransIDCol + " = ? ",
                new String[]{
                        Long.toString(transactItem.getID())
                }
        );
    }

}

