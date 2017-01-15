package com.samirvora.myfinance.presenter.helper;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by James on 26/11/2016.
 */
public class SortResult {

    private List<Transaction> mResult;

    public SortResult(){

    }

    public List<Transaction> sortAlphaAsc(List<Transaction> transactions){
        return sort(transactions,
                new Comparator<Transaction>() {
                    public int compare(Transaction firstTrans, Transaction secondTrans) {
                        return secondTrans.getTitle().compareTo(firstTrans.getTitle());
                    }
                }
        );
    }


    public List<Transaction> sortAlphaDesc(List<Transaction> transactions){
        return sort(transactions,
                new Comparator<Transaction>() {
                    public int compare(Transaction firstTrans, Transaction secondTrans) {
                        return firstTrans.getTitle().compareTo(secondTrans.getTitle());
                    }
                }
        );
    }


    public List<Transaction> sortPriceAsc(List<Transaction> transactions){
        return sort(transactions,
                new Comparator<Transaction>() {
                    public int compare(Transaction firstTrans, Transaction secondTrans) {
                        if(firstTrans.getAmount() < secondTrans.getAmount()){
                            return -1;
                        }else if(firstTrans.getAmount() > secondTrans.getAmount()){
                            return 1;
                        }else if(firstTrans.getAmount() == secondTrans.getAmount()){
                            return 0;
                        }
                        return 0;
                    }
                }
        );
    }


    public List<Transaction> sortPriceDesc(List<Transaction> transactions){
        return sort(transactions,
                new Comparator<Transaction>() {
                    public int compare(Transaction firstTrans, Transaction secondTrans) {
                        if(secondTrans.getAmount() < firstTrans.getAmount()){
                            return -1;
                        }else if(secondTrans.getAmount() > firstTrans.getAmount()){
                            return 1;
                        }else if(secondTrans.getAmount() == firstTrans.getAmount()){
                            return 0;
                        }
                        return 0;
                    }
                }
        );
    }


    public List<Transaction> sortDateAsc(List<Transaction> dataList){
        Collections.sort(dataList, new Comparator<Transaction>() {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.UK);

            public int compare(Transaction firstTrans, Transaction secondTrans) {

                try{

                    Date firstDate = dateFormat.parse(firstTrans.getDatePosted());
                    Date secondDate = dateFormat.parse(secondTrans.getDatePosted());
                    return firstDate.compareTo(secondDate);

                }catch (ParseException e){
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return dataList;
    }


    public List<Transaction> sortDateDesc(List<Transaction> dataList){
        Collections.sort(dataList, new Comparator<Transaction>() {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DB_DATE_FORMAT, Locale.UK);

            public int compare(Transaction firstTrans, Transaction secondTrans) {

                try{

                    Date firstDate = dateFormat.parse(firstTrans.getDatePosted());
                    Date secondDate = dateFormat.parse(secondTrans.getDatePosted());
                    return secondDate.compareTo(firstDate);

                }catch (ParseException e){
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return dataList;
    }




    private List<Transaction> sort(List<Transaction> list, Comparator sortComparator){
        mResult = new ArrayList<>();
        int counterIndex = 0;

        //Allways sort the list in date Asc
        list = sortDateAsc(list);

        while(counterIndex < list.size()){

            ArrayList<Transaction> tempList = new ArrayList<>();
            String date = list.get(counterIndex).getDatePosted();

            for (int i = 0; i < list.size(); i++) {

                Transaction currentTransaction = list.get(i);

                if(currentTransaction.getDatePosted().equals(date)){
                    tempList.add(currentTransaction);
                    counterIndex++;
                }

            }

            Collections.sort(tempList, sortComparator);

            mResult.addAll(tempList);
        }

        return mResult;
    }













}
