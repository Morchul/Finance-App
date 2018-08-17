package com.morchul.financeapp.filter;

import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.utilclasses.FilteredTransaction;
import com.morchul.financeapp.transaction.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    private List<TransactionFilterInterface> filters;
    private String UUID;
    private String name;
    private boolean group = false, date = false, account = false;

    public TransactionFilter(String name) {
        this(java.util.UUID.randomUUID().toString(), name);
    }

    public TransactionFilter(String UUID, String name){
        filters = new ArrayList<>();
        this.UUID = UUID;
        this.name = name;
    }

    public boolean hasDateFilter(){return date;}
    public boolean hasGroupFilter(){return group;}
    public boolean hasMoneyAccountFilter(){return account;}

    public String getName(){
        return name;
    }

    public String getUUID() {
        return UUID;
    }

    public List<TransactionFilterInterface> getFilter(){
        return filters;
    }

    public <T extends TransactionFilterInterface> List<T> getFilter(Class<T> fi){
        List<T> res = new ArrayList<>();
        for(TransactionFilterInterface f : getFilter()){
            if(fi.isInstance(f)){
                res.add((T)f);
            }
        }
        return res;
    }

    public void filterGroup(TransactionGroup g){
        filters.add(new GroupTransactionFilter(g));
        group = true;
    }

    public void filterDate(LocalDate from, LocalDate to, LocalDate pinnedDate, boolean pinned){
        filters.add(new DateTransactionFilter(from, to, pinnedDate, pinned));
        date = true;
    }

    public void filterMoneyAccount(MoneyAccountInterface moneyAccount, boolean from, boolean to){
        filters.add(new MoneyAccountFilter(moneyAccount, from, to));
        account = true;
    }

    public ObservableList<FilteredTransaction> filter(List<Transaction> transactions){
        List<Transaction> transactionsRes = transactions;
        for(TransactionFilterInterface filter: filters){
            transactionsRes = filter.filter(transactionsRes);
        }

        ObservableList<FilteredTransaction> res = FXCollections.observableArrayList();
        for(Transaction t : transactionsRes){
            res.add(new FilteredTransaction(t));
        }
        return res;
    }

    public List<Transaction> filterAndGetList(List<Transaction> transactions){
        List<Transaction> transactionsRes = transactions;
        for(TransactionFilterInterface filter: filters){
            transactionsRes = filter.filter(transactionsRes);
        }

        return transactionsRes;
    }
}
