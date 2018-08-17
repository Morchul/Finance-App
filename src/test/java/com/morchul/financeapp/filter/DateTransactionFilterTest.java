package com.morchul.financeapp.filter;

import com.morchul.financeapp.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DateTransactionFilterTest {

    private List<Transaction> transactions;

    @Before
    public void init(){
        transactions = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            LocalDate date = LocalDate.now().plusDays(i);
            transactions.add(new Transaction(date, 0, null, true, "", "", null ,null));
        }
    }

    @Test
    public void testFilter(){
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(5);
        LocalDate pinnedDate = from.plusDays(4);
        DateTransactionFilter filter = new DateTransactionFilter(from, to, null, false);
        DateTransactionFilter filter2 = new DateTransactionFilter(from, to, pinnedDate, true);

        List<Transaction> res = filter.filter(transactions);
        List<Transaction> res2 = filter2.filter(transactions);
        assertEquals(res.size(), 6);
        assertEquals(res2.size(), 2);
    }


}