package com.samirvora.myfinance.presenter.presenters.presenter_interfaces;

/**
 * Created by James on 30/10/2016.
 */
public interface HomePresenter {

    void onPreviousMonthClick();
    void onNextMonthClick();

    void displayMonthYear(String month, String year);
    void displayCurrentMonthYear();

    void sortListView(int itemID);
    int getCurrentSortID();

}
