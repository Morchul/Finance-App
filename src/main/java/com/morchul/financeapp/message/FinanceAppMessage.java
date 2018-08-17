package com.morchul.financeapp.message;

import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.util.List;

public abstract class FinanceAppMessage {

    public abstract Type getType();
    public Transaction getTransaction(){return null;}
    public List<Transaction> getTransactions(){return null;}
    public TransactionGroup getTransactionGroup(){return null;}
    public MoneyAccountInterface getAccount(){return null;}
    public boolean hasTransaction(){return false;}
    public boolean hasTransactions(){return false;}
    public boolean hasTransactionGroup(){return false;}
    public boolean hasAccount(){return false;}

    public abstract String getMessageText();

    public enum Type {
        TRANSACTION_APPROVE,
        MONEY_ACCOUNT_GOES_BANKRUPT,
        SPEND_TO_MUCH_MONEY,
        GROUP_MONEY_SUPERVISER_REMEMBER
    }
}
