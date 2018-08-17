package com.morchul.financeapp.filter;

import com.morchul.financeapp.transaction.Transaction;
import org.json.JSONObject;

import java.util.List;

public interface TransactionFilterInterface {
    List<Transaction> filter(List<Transaction> transactions);
    void addFilterToJSONObject(JSONObject json);
}
