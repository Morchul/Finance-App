package com.morchul.financeapp.ui;

import com.morchul.financeapp.settings.Language;

import java.time.LocalDate;

import static com.morchul.financeapp.Constants.dateFormatter;

public class TextDisplayHelper {



    public static String showDate(LocalDate date){
        Language l = Language.getInstance();
        if(date.plusDays(1).equals(LocalDate.now())){
            return l.getString("Yesterday");
        } else if(date.minusDays(1).equals(LocalDate.now())){
            return l.getString("Tomorrow");
        } else if(date.equals(LocalDate.now())){
            return l.getString("Today");
        } else {
            return date.format(dateFormatter);
        }
    }

    public static float roundNumber(float number, int scale){
        int pow = 10;
        for(int i = 1; i < scale; i++){
            pow *= 10;
        }
        float tmp = number * pow;
        return (tmp > 0) ?
                (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow :
                (float) (int) ((tmp - (int) tmp) <= -0.5f ? tmp - 1 : tmp) / pow;


    }
}
