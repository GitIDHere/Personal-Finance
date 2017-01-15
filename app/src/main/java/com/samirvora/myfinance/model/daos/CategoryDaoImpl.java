package com.samirvora.myfinance.model.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.samirvora.myfinance.model.daos.dao_interfaces.CategoryDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.enums.CategoryEnum;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.model.pojos.CategoryImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by James on 09/10/2016.
 */
public class CategoryDaoImpl implements CategoryDao<Category>{

    private DatabaseCrud mDBHelper;

    //ENUMS
    private String mCategoryTableName = CategoryEnum.TABLE_NAME;
    private String mCategoryID = CategoryEnum.ID;
    private String mCategoryName = CategoryEnum.NAME;
    private String mCategoryImg = CategoryEnum.IMG;

    public CategoryDaoImpl(DatabaseCrud dbHelper){
        this.mDBHelper = dbHelper;
    }


    @Override
    public long createRow(Category item) { //This could return -1
        ContentValues transactionCV = new ContentValues();
        transactionCV.put(mCategoryName, item.getName());
        transactionCV.put(mCategoryImg, item.getImg());

        long result = mDBHelper.addRowWithOnConflictIgnore(mCategoryTableName, transactionCV);

        if(result == -1){
            result = getCategoryID(item.getName(), item.getImg());
        }

        return result;
    }


    @Override
    public long getCategoryID(String name, int img) {

        Cursor result = mDBHelper.readRows(
                mCategoryTableName,
                new String[]{
                        mCategoryID
                },
                mCategoryName+" = ? AND "+mCategoryImg+" = ?",
                new String[]{
                        name,
                        Integer.toString(img)
                },
                null,
                null,
                null,
                "1"
        );

        try{
            return result.getLong(0);
        }finally {
            result.close();
        }
    }

    @Override
    public Category readRow(long categoryRowID) {
        Cursor result = mDBHelper.readRows(
                mCategoryTableName,
                new String[]{
                        mCategoryID,
                        mCategoryName,
                        mCategoryImg
                },
                mCategoryID+" = ?",
                new String[]{
                        Long.toString(categoryRowID)
                },
                null,
                null,
                null,
                "1"
        );

        if(result != null){

            Category category =  new CategoryImpl(
                    result.getLong(0),
                    result.getString(1),
                    result.getInt(2)
            );

            result.close();
            return category;

        }else{
            return null;
        }
    }

    @Override
    public List<Category> readAllRow() {
        Cursor result = mDBHelper.readRows(
                mCategoryTableName,
                new String[]{
                        mCategoryID,
                        mCategoryName,
                        mCategoryImg
                },
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(result != null){

            List<Category> resultList = new ArrayList<>();

            do{
                Category category = new CategoryImpl(
                        result.getLong(0),
                        result.getString(1),
                        result.getInt(2)
                );

                resultList.add(category);

            }while(result.moveToNext());

            result.close();

            return resultList;
        }else{
            return null;
        }
    }

    @Override
    public boolean deleteRow(long rowID) {
        int deletedRows = mDBHelper.deleteRow(
                mCategoryTableName,
                mCategoryID+" = ?",
                new String[]{
                        Long.toString(rowID)
                }
        );

        return (deletedRows > 0 );
    }

    @Override
    public int updateRow(Category item) {
        ContentValues transactionCV = new ContentValues();
        transactionCV.put(mCategoryName, item.getName());
        transactionCV.put(mCategoryImg, item.getImg());

        //Update the transaction row
        return mDBHelper.updateRow(
                mCategoryTableName,
                transactionCV,
                mCategoryID + " = ? ",
                new String[]{
                        Long.toString(item.getID())
                }
        );
    }

}
