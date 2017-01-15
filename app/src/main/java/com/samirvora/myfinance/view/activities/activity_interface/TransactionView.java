package com.samirvora.myfinance.view.activities.activity_interface;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;

import java.util.List;

/**
 * Created by James on 05/11/2016.
 */
public interface TransactionView {

    String getTransactTitle();
    String getTransactAmount();
    String getTransactionType();
    void setCategoryImage(int imgID);
    void setCategoryName(String categoryName);

    void setTransactionType(String transactionType);

    Category getCategory();
    void setCategoryData(List<Category> categoryData);

    boolean getIsRepeatActive();
    String getRepeatAmount();

    String getRepeatPeriod();
    String getRepeatEndDate();
}
