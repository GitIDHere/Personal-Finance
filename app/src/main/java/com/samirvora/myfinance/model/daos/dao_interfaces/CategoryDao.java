package com.samirvora.myfinance.model.daos.dao_interfaces;

/**
 * Created by James on 06/11/2016.
 */
public interface CategoryDao<T> extends GenericDao<T>{

    long getCategoryID(String name, int img);

}
