package com.morchul.financeapp.factory;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.TransactionBinder;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionFactory {

    private FinanceAppApplication app;

    TransactionFactory(FinanceAppApplication app){
        this.app = app;
    }

    public Transaction createTransactionSimple(
            LocalDate date,
            float amount,
            TransactionGroup group,
            boolean approved,
            String description,
            String UUID,
            MoneyAccountInterface from,
            MoneyAccountInterface to
    ){
        Transaction t = new Transaction(date, amount, group, approved, description, UUID, from, to);
        app.getFactory().monitorFactory.createTransactionMonitor(t, app.getMessageBoard());
        return t;
    }

    public List<Transaction> createTransaction(
            LocalDate date,
            float amount,
            TransactionGroup group,
            String description,
            MoneyAccountInterface from,
            MoneyAccountInterface to,
            int repetitionCount,
            RepetitionType type){

        if(type == RepetitionType.NONE){
            return Collections.singletonList(create(date, amount, group, description, from, to));
        } else {
            List<Transaction> transactions = new ArrayList<>();
            for(int i = 0; i < repetitionCount ; ++i){
                switch (type){
                    case DAY: transactions.add(create(date.plusDays(i),amount, group, description, from, to)); break;
                    case WEEK: transactions.add(create(date.plusWeeks(i),amount, group, description, from, to)); break;
                    case MONTH: transactions.add(create(date.plusMonths(i),amount, group, description, from, to)); break;
                    case YEAR: transactions.add(create(date.plusYears(i),amount, group, description, from, to)); break;
                }
            }
            TransactionBinder.bind(transactions);
            return transactions;
        }
    }

    private Transaction create(LocalDate date, float amount, TransactionGroup group, String description, MoneyAccountInterface from , MoneyAccountInterface to){
        Transaction t = new Transaction(date, amount, group, date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()), description, from, to);
        app.getFactory().monitorFactory.createTransactionMonitor(t, app.getMessageBoard());
        return t;
    }

    public enum RepetitionType{
        DAY,
        WEEK,
        MONTH,
        YEAR,
        NONE
    }
}
