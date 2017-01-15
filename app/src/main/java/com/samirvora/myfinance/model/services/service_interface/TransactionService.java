package com.samirvora.myfinance.model.services.service_interface;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.model.tasks.OnTransactionListResponse;

/**
 * Created by James on 30/10/2016.
 */
public interface TransactionService {

    long createTransaction(String type, String title, int amount, long category);

    int updateTransaction(Transaction transaction);

    boolean deleteTransaction(long transactionID);

    int getTotalTransaction(int month, int year, String type);

    void getTransactionList(OnTransactionListResponse<Transaction> responseHandler, String minDate, String maxDate);

}
