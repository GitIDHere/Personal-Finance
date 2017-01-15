package com.samirvora.myfinance.view.activities.activity_interface;

import java.util.List;

/**
 * Created by James on 29/10/2016.
 */
public interface HomeView<T>{

    String getCurrentMonth();
    String getCurrentYear();

    void setCurrentMonth(String month);
    void setCurrentYear(String year);

    void setEnableNextMonthNavButton(boolean isEnable);
    
    void setTotalExpense(String expense);
    void setTotalIncome(String income);

    void setListAdapterData(List<T> dataList);
    void clearListAdapterData();
}
