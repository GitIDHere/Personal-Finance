package com.samirvora.myfinance.model.pojos.base_pojo_interface;

/**
 * Created by James on 06/11/2016.
 */
public interface Transaction extends Table {

    long getCategoryID();
    void setCategoryID(long categoryID);

    String getTitle();
    void setTitle(String title);

    int getAmount();
    void setAmount(int amount);

    String getDatePosted();
    void setDatePosted(String datePosted);

    String getType();
    void setType(String type);



    boolean isRepeat();
    void setRepeat(boolean isRepeat);

    String getRepeatEndDate();
    void setRepeatEndDate(String repeatEndDate);

    int getCategoryImg();
    void setCategoryImg(int categoryImg);

    String getCategoryName();
    void setCategoryName(String categoryName);

}
