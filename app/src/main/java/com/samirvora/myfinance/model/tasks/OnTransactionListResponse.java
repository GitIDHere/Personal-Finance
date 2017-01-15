package com.samirvora.myfinance.model.tasks;

import java.util.List;

/**
 * Created by James on 30/10/2016.
 */
public interface OnTransactionListResponse<T> {
        void onTransactionListCreateResponse(List<T> output, int totalExpense, int totalIncome);
}
