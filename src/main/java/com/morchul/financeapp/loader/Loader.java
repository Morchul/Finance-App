package com.morchul.financeapp.loader;

import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.store.StoreHelper;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.util.List;

public class Loader {

    private StoreHelper storeHelper;
    private LoadingSet loadingSet = null;


    public Loader(StoreHelper storeHelper) {
        this.storeHelper = storeHelper;
    }

    public LoadingSet load(){
        if(loadingSet == null) {

            List<MoneyAccountInterface> m = storeHelper.getMoneyAccounts();
            List<MoneyAccountInterface> exM = storeHelper.getExternalMoneyAccounts();
            List<TransactionGroup> g = storeHelper.getTransactionGroups();
            List<TransactionFilter> f = storeHelper.getTransactionFilter();
            List<Transaction> t = storeHelper.getTransactions();

            loadingSet = new LoadingSet(g, m, exM, f, t);
        }
        return loadingSet;
    }

    public void clear(){
        loadingSet = null;
    }
}
