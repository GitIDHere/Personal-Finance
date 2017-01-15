package com.samirvora.myfinance.view.activities.activity_interface;

/**
 * Created by James on 12/11/2016.
 */
public interface EditTransactionView extends TransactionView{

    //Setters
    //void setType(String type);
    void setTitle(String title);
    void setAmount(String amount);
    void setDatePosted(String datePosted);
    void setRepeatCheckBox(boolean isRepeat);
    void setRepeatAmount(String amount);
    void setRepeatPeriod(String period);
    void setRepeatEndDate(String endDate);



    //Getters
    String getDatePosted();

//    void setCategoryID(long categoryID);
//    long getCategoryID();

}
