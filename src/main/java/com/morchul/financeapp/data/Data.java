package com.morchul.financeapp.data;

import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Data {

    private ObservableList<TransactionGroup> groups;
    private ObservableList<Transaction> transactions;
    private ObservableList<MoneyAccountInterface> accounts;
    private ObservableList<MoneyAccountInterface> externalAccounts;
    private ObservableList<TransactionFilter> filters;
    public DataUtils utils;
    public DataMonitor monitor;

    public Data() {
        groups = FXCollections.observableArrayList();
        transactions = FXCollections.observableArrayList();
        accounts = FXCollections.observableArrayList();
        externalAccounts = FXCollections.observableArrayList();
        filters = FXCollections.observableArrayList();
        utils = new DataUtils(this);
        monitor = new DataMonitor(this);
    }

    public ObservableList<MoneyAccountInterface> getAccounts() { return accounts; }

    public ObservableList<TransactionGroup> getGroups() {
        return groups;
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public ObservableList<MoneyAccountInterface> getExternalAccounts() {
        return externalAccounts;
    }

    public ObservableList<TransactionFilter> getFilters() {
        return filters;
    }

    public void setAccounts(ObservableList<MoneyAccountInterface> accounts){this.accounts = accounts;}

    public void setExternalAccounts(ObservableList<MoneyAccountInterface> externalAccounts) {
        this.externalAccounts = externalAccounts;
    }

    public void setGroups(ObservableList<TransactionGroup> groups){
        this.groups = groups;
    }

    public void setTransactions(ObservableList<Transaction> transactions){
        this.transactions = transactions;
    }

    public void removeTransaction(Transaction transaction, boolean toNext, boolean toPrevious){
//        Transaction t = transaction;
//        while(toNext && t.hasNext()){
//            transactions.remove(t.getNext());
//            t = t.getNext();
//        }
//        t = transaction;
//        while(toPrevious && t.hasPrevious()){
//            transactions.remove(t.getPrevious());
//            t = t.getPrevious();
//        }

        transaction.delete(toNext, toPrevious);
//        transactions.remove(transaction);
    }

    public void removeFilter(TransactionFilter filter){
        filters.remove(filter);
    }
    public void removeGroup(TransactionGroup group){
        group.delete();
        groups.remove(group);
    }
    public void removeAccount(MoneyAccountInterface account){ accounts.remove(account); }
    public void removeExternalAccount(MoneyAccountInterface account){externalAccounts.remove(account);}
    public void addFilter(TransactionFilter filter){filters.add(filter);}
    public void addGroup(TransactionGroup group){ groups.add(group); }
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        transaction.setData(this);
    }
    public void addAllTransaction(List<Transaction> transactions){
        for(Transaction t : transactions){
            t.setData(this);
            this.transactions.add(t);
        }
    }
    public void addAccount(MoneyAccountInterface account){ accounts.add(account); }
    public void addExternalAccount(MoneyAccountInterface account){externalAccounts.add(account);}
}
