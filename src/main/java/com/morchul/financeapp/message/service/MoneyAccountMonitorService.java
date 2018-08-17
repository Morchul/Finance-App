package com.morchul.financeapp.message.service;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.message.monitor.TransactionMonitor;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

public class MoneyAccountMonitorService implements TransactionMonitor{

    private MoneyAccountInterface moneyAccount;
    private MessageBoard board;
    private FinanceAppApplication app;
    private FinanceAppMessage goesBankruptMessage;
    private int daysUntilBankrupt;
    private boolean pinned = false;
    private TransactionFilter filter;
    private TransactionFilter filterForBankruptTransactions;
    private List<Transaction> bankruptTransactions; //TODO find transaction who bankrupt money account

    public MoneyAccountMonitorService(MoneyAccountInterface m, MessageBoard board, FinanceAppApplication app) {
        this.moneyAccount = m;
        this.board = board;
        this.app = app;
        filter = new TransactionFilter("");
        filter.filterMoneyAccount(m, true, true);
        filterForBankruptTransactions = new TransactionFilter("");
        filterForBankruptTransactions.filterMoneyAccount(m,true, false);

        goesBankruptMessage = createGoesBankruptMessage();
        if((daysUntilBankrupt = daysUntilBankrupt(app.getSettings().getLookDaysInFuture())) != -1){
            pinMessage();
        }
    }

    private void pinMessage(){
        unpinMessage();
        filterForBankruptTransactions.filterDate(LocalDate.now().plusDays(daysUntilBankrupt), LocalDate.now().plusDays(daysUntilBankrupt + 1),null, false);
        bankruptTransactions = filterForBankruptTransactions.filterAndGetList(app.getData().getTransactions());
        goesBankruptMessage = createGoesBankruptMessage();
        board.pinMessage(goesBankruptMessage);
        pinned = true;

    }
    private void unpinMessage(){
        if(pinned){
            board.unpinMessage(goesBankruptMessage);
            pinned = false;
        }
    }

    private int daysUntilBankrupt(int lookingForDays){
        for(int i = 0; i <= lookingForDays; i++){
            if(app.getData().utils.getTotalMoney(LocalDate.MIN,LocalDate.now().plusDays(i), false, filter, 0) < 0)
                return i;
        }
        return -1;
    }

    @Override
    public void transactionChanged() {
        if((daysUntilBankrupt = daysUntilBankrupt(app.getSettings().getLookDaysInFuture())) != -1){
            pinMessage();
        } else {
            unpinMessage();
        }
    }

    @Override
    public void transactionApproved(Transaction transaction) {

    }

    private FinanceAppMessage createGoesBankruptMessage(){
        return new FinanceAppMessage() {

            @Override
            public Type getType() {
                return Type.MONEY_ACCOUNT_GOES_BANKRUPT;
            }

            @Override
            public boolean hasTransactions(){return true;}

            @Override
            public List<Transaction> getTransactions(){return bankruptTransactions;}

            @Override
            public String getMessageText() {
                return app.getLanguage().getString("MoneyAccount") + " " + moneyAccount.getName() + " "+ app.getLanguage().getString("GoesBankruptIn")+ " " + daysUntilBankrupt + " "+app.getLanguage().getString("Days")+"!";
            }
        };
    }
}
