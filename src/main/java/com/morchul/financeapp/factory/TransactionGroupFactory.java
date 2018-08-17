package com.morchul.financeapp.factory;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.transaction.group.SimpleTransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroup;

public class TransactionGroupFactory {

    private FinanceAppApplication app;

    public TransactionGroupFactory(FinanceAppApplication app) {
        this.app = app;
    }

    public TransactionGroup createTransactionGroup(String name, float amount){
        TransactionGroup g = new SimpleTransactionGroup(name, amount);
        app.getFactory().monitorFactory.createTransactionGroupMonitor(g, app.getMessageBoard());
        g.setSupervisor(app.getGroupMoneySupervisor());
        return g;
    }

    public TransactionGroup createTransactionGroup(String name, Float amount, String UUID){
        TransactionGroup g = new SimpleTransactionGroup(name, amount, UUID);
        app.getFactory().monitorFactory.createTransactionGroupMonitor(g, app.getMessageBoard());
        g.setSupervisor(app.getGroupMoneySupervisor());
        return g;
    }

    public TransactionGroup createTransactionGroupPlaceHolder(String UUID){
        return new SimpleTransactionGroup("",0, UUID);
    }
}
