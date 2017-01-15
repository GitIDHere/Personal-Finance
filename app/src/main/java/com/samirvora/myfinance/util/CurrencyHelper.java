package com.samirvora.myfinance.util;

import java.text.DecimalFormat;

/**
 * Created by samirv on 07/10/2016.
 */
public class CurrencyHelper {

    public static int covertStringToInt(String amt){
        String clearedAmt = clearCurrencyFormat(amt);
        return (int)(Double.parseDouble(clearedAmt) * 100);
    }

    public static String convertIntToCurrencyString(int amt){
        if(amt == 0)
            return "0.00";

        double doubleAmt = amt / (double)100;
        DecimalFormat formatter = new DecimalFormat("#,###,###,###,###,###,##0.00");
        return formatter.format(doubleAmt);
        //return String.format(Locale.UK, "%.2f", );
    }

    public static String formatStringToCurrencyString(String unfromattedString){
        String userAmt = CurrencyHelper.clearCurrencyFormat(unfromattedString);
        DecimalFormat formatter = new DecimalFormat("#,###,###,###,###,###,###.00");
        return "£"+formatter.format(Double.parseDouble(userAmt));
    }

    public static String clearCurrencyFormat(String amount){
        return amount.replace("£", "").replace(",", "");
    }

}
