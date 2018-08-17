package com.morchul.financeapp.store;

import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.json.JSONObject;

import java.util.List;

public interface Store {
    List<JSONObject> getTransactions();
    void saveTransactions(List<JSONObject> transactions);
    void deleteTransaction(Transaction transaction);

    List<JSONObject> getTransactionGroups();
    void saveTransactionGroups(List<JSONObject> groups);
    void deleteTransactionGroup(TransactionGroup transactionGroup);

    List<JSONObject> getMoneyAccounts();
    void saveMoneyAccounts(List<JSONObject> accounts);
    void deleteMoneyAccount(MoneyAccountInterface account);

    List<JSONObject> getExternalMoneyAccounts();
    void saveExternalMoneyAccounts(List<JSONObject> accounts);
    void deleteExternalMoneyAccount(MoneyAccountInterface account);

    List<JSONObject> getFilter();
    void saveFilter(List<JSONObject> filter);
    void deleteFilter(TransactionFilter filter);

    void clear();
}
