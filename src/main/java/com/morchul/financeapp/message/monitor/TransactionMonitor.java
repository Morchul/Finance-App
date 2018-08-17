package com.morchul.financeapp.message.monitor;


import com.morchul.financeapp.transaction.Transaction;

public interface TransactionMonitor {
    void transactionChanged();
    void transactionApproved(Transaction transaction);
}
