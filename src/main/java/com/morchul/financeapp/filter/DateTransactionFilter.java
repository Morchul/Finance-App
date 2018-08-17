package com.morchul.financeapp.filter;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.transaction.Transaction;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.morchul.financeapp.filter.FilterConverter.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class DateTransactionFilter implements TransactionFilterInterface {

    private LocalDate from;
    private LocalDate to;
    private LocalDate pinnedDate;
    private boolean pinned;

    public DateTransactionFilter(LocalDate from, LocalDate to, LocalDate pinnedDate, boolean pinned) {
        this.from = from;
        this.to = to;
        this.pinned = pinned;
        this.pinnedDate = pinnedDate;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();

        long daysFrom = pinnedDate == null ? 0 : DAYS.between(this.pinnedDate, this.from);
        long daysTo = pinnedDate == null ? 0 : DAYS.between(this.pinnedDate, this.to);
        LocalDate from = pinned ? LocalDate.now().plusDays(daysFrom) : this.from;
        LocalDate to = pinned ? LocalDate.now().plusDays(daysTo): this.to;

        for(Transaction t : transactions){
            if(!t.getDate().isBefore(from) && !t.getDate().isAfter(to))
                result.add(t);
        }
        return result;
    }

    @Override
    public void addFilterToJSONObject(JSONObject json) {
        json.put(TRANSACTION_DATE_FILTER_FROM, from.format(Constants.dateFormatter));
        json.put(TRANSACTION_DATE_FILTER_TO, to.format(Constants.dateFormatter));
        json.put(TRANSACTION_DATE_FILTER_PINNED_DATE, pinnedDate.format(Constants.dateFormatter));
        json.put(TRANSACTION_DATE_PINNED, pinned);
    }
}
