package com.samirvora.myfinance.model.daos.dao_interfaces;


/**
 * Created by James on 23/10/2016.
 */
public interface TotalTransactionDao<T> extends GenericDao<T> {

    int getTotalTransForMonth(int month, int year, String type);

}
