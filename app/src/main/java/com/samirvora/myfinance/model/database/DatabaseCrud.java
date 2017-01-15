package com.samirvora.myfinance.model.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by James on 05/11/2016.
 */
public interface DatabaseCrud {
    //Create
    long createRow(String tableName, ContentValues values);
    long addRowWithOnConflictIgnore(String tableName, ContentValues values);

    //Read
    Cursor readRows(String tableName, String[] columnHeaders, String whereClause, String[] whereArgs, String rowGroup, String rowGroupHaving, String orderBy, String limit);

    //Update
    int updateRow(String tableName, ContentValues newValues, String whereClause, String[] whereArgs);

    //Delete
    int deleteRow(String tableName, String whereClause, String[] whereArgs);

    Cursor rawQuery(String query);

}
