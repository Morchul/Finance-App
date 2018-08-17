package com.morchul.financeapp.factory;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.message.service.MoneyAccountMonitorService;
import com.morchul.financeapp.message.service.TransactionGroupMoneySupervisorService;
import com.morchul.financeapp.message.service.TransactionGroupMonitorService;
import com.morchul.financeapp.message.service.TransactionMonitorService;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroupMoneySupervisor;

public class MonitorFactory {

    private FinanceAppApplication app;

    MonitorFactory(FinanceAppApplication app){
        this.app = app;
    }

    public void createTransactionMonitor(Transaction t, MessageBoard board){
        TransactionMonitorService tr = new TransactionMonitorService(t, board);
        app.getData().monitor.addTransactionMonitor(tr);
    }

    public void createMoneyAccountMonitor(MoneyAccountInterface m, MessageBoard board){
        MoneyAccountMonitorService ma = new MoneyAccountMonitorService(m, board, app);
        app.getData().monitor.addTransactionMonitor(ma);
    }

    public void createTransactionGroupMonitor(TransactionGroup g, MessageBoard board){
        TransactionGroupMonitorService gs = new TransactionGroupMonitorService(g, board, app);
        app.getData().monitor.addTransactionMonitor(gs);
        app.getData().monitor.addTransactionGroupMonitor(gs);
    }

    public void createTransactionGroupMoneySupervisorMonitor(TransactionGroupMoneySupervisor supervisor, MessageBoard board){
        new TransactionGroupMoneySupervisorService(supervisor, board, app);
    }

}
