package com.samirvora.myfinance.model.pojos.base_pojo_interface;

/**
 * Created by James on 06/11/2016.
 */
public interface RepeatDays extends Table {

    long getTransactionID();
    void setTransactionID(long transactionID);

    int getRepeatAmount();
    void setRepeatAmount(int repeat_amount);

    String getRepeatPeriod();
    void setRepeatPeriod(String repeat_period);

    int getRepeatDays();
    void setRepeatDays(int repeat_days);

    String getRepeatEndDate();
    void setRepeatEndDate(String repeatEndDate);

    String getRepeatStartDate();
    void setRepeatStartDate(String repeatStartDate);
}
