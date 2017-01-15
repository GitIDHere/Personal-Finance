package com.samirvora.myfinance.model.enums;

/**
 * Created by samirv on 25/08/2016.
 */
public interface TransactionEnum {

    public static final String TABLE_NAME = "Transactions";

    public static final String ID = "transaction_id"; // 0
    public static final String TITLE = "title"; // 1
    public static final String AMOUNT = "amount"; // 2
    public static final String CATEGORY = "category"; // 3
    public static final String DATE_POSTED = "date_posted"; // 4
    public static final String TYPE = "type"; // 5

    public static final String TYPE_EXPENSE = "Expense";
    public static final String TYPE_INCOME = "Income";

}
