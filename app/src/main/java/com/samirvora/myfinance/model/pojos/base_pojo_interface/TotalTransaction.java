package com.samirvora.myfinance.model.pojos.base_pojo_interface;

/**
 * Created by James on 06/11/2016.
 */
public interface TotalTransaction extends Table {

    int getTotal();
    void setTotal(int total);

    int getMonth();
    void setMonth(int month);

    int getYear();
    void setYear(int year);

    String getType();
    void setType(String type);

}
