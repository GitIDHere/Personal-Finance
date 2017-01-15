package com.samirvora.myfinance.model.pojos.base_pojo_interface;

/**
 * Created by James on 06/11/2016.
 */
public interface Category extends Table{

    long getID();
    void setID(long id);
    int getImg();
    void setImg(int img);
    String getName();
    void setName(String name);

}
