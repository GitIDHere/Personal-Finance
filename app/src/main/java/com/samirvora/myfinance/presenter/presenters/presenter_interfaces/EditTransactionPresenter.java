package com.samirvora.myfinance.presenter.presenters.presenter_interfaces;

/**
 * Created by James on 12/11/2016.
 */
public interface EditTransactionPresenter {

    void setCategoryData(String transactionType);

    boolean submitEditTransaction();

    boolean deleteTransaction();

}
