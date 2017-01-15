package com.samirvora.myfinance.model.services;

import com.samirvora.myfinance.model.daos.RepeatDaysDaoImpl;
import com.samirvora.myfinance.model.daos.RepeatTransactionDaoImpl;
import com.samirvora.myfinance.model.daos.dao_interfaces.RepeatDaysDao;
import com.samirvora.myfinance.model.daos.dao_interfaces.RepeatTransactionDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatTransaction;
import com.samirvora.myfinance.model.services.service_interface.RepeatTransactionService;

/**
 * Created by James on 06/11/2016.
 */
public class RepeatTransactionServiceImpl implements RepeatTransactionService {

    private DatabaseCrud mDBHelper;

    private RepeatDaysDao<RepeatDays> mRepeatDaysDao;
    private RepeatTransactionDao<RepeatTransaction> mRepeatTransactionDao;


    public RepeatTransactionServiceImpl(DatabaseCrud dbConnection){
        mDBHelper = dbConnection;
        mRepeatDaysDao = new RepeatDaysDaoImpl(mDBHelper);
        mRepeatTransactionDao = new RepeatTransactionDaoImpl(mDBHelper);
    }

    @Override
    public RepeatDays getRepeatDaysByTransID(long transID) {
        return mRepeatDaysDao.getRepeatDaysByTransID(transID);
    }

    @Override
    public int updateRepeatDays(RepeatDays repeatDays){
       return mRepeatDaysDao.updateRow(repeatDays);
    }

    @Override
    public boolean deleteRepeatDays(long repeatDaysID) {
        return mRepeatDaysDao.deleteRow(repeatDaysID);
    }

    @Override
    public boolean deleteRepeatTransactionByTransID(long transactionID) {
        return mRepeatTransactionDao.deleteRowsByTransaction(transactionID);
    }

    @Override
    public long insertRepeatDaysRow(RepeatDays repeatDays) {
        return mRepeatDaysDao.createRow(repeatDays);
    }

    @Override
    public long insertRepeatTransactionRow(RepeatTransaction repeatTransaction) {
        return mRepeatTransactionDao.createRow(repeatTransaction);
    }

    public boolean removeRepeatDays(long repeatDaysRowID){
        return mRepeatDaysDao.deleteRow(repeatDaysRowID);
    }


}
