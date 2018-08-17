package com.morchul.financeapp.filter;

import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.morchul.financeapp.filter.FilterConverter.TRANSACTION_MONEY_ACCOUNT_FILTER;

public class MoneyAccountFilter implements TransactionFilterInterface {

    private MoneyAccountInterface moneyAccount;
    private boolean from, to;

    public MoneyAccountFilter(MoneyAccountInterface moneyAccount, boolean from, boolean to) {
        this.moneyAccount = moneyAccount;
        this.from = from;
        this.to = to;
    }

    public MoneyAccountInterface getMoneyAccountFilter(){return moneyAccount;}

    public void setMoneyAccount(MoneyAccountInterface moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        for(Transaction t : transactions){
            if((t.getFrom() != null && t.getFrom().getName().equals(moneyAccount.getName())) && from)
                result.add(t);
            else if((t.getTo() != null && t.getTo().getName().equals(moneyAccount.getName())) && to)
                result.add(t);
        }
        return result;
    }

    @Override
    public void addFilterToJSONObject(JSONObject json) {
        json.put(TRANSACTION_MONEY_ACCOUNT_FILTER, moneyAccount.getUUID());
    }
}
