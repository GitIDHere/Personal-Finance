package com.samirvora.myfinance.model.services;

import com.samirvora.myfinance.R;
import com.samirvora.myfinance.model.daos.CategoryDaoImpl;
import com.samirvora.myfinance.model.daos.dao_interfaces.CategoryDao;
import com.samirvora.myfinance.model.database.DatabaseCrud;
import com.samirvora.myfinance.model.pojos.CategoryImpl;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.Category;
import com.samirvora.myfinance.model.services.service_interface.CategoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 05/11/2016.
 */
public class CategoryServiceImpl implements CategoryService<Category> {

    private List<Category> categories;

    private DatabaseCrud mDBHelper;

    private CategoryDao<Category> categoryDao;

    public CategoryServiceImpl(DatabaseCrud dbConnection){

        mDBHelper = dbConnection;

        categoryDao = new CategoryDaoImpl(mDBHelper);
    }


    @Override
    public long insertCategory(Category category) {
        return categoryDao.createRow(category);
    }

    @Override
    public List<Category> getExpenseCategoryData() {
        prepareExpenseList();
        return categories;
    }

    @Override
    public List<Category> getIncomeCategoryData() {
        prepareIncomeList();
        return categories;
    }


    private void prepareExpenseList(){
        categories = new ArrayList<>();

        CategoryImpl categoryImpl = new CategoryImpl("Bill", R.drawable.icon_category_bill);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Car", R.drawable.icon_category_car);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Car Wash", R.drawable.icon_category_car_wash);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Children", R.drawable.icon_category_children);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Clothes", R.drawable.icon_category_clothes);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Commute", R.drawable.icon_category_commute);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Cosmetic", R.drawable.icon_category_cosmetic);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Electronic", R.drawable.icon_category_electronic);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Leisure", R.drawable.icon_category_entertainment);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Food", R.drawable.icon_category_food);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Fuel", R.drawable.icon_category_fuel);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Gift", R.drawable.icon_category_gift);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Health", R.drawable.icon_category_health);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Holiday", R.drawable.icon_category_holiday);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Home", R.drawable.icon_category_home);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Insurance", R.drawable.icon_category_insurance);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Internet", R.drawable.icon_category_internet);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Jewellery", R.drawable.icon_category_jewellery);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("License", R.drawable.icon_category_license);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Pets", R.drawable.icon_category_pets);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Rent", R.drawable.icon_category_rent);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Repair", R.drawable.icon_category_repair);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Shopping", R.drawable.icon_category_shopping);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Sport", R.drawable.icon_category_sport);
        categories.add(categoryImpl);
    }

    private void prepareIncomeList() {
        categories = new ArrayList<>();

        CategoryImpl categoryImpl = new CategoryImpl("Bonus", R.drawable.icon_category_bonus);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Business", R.drawable.icon_category_business);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Dividend", R.drawable.icon_category_dividens);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Gambling", R.drawable.icon_category_gambling);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Gift", R.drawable.icon_category_gift);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Interest", R.drawable.icon_category_interest);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Job", R.drawable.icon_category_job);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Pension", R.drawable.icon_category_pension);
        categories.add(categoryImpl);

        categoryImpl = new CategoryImpl("Rent", R.drawable.icon_category_rent);
        categories.add(categoryImpl);

    }

}
