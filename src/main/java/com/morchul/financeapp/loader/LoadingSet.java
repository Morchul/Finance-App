package com.morchul.financeapp.loader;

import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.util.List;

public class LoadingSet {
    
    public List<Transaction> transactions;
    public List<TransactionGroup> groups;
    public List<MoneyAccountInterface> accounts;
    public List<MoneyAccountInterface> externalAccounts;
    public List<TransactionFilter> filters;

    public LoadingSet(List<TransactionGroup> groups, List<MoneyAccountInterface> accounts, List<MoneyAccountInterface> externalAccounts, List<TransactionFilter> filters,List<Transaction> transactions) {
        this.transactions = transactions;
        this.groups = groups;
        this.accounts = accounts;
        this.externalAccounts = externalAccounts;
        this.filters = filters;
    }
}
