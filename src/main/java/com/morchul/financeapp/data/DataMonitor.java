package com.morchul.financeapp.data;

import com.morchul.financeapp.message.monitor.MoneyAccountMonitor;
import com.morchul.financeapp.message.monitor.TransactionGroupMonitor;
import com.morchul.financeapp.message.monitor.TransactionMonitor;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import javafx.collections.ListChangeListener;

import java.util.ArrayList;
import java.util.List;

public class DataMonitor {

    private List<TransactionMonitor> transactionMonitor;
    private List<TransactionGroupMonitor> groupMonitor;
    private List<MoneyAccountMonitor> accountMonitor;

    public DataMonitor(Data data) {

        transactionMonitor = new ArrayList<>();
        groupMonitor = new ArrayList<>();
        accountMonitor = new ArrayList<>();

        data.getTransactions().addListener((ListChangeListener<Transaction>) c -> {
            c.next();
            for(Transaction t : c.getAddedSubList()){
                t.setHandler(() -> {
                    for(TransactionMonitor m : transactionMonitor)
                        m.transactionApproved(t);
                });
            }
            transactionChangedEvent();
        });
        data.getGroups().addListener((ListChangeListener<TransactionGroup>) c -> groupChangedEvent());
        data.getAccounts().addListener((ListChangeListener<MoneyAccountInterface>) c -> accountChangedEvent());
    }

    public void dataChangedEvent(){
        transactionChangedEvent();
        groupChangedEvent();
        accountChangedEvent();
    }

    public void transactionChangedEvent(){
        for(TransactionMonitor monitor: transactionMonitor){
            monitor.transactionChanged();
        }
    }

    public void groupChangedEvent(){
        for(TransactionGroupMonitor monitor: groupMonitor){
            monitor.groupChanged();
        }
    }
    public void accountChangedEvent(){
        for(MoneyAccountMonitor monitor: accountMonitor){
            monitor.moneyAccountChanged();
        }
    }

    public void addTransactionMonitor(TransactionMonitor monitor){
        transactionMonitor.add(monitor);
    }
    public void addTransactionGroupMonitor(TransactionGroupMonitor monitor){
        groupMonitor.add(monitor);
    }
    public void addMoneyAccountMonitor(MoneyAccountMonitor monitor){
        accountMonitor.add(monitor);
    }
}
