package com.morchul.financeapp.factory;

import com.morchul.financeapp.FinanceAppApplication;

public class Factory {

    public MonitorFactory monitorFactory;
    public TransactionFactory transactionFactory;
    public MoneyAccountFactory moneyAccountFactory;
    public TransactionGroupFactory transactionGroupFactory;

    public Factory(FinanceAppApplication app) {
        monitorFactory = new MonitorFactory(app);
        transactionFactory = new TransactionFactory(app);
        moneyAccountFactory = new MoneyAccountFactory(app);
        transactionGroupFactory = new TransactionGroupFactory(app);
    }
}
