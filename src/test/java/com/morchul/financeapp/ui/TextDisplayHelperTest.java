package com.morchul.financeapp.ui;

import com.morchul.financeapp.settings.Language;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.Assert.*;

public class TextDisplayHelperTest {


    @Before
    public void init(){
        Language l = Language.getInstance();
        l.changeLanguage(Locale.US);
    }

    @Test
    public void showDateTest(){
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();

        assertEquals("Tomorrow", TextDisplayHelper.showDate(tomorrow));
        assertEquals("Yesterday", TextDisplayHelper.showDate(yesterday));
        assertEquals("Today", TextDisplayHelper.showDate(today));
    }

    @Test
    public void roundNumberTest(){
        assertEquals(-10.3f, TextDisplayHelper.roundNumber(-10.26f, 1), 0);
        assertEquals(-10.2f, TextDisplayHelper.roundNumber(-10.22f, 1), 0);
        assertEquals(3.249f, TextDisplayHelper.roundNumber(3.2486f, 3), 0);
        assertEquals(4.241f, TextDisplayHelper.roundNumber(4.2414f, 3), 0);
    }
}