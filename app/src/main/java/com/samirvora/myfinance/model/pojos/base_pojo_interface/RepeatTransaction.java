package com.samirvora.myfinance.model.pojos.base_pojo_interface;

/**
 * Created by James on 12/11/2016.
 */
public interface RepeatTransaction extends Table {

    long getTransactionID();
    void setTransactionID(long transactionID);

    String getRepeatTransactDate();
    void setRepeatTransactDate(String repeatTransactDate);

    int getRepeatTransactAmount();
    void setRepeatTransactAmount(int amount);

}
