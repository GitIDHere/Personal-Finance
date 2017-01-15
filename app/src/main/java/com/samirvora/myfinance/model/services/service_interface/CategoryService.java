package com.samirvora.myfinance.model.services.service_interface;

import java.util.List;

/**
 * Created by James on 05/11/2016.
 */
public interface CategoryService<T> {
    long insertCategory(T category);
    List<T> getExpenseCategoryData();
    List<T> getIncomeCategoryData();

}
