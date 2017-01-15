package com.samirvora.myfinance.presenter.presenters;


import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.database.DBHelper;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.model.services.TransactionServiceImpl;
import com.samirvora.myfinance.model.tasks.OnTransactionListResponse;
import com.samirvora.myfinance.model.services.service_interface.TransactionService;
import com.samirvora.myfinance.presenter.helper.SortResult;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.HomePresenter;
import com.samirvora.myfinance.view.activities.activity_interface.HomeView;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.util.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by James on 29/10/2016.
 */
public class HomePresenterImpl implements HomePresenter, OnTransactionListResponse<Transaction>{

    private HomeView<Object> homeView;
    private TransactionService transactionService;

    private DateHelper mDateHelper;
    private Calendar mCalendar;

    private Date mCurrentDate;

    private SortResult sortResult;
    private int mCurrentSortID = 0;
    private List<Transaction> mDataList;

    public HomePresenterImpl(HomeView<Object> homeView){
        this.homeView = homeView;

        this.mDateHelper = DateHelper.getInstance();

        this.sortResult = new SortResult();
        this.mDataList = new ArrayList<>();

        this.mCalendar = Calendar.getInstance();
        this.mCalendar.setTime(mDateHelper.getTodaysDateAsDate());

        this.mCurrentDate = mCalendar.getTime();

        this.transactionService = new TransactionServiceImpl(DBHelper.getInstance());

        String minDate = getMinDateInMonth(mCalendar);
        String maxDate = getMaxDateInMonth(mCalendar);
        this.transactionService.getTransactionList(this, minDate, maxDate);

        homeView.setEnableNextMonthNavButton(false);
    }





    @Override
    public void onTransactionListCreateResponse(List<Transaction> output, int totalExpense, int totalIncome) {

        mDataList.clear();
        homeView.clearListAdapterData();

        homeView.setTotalExpense(CurrencyHelper.convertIntToCurrencyString(totalExpense));
        homeView.setTotalIncome(CurrencyHelper.convertIntToCurrencyString(totalIncome));

        if(output == null)
            return;

        mDataList.addAll(output);
        homeView.setListAdapterData(createAdapterData(sortDataList(mCurrentSortID, output)));
    }


    /** DATE NAVIGATION **/
    @Override
    public void onPreviousMonthClick(){
        mCalendar.add(Calendar.MONTH, -1);

        String month = getCurrentMonthDisplayName(mCalendar);

        displayMonthYear(month, getCurrentYearString());

        this.transactionService.getTransactionList(this, getMinDateInMonth(mCalendar), getMaxDateInMonth(mCalendar));

        homeView.setEnableNextMonthNavButton(true);
    }


    @Override
    public void onNextMonthClick(){
        Date nextMonthCal = mCalendar.getTime();

        if(nextMonthCal.before(mCurrentDate)){

            mCalendar.add(Calendar.MONTH, 1);

            String month = getCurrentMonthDisplayName(mCalendar);
            String year = getCurrentYearString();

            displayMonthYear(month, year);

            nextMonthCal =  mCalendar.getTime();

            if(!nextMonthCal.before(mCurrentDate))
                homeView.setEnableNextMonthNavButton(false);

            this.transactionService.getTransactionList(this, getMinDateInMonth(mCalendar), getMaxDateInMonth(mCalendar));
        }else{
            homeView.setEnableNextMonthNavButton(false);
        }

    }

    @Override
    public void displayMonthYear(String month, String year) {
        homeView.setCurrentMonth(month);
        homeView.setCurrentYear(year);
    }

    @Override
    public void displayCurrentMonthYear() {
        homeView.setCurrentMonth(getCurrentMonthDisplayName(mCalendar));
        homeView.setCurrentYear(getCurrentYearString());
    }
    /** DATE NAVIGATION **/


    @Override
    public void sortListView(int itemID){
        this.mCurrentSortID = itemID;
        homeView.setListAdapterData(createAdapterData(sortDataList(mCurrentSortID, mDataList)));
    }




    private List<Transaction> sortDataList(int sortItemID, List<Transaction> data){

        switch (sortItemID){
            case R.id.sortAlphaAscOpt:
                return sortResult.sortAlphaAsc(data);

            case R.id.sortAlphaDescOpt:
                return sortResult.sortAlphaDesc(data);

            case R.id.sortDateAscOpt:
                return sortResult.sortDateAsc(data);

            case R.id.sortDateDescOpt:
                return sortResult.sortDateDesc(data);

            case R.id.sortPriceAscOpt:
                return sortResult.sortPriceAsc(data);

            case R.id.sortPriceDescOpt:
                return sortResult.sortPriceDesc(data);

            default:
                return sortResult.sortDateDesc(data);
        }

    }



    public int getCurrentSortID(){
        return mCurrentSortID;
    }




    /** HEPLERS **/
    private String getCurrentMonthDisplayName(Calendar calendar){
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK);
    }

    private String getCurrentYearString(){
        return Integer.toString(mCalendar.get(Calendar.YEAR));
    }

    private String getMinDateInMonth(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return mDateHelper.getDateFromCalendar(calendar);
    }

    private String getMaxDateInMonth(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return mDateHelper.getDateFromCalendar(calendar);
    }


    private List<Object> createAdapterData(List<Transaction> dataList) {

        if(dataList == null)
            return null;

        List<Object> result = new ArrayList<>();

        String date = "";

        if(dataList.size() > 0){

            for (int i = 0; i < dataList.size(); i++) {

                Transaction currentTransaction = dataList.get(i);
                String datePosted = currentTransaction.getDatePosted();

                if(!datePosted.equals(date)){
                    date = datePosted;
                    result.add(formatDateToHeaderString(currentTransaction.getDatePosted()));
                }

                result.add(currentTransaction);
            }

        }

        return result;
    }

    private String formatDateToHeaderString(String date){
        StringBuilder sb = new StringBuilder();

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.mDateHelper.getDateFromString(date));

        sb.append(String.format(Locale.UK, "%02d", cal.get(Calendar.DAY_OF_MONTH)));
        sb.append(" / ");
        sb.append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK));
        return sb.toString();
    }


}
