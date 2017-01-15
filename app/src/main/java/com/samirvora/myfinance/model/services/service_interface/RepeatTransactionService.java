package com.samirvora.myfinance.model.services.service_interface;

import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatDays;
import com.samirvora.myfinance.model.pojos.base_pojo_interface.RepeatTransaction;

/**
 * Created by James on 06/11/2016.
 */
public interface RepeatTransactionService {

    RepeatDays getRepeatDaysByTransID(long transID);

    int updateRepeatDays(RepeatDays repeatDays);

    boolean deleteRepeatTransactionByTransID(long transactionID);
    boolean deleteRepeatDays(long repeatDaysID);

    long insertRepeatDaysRow(RepeatDays repeatDays);

    long insertRepeatTransactionRow(RepeatTransaction repeatTransaction);

    boolean removeRepeatDays(long repeadDaysRowID);

}
