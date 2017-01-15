package com.samirvora.myfinance.model.database;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.samirvora.myfinance.model.enums.*;
import com.samirvora.myfinance.util.ThisApplication;

/**
 * Created by James on 16/07/2016.
 */
public final class DBConnect extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserFinance.db";


    /********* SQL QUERIES *********/

    //TRANSACTION
    private final String createTransactionTable =
            "CREATE TABLE IF NOT EXISTS "+ TransactionEnum.TABLE_NAME+"("
                    +TransactionEnum.ID+" INTEGER PRIMARY KEY, "
                    +TransactionEnum.TITLE+" TEXT NOT NULL, "
                    +TransactionEnum.AMOUNT+" INTEGER NOT NULL, "
                    +TransactionEnum.CATEGORY+" TEXT NOT NULL, "
                    +TransactionEnum.DATE_POSTED +" TEXT NOT NULL, "
                    +TransactionEnum.TYPE+" TEXT NOT NULL"
                    +")";


    //CATEGORY
    private final String createCategoryTable =
            "CREATE TABLE IF NOT EXISTS "+ CategoryEnum.TABLE_NAME+"("
                    +CategoryEnum.ID+" INTEGER PRIMARY KEY, "
                    +CategoryEnum.NAME+" INTEGER NOT NULL, "
                    +CategoryEnum.IMG+" INTEGER NOT NULL UNIQUE "
                    +")";


    //TOTAL TRANSACTION
    private final String createTotalTransactionTable =
            "CREATE TABLE IF NOT EXISTS "+ TotalTransactionEnum.TABLE_NAME+"("
                    +TotalTransactionEnum.ID+" INTEGER PRIMARY KEY, "
                    +TotalTransactionEnum.TOTAL+" INTEGER NOT NULL, "
                    +TotalTransactionEnum.MONTH+" INTEGER NOT NULL, "
                    +TotalTransactionEnum.YEAR+" INTEGER NOT NULL, "
                    +TotalTransactionEnum.TYPE+" TEXT NOT NULL"
                    +")";


    //REPEAT_DAYS
    private final String createRepeatDaysTable =
            "CREATE TABLE IF NOT EXISTS "+ RepeatDaysEnum.TABLE_NAME+"("
                    + RepeatDaysEnum.ID+" INTEGER PRIMARY KEY, "
                    + RepeatDaysEnum.TRANSACTION_ID +" INTEGER NOT NULL, "
                    + RepeatDaysEnum.AMOUNT+" INTEGER NOT NULL, "
                    + RepeatDaysEnum.PERIOD+" TEXT NOT NULL, "
                    + RepeatDaysEnum.DAYS+" INTEGER NOT NULL, "
                    + RepeatDaysEnum.REPEAT_START_DATE+" TEXT NOT NULL, "
                    + RepeatDaysEnum.REPEAT_END_DATE+" TEXT NOT NULL"
                    +")";


    //ITEM_REPEATS
    private final String createItemRepeatsTable =
            "CREATE TABLE IF NOT EXISTS "+ RepeatTransactionEnum.TABLE_NAME+"("
                    + RepeatTransactionEnum.ID+" INTEGER PRIMARY KEY, "
                    + RepeatTransactionEnum.TRANSACTION_ID +" INTEGER NOT NULL, "
                    + RepeatTransactionEnum.REPEAT_DATE +" TEXT NOT NULL, "
                    + RepeatTransactionEnum.AMOUNT +" INTEGER NOT NULL "
                    +")";


    /********* SQL QUERIES *********/


    public DBConnect(){
        super(ThisApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("DBConnect instantiated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTransactionTable);
        db.execSQL(createCategoryTable);
        db.execSQL(createTotalTransactionTable);
        db.execSQL(createRepeatDaysTable);
        db.execSQL(createItemRepeatsTable);
        System.out.println("DB CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TransactionEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CategoryEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TotalTransactionEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ RepeatDaysEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ RepeatTransactionEnum.TABLE_NAME);
        System.out.println("Upgraded dropping table...");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TransactionEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CategoryEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TotalTransactionEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ RepeatDaysEnum.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ RepeatTransactionEnum.TABLE_NAME);
        System.out.println("Downgrading dropping table...");
        onCreate(db);
    }




    /* VIEW SQL SPECIFIC FUNCTION */
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
    /* VIEW SQL SPECIFIC FUNCTION */
}
