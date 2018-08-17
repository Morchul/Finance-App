package com.morchul.financeapp.transaction;

import java.util.List;

public class TransactionBinder {

    public static void bind(List<Transaction> transactionChain){
        Transaction actual;
        Transaction next;
        Transaction previous = null;

        actual = transactionChain.get(0);
        next = transactionChain.get(1);
        actual.bind(next, previous);

        for(int i = 1; i < transactionChain.size(); ++i){
            previous = actual;
            actual = next;
            next = (i + 1 < transactionChain.size()) ? transactionChain.get(i + 1) : null;
            actual.bind(next, previous);
        }
    }
}
