package com.samirvora.myfinance.model.daos.dao_interfaces;

import java.util.List;

/**
 * Created by James on 30/10/2016.
 */
public interface GenericDao<E> {

    //create
    long createRow(E item);

    //read
    E readRow(long rowID);

    //readll
    List<E> readAllRow();

    //delete
    boolean deleteRow(long id);

    //update
    int updateRow(E item);

}
