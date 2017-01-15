package com.samirvora.myfinance.model.services;

import com.samirvora.myfinance.model.daos.TotalTransactionDaoImpl;
import com.samirvora.myfinance.model.daos.TransactionDaoImpl;
import com.samirvora.myfinance.model.daos.dao_interfaces.TotalTransactionDao;
import com.samirvora.myfinance.model.daos.dao_interfaces.TransactionDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.pojos.TransactionImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.TotalTransaction;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.model.services.service_interface.TransactionService;
import com.samirvora.myfinance.model.tasks.OnTransactionListResponse;
import com.samirvora.myfinance.model.tasks.UpdateRepeatTransactionsTask;
import com.samirvora.myfinance.util.DateHelper;

/**
 * Created by James on 30/10/2016.
 */
public class TransactionServiceImpl implements TransactionService {

    private TransactionDao<Transaction> transactionDao;
    private TotalTransactionDao<TotalTransaction> totalTransactionDao;

    private DatabaseCrud mDBHelper;

    //Temp
    private DateHelper mDateHelper;

    public TransactionServiceImpl(DatabaseCrud dbConnection){
        mDBHelper = dbConnection;
        transactionDao = new TransactionDaoImpl(mDBHelper);
        totalTransactionDao = new TotalTransactionDaoImpl(mDBHelper);

        mDateHelper = DateHelper.getInstance();
    }


    @Override
    public long createTransaction(String title, String type, int amount, long categoryID) {
        //Insert TransactionImpl
        TransactionImpl transaction = new TransactionImpl();
        transaction.setTitle(title);
        transaction.setAmount(amount);
        transaction.setCategoryID(categoryID);
        transaction.setDatePosted(mDateHelper.getTodaysDateAsString());
        transaction.setType(type);
        return transactionDao.createRow(transaction);
    }

    @Override
    public int updateTransaction(Transaction transaction) {
        return transactionDao.updateRow(transaction);
    }

    @Override
    public boolean deleteTransaction(long transactionID) {
        return transactionDao.deleteRow(transactionID);
    }

    @Override
    public void getTransactionList(OnTransactionListResponse<Transaction> responseHandler, String minDate, String maxDate) {
        new UpdateRepeatTransactionsTask(mDBHelper, responseHandler, minDate, maxDate).execute();
    }

    @Override
    public int getTotalTransaction(int month, int year, String type) {
        return totalTransactionDao.getTotalTransForMonth(month, year, type);
    }

}
