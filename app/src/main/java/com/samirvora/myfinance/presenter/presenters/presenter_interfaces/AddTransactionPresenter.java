package com.samirvora.myfinance.presenter.presenters.presenter_interfaces;

/**
 * Created by James on 05/11/2016.
 */
public interface AddTransactionPresenter<T> {
    //List<T> getCategoryData(int vID);
    //void setTransactType(int vID);
    void setCategoryData(int vID);
    boolean submitTransaction();
}
