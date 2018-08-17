package com.morchul.financeapp.message.service;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.message.monitor.TransactionGroupMonitor;
import com.morchul.financeapp.message.monitor.TransactionMonitor;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;

public class TransactionGroupMonitorService implements TransactionMonitor, TransactionGroupMonitor {

    private TransactionGroup transactionGroup;
    private MessageBoard board;
    private FinanceAppApplication app;
    private FinanceAppMessage spendToMuchMoney;
    private TransactionFilter filter;
    private boolean pinned = false;

    public TransactionGroupMonitorService(TransactionGroup g, MessageBoard board, FinanceAppApplication app) {
        this.transactionGroup = g;
        this.board = board;
        this.app = app;
        filter = new TransactionFilter("groupFilter");
        filter.filterGroup(transactionGroup);
        spendToMuchMoney = createSpendToMuchMoneyMessage();
    }


    private void pinMessage(FinanceAppMessage m){
        if(!pinned){
            board.pinMessage(m);
            pinned = true;
        }
    }

    private void unpinMessage(FinanceAppMessage m){
        if(pinned){
            board.unpinMessage(m);
            pinned = false;
        }
    }

    @Override
    public void transactionChanged() {
        if(transactionGroup.getMoney() < 0){
            pinMessage(spendToMuchMoney);
        } else {
            unpinMessage(spendToMuchMoney);
        }
    }

    @Override
    public void transactionApproved(Transaction transaction) {
        if(transactionGroup.getMoney() < 0){
            pinMessage(spendToMuchMoney);
        } else {
            unpinMessage(spendToMuchMoney);
        }
    }

    private FinanceAppMessage createSpendToMuchMoneyMessage(){
        return new FinanceAppMessage() {
            @Override
            public Type getType() {
                return Type.SPEND_TO_MUCH_MONEY;
            }

            @Override
            public String getMessageText() {
                return app.getLanguage().getString("YouSpendToMuchMoneyThisMonth") + " " + transactionGroup.getName();
            }
        };
    }

    @Override
    public void groupChanged() {
        if(transactionGroup.getMoney() < 0){
            pinMessage(spendToMuchMoney);
        } else {
            unpinMessage(spendToMuchMoney);
        }
    }
}
