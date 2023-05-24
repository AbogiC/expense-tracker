package com.personal.expensetracker.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseManagerUtil {
    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(date);
    }

    public static String convertAmountToCurrency(Long amount) {
        StringBuilder output = new StringBuilder();
        String amountString = String.valueOf(amount);
        int totalChar = amountString.length();
        int countPosition = 0;

        if (totalChar % 3 == 0) {
            for (int i = 0; i < totalChar / 3; i++) {
                output.append(amountString.substring(countPosition, countPosition + 3));
                countPosition += 3;
                if(countPosition != totalChar) {
                    output.append(".");
                }
            }
        } else {
            output.append(amountString.substring(countPosition, countPosition + totalChar % 3));
            output.append(".");
            countPosition = totalChar % 3;
            for (int i = 0; i < totalChar / 3; i++) {
                output.append(amountString.substring(countPosition, countPosition + 3));
                countPosition += 3;
                if(countPosition != totalChar) {
                    output.append(".");
                }
            }
        }

        return output.toString();
    }
}
