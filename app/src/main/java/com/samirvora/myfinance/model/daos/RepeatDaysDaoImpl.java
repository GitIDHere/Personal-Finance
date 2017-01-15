package com.samirvora.myfinance.model.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.samirvora.myfinance.model.daos.dao_interfaces.RepeatDaysDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.RepeatDaysEnum;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;
import com.samirvora.myfinance.model.pojos.RepeatDaysImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by samirv on 30/08/2016.
 */
public class RepeatDaysDaoImpl implements RepeatDaysDao<RepeatDays> {

    private DatabaseCrud mDBHelper;

    private String mRepDaysTableName = RepeatDaysEnum.TABLE_NAME;
    private String mRepDaysIDCol = RepeatDaysEnum.ID;
    private String mRepDaysTransIDCol = RepeatDaysEnum.TRANSACTION_ID;
    private String mRepDaysAmountCol = RepeatDaysEnum.AMOUNT;
    private String mRepDaysPeriodCol = RepeatDaysEnum.PERIOD;
    private String mRepDaysDaysCol = RepeatDaysEnum.DAYS;
    private String mRepDaysStartDateCol = RepeatDaysEnum.REPEAT_START_DATE;
    private String mRepDaysEndDateCol = RepeatDaysEnum.REPEAT_END_DATE;


    public RepeatDaysDaoImpl(DatabaseCrud dbHelper){
        this.mDBHelper = dbHelper;
    }


    @Override
    public RepeatDays getRepeatDaysByTransID(long transID) {
        Cursor result = mDBHelper.readRows(
                mRepDaysTableName,
                new String[]{
                        mRepDaysIDCol,
                        mRepDaysTransIDCol,
                        mRepDaysAmountCol,
                        mRepDaysPeriodCol,
                        mRepDaysDaysCol,
                        mRepDaysStartDateCol,
                        mRepDaysEndDateCol
                },
                mRepDaysTransIDCol+" = ?",
                new String[]{
                        Long.toString(transID)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            RepeatDaysImpl repeatDays = new RepeatDaysImpl(
                    result.getLong(0),
                    result.getLong(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getString(5),
                    result.getString(6)
            );

            result.close();
            return repeatDays;

        }else{
            return null;
        }
    }

    @Override
    public long createRow(RepeatDays repeatDaysItem) {
        ContentValues repeatCV = new ContentValues();
        repeatCV.put(mRepDaysTransIDCol, repeatDaysItem.getTransactionID());
        repeatCV.put(mRepDaysAmountCol, repeatDaysItem.getRepeatAmount());
        repeatCV.put(mRepDaysPeriodCol, repeatDaysItem.getRepeatPeriod());
        repeatCV.put(mRepDaysDaysCol, repeatDaysItem.getRepeatDays());
        repeatCV.put(mRepDaysStartDateCol, repeatDaysItem.getRepeatStartDate());
        repeatCV.put(mRepDaysEndDateCol, repeatDaysItem.getRepeatEndDate());
        return mDBHelper.createRow(mRepDaysTableName, repeatCV);
    }

    @Override
    public RepeatDays readRow(long id) {
        Cursor result = mDBHelper.readRows(
                mRepDaysTableName,
                new String[]{
                        mRepDaysIDCol,
                        mRepDaysTransIDCol,
                        mRepDaysAmountCol,
                        mRepDaysPeriodCol,
                        mRepDaysDaysCol,
                        mRepDaysStartDateCol,
                        mRepDaysEndDateCol
                },
                mRepDaysIDCol+" = ?",
                new String[]{
                        Long.toString(id)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            RepeatDaysImpl repeatDays = new RepeatDaysImpl(
                    result.getLong(0),
                    result.getLong(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getString(5),
                    result.getString(6)
            );

            result.close();
            return repeatDays;

        }else{
            return null;
        }
    }

    @Override
    public List<RepeatDays> readAllRow() {
        Cursor result = mDBHelper.readRows(
                mRepDaysTableName,
                new String[]{
                        mRepDaysIDCol,
                        mRepDaysTransIDCol,
                        mRepDaysAmountCol,
                        mRepDaysPeriodCol,
                        mRepDaysDaysCol,
                        mRepDaysStartDateCol,
                        mRepDaysEndDateCol
                },
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(result != null){

            List<RepeatDays> resultList = new ArrayList<>();

            do{
                RepeatDays repeatDays = new RepeatDaysImpl(
                        result.getLong(0),
                        result.getLong(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getString(5),
                        result.getString(6)
                );

                resultList.add(repeatDays);

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
                mRepDaysTableName,
                mRepDaysIDCol+" = ?",
                new String[]{
                        Long.toString(id)
                }
        );

        return (deletedRows > 0 );
    }

    @Override
    public int updateRow(RepeatDays repeatDaysItem) {
        ContentValues repeatCV = new ContentValues();
        repeatCV.put(mRepDaysTransIDCol, repeatDaysItem.getTransactionID());
        repeatCV.put(mRepDaysAmountCol, repeatDaysItem.getRepeatAmount());
        repeatCV.put(mRepDaysPeriodCol, repeatDaysItem.getRepeatPeriod());
        repeatCV.put(mRepDaysDaysCol, repeatDaysItem.getRepeatDays());
        repeatCV.put(mRepDaysStartDateCol, repeatDaysItem.getRepeatStartDate());
        repeatCV.put(mRepDaysEndDateCol, repeatDaysItem.getRepeatEndDate());

        return mDBHelper.updateRow(
                mRepDaysTableName,
                repeatCV,
                mRepDaysIDCol + " = ? ",
                new String[]{
                        Long.toString(repeatDaysItem.getID())
                }
        );
    }
}
