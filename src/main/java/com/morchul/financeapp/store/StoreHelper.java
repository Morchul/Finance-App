package com.morchul.financeapp.store;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.FilterConverter;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.TransactionBinder;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.morchul.financeapp.json.TransactionConverter.TRANSACTION_CHAIN;

public class StoreHelper {

    private Store store;
    private FinanceAppApplication app;

    public StoreHelper(Store store, FinanceAppApplication app) {
        this.store = store;
        this.app = app;
    }

    public void saveMoneyAccounts(List<MoneyAccountInterface> accounts){
        List<JSONObject> jsons = new ArrayList<>();
        for(MoneyAccountInterface a : accounts){
            jsons.add(app.getConverter().toJSON(a));
        }
        store.saveMoneyAccounts(jsons);
    }

    public void saveExternalMoneyAccounts(List<MoneyAccountInterface> accounts){
        List<JSONObject> jsons = new ArrayList<>();
        for(MoneyAccountInterface m : accounts){
            jsons.add(app.getConverter().toJSON(m));
        }
        store.saveExternalMoneyAccounts(jsons);
    }

    public void saveTransactionFilters(List<TransactionFilter> filters){
        List<JSONObject> jsons = new ArrayList<>();
        for(TransactionFilter f :  filters){
            jsons.add(FilterConverter.filterToJSON(f));
        }
        store.saveFilter(jsons);
    }

    public void saveTransactionGroups(List<TransactionGroup> groups){
        List<JSONObject> jsons = new ArrayList<>();
        for(TransactionGroup g : groups){
            jsons.add(app.getConverter().toJSON(g));
        }
        store.saveTransactionGroups(jsons);
    }

    public List<TransactionGroup> getTransactionGroups(){
        List<TransactionGroup> res = new ArrayList<>();
        List<JSONObject> jsons = store.getTransactionGroups();
        for(JSONObject json: jsons){
            res.add(app.getConverter().toTransactionGroup(json));
        }
        return res;
    }

    public void saveTransactions(List<Transaction> transactionList){
        List<JSONObject> jsons = new ArrayList<>();
        for(Transaction t : transactionList){
            JSONObject o = app.getConverter().transactionChainToJSON(t);
            if(o == null)
                continue;
            jsons.add(o);
        }
        store.saveTransactions(jsons);
    }

    public List<MoneyAccountInterface> getMoneyAccounts(){
        List<MoneyAccountInterface> res = new ArrayList<>();
        List<JSONObject> jsons = store.getMoneyAccounts();
        for(JSONObject json: jsons){
            res.add(app.getConverter().toMoneyAccount(json));
        }
        return res;
    }

    public List<MoneyAccountInterface> getExternalMoneyAccounts(){
        List<MoneyAccountInterface> res = new ArrayList<>();
        List<JSONObject> jsons = store.getExternalMoneyAccounts();
        for(JSONObject json: jsons){
            res.add(app.getConverter().toExternalMoneyAccount(json));
        }
        return res;
    }

    public List<TransactionFilter> getTransactionFilter(){
        List<TransactionFilter> res = new ArrayList<>();
        List<JSONObject> jsons = store.getFilter();
        for(JSONObject json: jsons){
            res.add(FilterConverter.JSONToFilter(json));
        }
        return res;
    }

    public List<Transaction> getTransactions(){
        List<Transaction> res = new ArrayList<>();
        List<JSONObject> jsons = store.getTransactions();
        for(JSONObject json: jsons){
            if(json.has(TRANSACTION_CHAIN)){
                res.addAll(joinTransactionChain(json));
            } else {
                res.add(app.getConverter().toTransaction(json));
            }
        }
        return res;
    }

    private List<Transaction> joinTransactionChain(JSONObject json){
        List<Transaction> chain = new ArrayList<>();
        for(Object o : json.getJSONArray(TRANSACTION_CHAIN)){
            chain.add(toTrans(o));
        }
        TransactionBinder.bind(chain);
        return chain;
    }

    /**
     * helper method
     * @param o instance of JSONObject
     * @return Transaction
     */
    private Transaction toTrans(Object o) {
        return app.getConverter().toTransaction((JSONObject) o);
    }
}
