package com.samirvora.myfinance.model.daos.dao_interfaces;

/**
 * Created by James on 23/10/2016.
 */
public interface RepeatDaysDao<T> extends GenericDao<T>{

    T getRepeatDaysByTransID(long transID);

}
