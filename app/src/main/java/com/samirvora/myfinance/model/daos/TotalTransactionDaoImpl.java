package com.samirvora.myfinance.model.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.samirvora.myfinance.model.daos.dao_interfaces.TotalTransactionDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.TotalTransactionEnum;
import com.samirvora.myfinance.model.pojos.TotalTransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.TotalTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by samirv on 30/08/2016.
 */
public class TotalTransactionDaoImpl implements TotalTransactionDao<TotalTransaction> {

    private DatabaseCrud mDBHelper;

    private String mTotTransTableName = TotalTransactionEnum.TABLE_NAME;
    private String mTotTransID = TotalTransactionEnum.ID;
    private String mTotTransTotalCol = TotalTransactionEnum.TOTAL;
    private String mTotTransMonthCol = TotalTransactionEnum.MONTH;
    private String mTotTransYearCol = TotalTransactionEnum.YEAR;
    private String mTotTransTypeCol = TotalTransactionEnum.TYPE;

    public TotalTransactionDaoImpl(DatabaseCrud dbHelper){
        this.mDBHelper = dbHelper;
    }


    @Override
    public int getTotalTransForMonth(int month, int year, String type){

        Cursor queryResultCur = mDBHelper.rawQuery(
                "SELECT tot_transact."+mTotTransTotalCol
                        +" FROM "+mTotTransTableName+" AS tot_transact"
                        +" WHERE"
                        +" tot_transact."+mTotTransTypeCol+" = '"+type+"'"
                        +" AND tot_transact."+mTotTransMonthCol+" = "+month
                        +" AND tot_transact."+mTotTransYearCol+" = "+year
        );

        if(queryResultCur == null){
            return 0;
        }

        try{
            return queryResultCur.getInt(0);
        }finally {
            queryResultCur.close();
        }

    }


    @Override
    public long createRow(TotalTransaction totalTransaction) {
        ContentValues totTransCV = new ContentValues();
        totTransCV.put(mTotTransTotalCol, totalTransaction.getTotal());
        totTransCV.put(mTotTransMonthCol, totalTransaction.getMonth());
        totTransCV.put(mTotTransYearCol, totalTransaction.getYear());
        totTransCV.put(mTotTransTypeCol, totalTransaction.getType());
        return mDBHelper.createRow(mTotTransTableName, totTransCV);
    }

    @Override
    public TotalTransaction readRow(long totTransID) {
        Cursor result = mDBHelper.readRows(
                mTotTransTableName,
                new String[]{
                        mTotTransID,
                        mTotTransTotalCol,
                        mTotTransMonthCol,
                        mTotTransYearCol,
                        mTotTransTypeCol
                },
                mTotTransID+" = ?",
                new String[]{
                        Long.toString(totTransID)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            TotalTransactionImpl transaction =  new TotalTransactionImpl(
                    result.getLong(0),
                    result.getInt(1),
                    result.getInt(2),
                    result.getInt(3),
                    result.getString(4)
            );

            result.close();
            return transaction;

        }else{
            return null;
        }
    }

    @Override
    public List<TotalTransaction> readAllRow() {
        Cursor result = mDBHelper.readRows(
                mTotTransTableName,
                new String[]{
                        mTotTransID,
                        mTotTransTotalCol,
                        mTotTransMonthCol,
                        mTotTransYearCol,
                        mTotTransTypeCol
                },
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(result != null){

            List<TotalTransaction> resultList = new ArrayList<>();

            do{
                TotalTransaction transaction = new TotalTransactionImpl(
                        result.getLong(0),
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getString(4)
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
                mTotTransTableName,
                mTotTransID+" = ?",
                new String[]{
                        Long.toString(id)
                }
        );

        return (deletedRows > 0 );
    }

    @Override
    public int updateRow(TotalTransaction totalTransaction) {
        ContentValues totTransCV = new ContentValues();
        totTransCV.put(mTotTransTotalCol, totalTransaction.getTotal());
        totTransCV.put(mTotTransMonthCol, totalTransaction.getMonth());
        totTransCV.put(mTotTransYearCol, totalTransaction.getYear());
        totTransCV.put(mTotTransTypeCol, totalTransaction.getType());

        //Update the transaction row
        return mDBHelper.updateRow(
                mTotTransTableName,
                totTransCV,
                mTotTransMonthCol + " = ? AND "+mTotTransYearCol+" = ? AND "+mTotTransTypeCol+" = ?",
                new String[]{
                        Integer.toString(totalTransaction.getMonth()),
                        Integer.toString(totalTransaction.getYear()),
                        totalTransaction.getType()
                }
        );
    }
}
