package com.samirvora.myfinance.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Transaction;
import com.samirvora.myfinance.presenter.presenters.presenter_interfaces.HomePresenter;
import com.samirvora.myfinance.presenter.presenters.HomePresenterImpl;
import com.samirvora.myfinance.presenter.adapters.TransactionListViewAdapter;
import com.samirvora.myfinance.util.CurrencyHelper;
import com.samirvora.myfinance.view.activities.activity_interface.HomeView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeView<Object>{

    private Context mCtx;
    private Toolbar mToolbar;

    /** VIEWS **/
    private TextView mMonthV;
    private TextView mYearV;
    private TextView mTotalExpense;
    private TextView mTotalIncome;

    //NEED TO CHANGE THIS TO BUTTON
    private ImageButton mNextMonthButton;
    private ImageButton mPreviousMonthButton;

    private ListView mListView;
    private TransactionListViewAdapter mLVAdapter;

    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mCtx = this;

        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);

        this.mMonthV = (TextView)findViewById(R.id.currentMonth);
        this.mYearV = (TextView)findViewById(R.id.currentYear);

        this.mNextMonthButton = (ImageButton)findViewById(R.id.nextMonth);
        this.mPreviousMonthButton = (ImageButton)findViewById(R.id.previousMonth);

        this.mTotalExpense = (TextView)findViewById(R.id.totalMonthExp);
        this.mTotalIncome = (TextView)findViewById(R.id.totalMonthInc);

        this.mListView = (ListView)findViewById(R.id.itemListView);
        this.mLVAdapter = new TransactionListViewAdapter(this);
        mListView.setAdapter(mLVAdapter);

        homePresenter = new HomePresenterImpl(this);
        homePresenter.displayCurrentMonthYear();


        /** LISTENERS **/
        mListView.setOnItemClickListener(listVItemClickLitener);

        mNextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onNextMonthClick();
            }
        });
        mPreviousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onPreviousMonthClick();
            }
        });
    }




    /** LISTENERS **/

    private AdapterView.OnItemClickListener listVItemClickLitener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Transaction selectedTransaction;

            try{
                selectedTransaction = (Transaction) mLVAdapter.getItem(position);
            }catch (ClassCastException e){
                return;
            }

            Intent intent = new Intent(mCtx, EditTransactionActivity.class);
            intent.putExtra(EditTransactionActivity.TRANS_ID, selectedTransaction.getID());
            intent.putExtra(EditTransactionActivity.TRANS_TYPE, selectedTransaction.getType());
            intent.putExtra(EditTransactionActivity.TRANS_TITLE, selectedTransaction.getTitle());
            intent.putExtra(EditTransactionActivity.TRANS_AMOUNT, CurrencyHelper.convertIntToCurrencyString(selectedTransaction.getAmount()));

            intent.putExtra(EditTransactionActivity.TRANS_CATEGORY_ID, selectedTransaction.getCategoryID());
            intent.putExtra(EditTransactionActivity.TRANS_DATE_POSTED, selectedTransaction.getDatePosted());


            intent.putExtra(EditTransactionActivity.TRANS_CATEGORY_IMG, selectedTransaction.getCategoryImg());
            intent.putExtra(EditTransactionActivity.TRANS_CATEGORY_NAME, selectedTransaction.getCategoryName());
            intent.putExtra(EditTransactionActivity.TRANS_IS_REPEAT, selectedTransaction.isRepeat());
            intent.putExtra(EditTransactionActivity.TRANS_REPEAT_END_DATE, selectedTransaction.getRepeatEndDate());
            startActivity(intent);
        }
    };

    /** LISTENERS **/





    /** SETTERS **/

    @Override
    public void setListAdapterData(List<Object> dataList){
        mLVAdapter.clearList();
        mLVAdapter.setData(dataList);
    }

    @Override
    public void setTotalExpense(String expense) {
        mTotalExpense.setText(expense);
    }

    @Override
    public void setTotalIncome(String income) {
        mTotalIncome.setText(income);
    }

    @Override
    public void setCurrentMonth(String month) {
        mMonthV.setText(month);
    }

    @Override
    public void setCurrentYear(String year) {
        mYearV.setText(year);
    }

    @Override
    public void setEnableNextMonthNavButton(boolean isEnable){
        this.mNextMonthButton.setEnabled(isEnable);
    }

    /** SETTERS **/



    /** GETTERS **/

    @Override
    public String getCurrentMonth() {
        return mMonthV.getText().toString();
    }

    @Override
    public String getCurrentYear() {
        return mYearV.getText().toString();
    }

    /** GETTERS **/


    public void clearListAdapterData(){
        mLVAdapter.clearList();
    }



    /****** ACTION BAR ******/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_header_toolbar, menu);
        return true;
    }

    //This helps to determine the action for the buttons on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.sortIcon://SORT BUTTON CLICK
                final PopupMenu sortMenu = new PopupMenu(mCtx, findViewById(R.id.sortIcon));
                MenuInflater menuInflater = sortMenu.getMenuInflater();
                menuInflater.inflate(R.menu.sort_menu, sortMenu.getMenu());

                sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        homePresenter.sortListView(item.getItemId());
                        return true;
                    }
                });

                MenuItem menuItem;
                switch (homePresenter.getCurrentSortID()){
                    case R.id.sortAlphaAscOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortAlphaAscOpt);
                        break;
                    case R.id.sortAlphaDescOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortAlphaDescOpt);
                        break;
                    case R.id.sortDateAscOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortDateAscOpt);
                        break;
                    case R.id.sortDateDescOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortDateDescOpt);
                        break;
                    case R.id.sortPriceAscOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortPriceAscOpt);
                        break;
                    case R.id.sortPriceDescOpt:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortPriceDescOpt);
                        break;
                    default:
                        menuItem = sortMenu.getMenu().findItem(R.id.sortDateDescOpt);
                }

                menuItem.setChecked(true);

                sortMenu.show();
                return true;
            case R.id.menu_addTransaction:
                Intent intent = new Intent(this, AddTransactionActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /****** ACTION BAR ******/


}
