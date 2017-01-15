package com.samirvora.myfinance.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by samirv on 19/08/2016.
 */
public class DBHelper implements DatabaseCrud{

    private DBConnect mDBConnect;
    private SQLiteDatabase mDB;


    /******** SINGLETON CONTRUCTOR ********/

    private static DBHelper mDBHelper = null;

    public static DBHelper getInstance(){
        if(mDBHelper == null){
            mDBHelper = new DBHelper();
        }
        return mDBHelper;
    }

    private DBHelper(){
        mDBConnect = new DBConnect();
    }

    /******** SINGLETON CONTRUCTOR ********/





    /************ TABLE CRUD *********/

    // Adding new Row to table
    @Override
    public long createRow(String tableName, ContentValues values) {
        mDB = mDBConnect.getWritableDatabase();
        return mDB.insert(tableName, null, values);
    }


    public long addRowWithOnConflictIgnore(String tableName, ContentValues values){
        mDB = mDBConnect.getWritableDatabase();
        return mDB.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    @Override
    public Cursor rawQuery(String query){

        mDB = mDBConnect.getWritableDatabase();
        Cursor cursor = mDB.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){
                return cursor;
            }

            cursor.close();

        }

        return null;
    }



    // Getting single contact
    @Override
    public Cursor readRows(String tableName, String[] columnHeaders, String whereClause, String[] whereArgs, String rowGroup, String rowGroupHaving, String orderBy, String limit) {

        mDB = mDBConnect.getWritableDatabase();

        Cursor cursor = mDB.query(
                tableName,
                columnHeaders,
                whereClause,
                whereArgs,
                rowGroup,
                rowGroupHaving,
                orderBy,
                limit
        );

        if(cursor != null){

            if(cursor.moveToFirst()){
                return cursor;
            }

            cursor.close();
        }

        return null;
    }



    // Updating single contact
    @Override
    public int updateRow(String tableName, ContentValues newValues, String whereClause, String[] whereArgs){

        mDB = mDBConnect.getWritableDatabase();

        int result = mDB.update(
                tableName,
                newValues,
                whereClause,
                whereArgs);

        return result;
    }


    // Deleting single contact
    @Override
    public int deleteRow(String tableName, String whereClause, String[] whereArgs) {

        mDB = mDBConnect.getWritableDatabase();

        int rowsDeleted = mDB.delete(tableName,
                whereClause,
                whereArgs);

        return rowsDeleted;
    }


    public void closeDB(){
        mDBConnect.close();
    }

    /************ EXPENSES CRUD *********/

}
