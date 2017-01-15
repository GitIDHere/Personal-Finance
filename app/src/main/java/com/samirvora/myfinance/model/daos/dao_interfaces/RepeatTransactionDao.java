package com.samirvora.myfinance.model.daos.dao_interfaces;

/**
 * Created by James on 12/11/2016.
 */
public interface RepeatTransactionDao<T> extends GenericDao<T>{

    boolean deleteRowsByTransaction(long transactionID);

}
