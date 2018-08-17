package com.morchul.financeapp.filter;

import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.morchul.financeapp.filter.FilterConverter.TRANSACTION_GROUP_FILTER;

public class GroupTransactionFilter implements TransactionFilterInterface {

    private TransactionGroup group;

    public GroupTransactionFilter(TransactionGroup group) { this.group = group; }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        for(Transaction t : transactions){
            if(t.getGroup() != null && t.getGroup().getName().equals(group.getName()))
                result.add(t);
        }
        return result;
    }

    public TransactionGroup getGroup() { return group; }

    public void setGroup(TransactionGroup group) { this.group = group; }

    @Override
    public void addFilterToJSONObject(JSONObject json) {
        json.put(TRANSACTION_GROUP_FILTER, group.getUUID());
    }
}
