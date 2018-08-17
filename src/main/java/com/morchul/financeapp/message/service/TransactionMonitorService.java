package com.morchul.financeapp.message.service;

import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.message.monitor.TransactionMonitor;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.ui.TextDisplayHelper;

import java.time.LocalDate;

public class TransactionMonitorService implements TransactionMonitor {

    private Transaction transaction;
    private FinanceAppMessage approveMessage;

    public TransactionMonitorService(Transaction t, MessageBoard board) {
        this.transaction = t;
        approveMessage = createApproveMessage();

        if(!transaction.isApproved() && !transaction.getDate().isAfter(LocalDate.now())){
            board.pinMessage(approveMessage);
        }
    }

    private FinanceAppMessage createApproveMessage(){
        return new FinanceAppMessage() {
            @Override
            public FinanceAppMessage.Type getType() {
                return Type.TRANSACTION_APPROVE;
            }

            @Override
            public Transaction getTransaction() { return transaction; }

            @Override
            public boolean hasTransaction(){ return true; }

            @Override
            public String getMessageText() {
                return "Approve Transaction : " + transaction.getDescription() + " " + TextDisplayHelper.showDate(transaction.getDate());
            }
        };
    }

    @Override
    public void transactionChanged() {

    }

    @Override
    public void transactionApproved(Transaction transaction) {

    }
}
