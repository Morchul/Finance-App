package com.morchul.financeapp.data;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.GroupTransactionFilter;
import com.morchul.financeapp.filter.MoneyAccountFilter;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.loader.LoadingSet;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.util.List;

public class DataHelper {

    private FinanceAppApplication app;

    public DataHelper(FinanceAppApplication app) {
        this.app = app;
    }

    public void fillData(Data data, LoadingSet loadingSet) throws Exception {
        app.getLogger().info("Create Transaction groups ...");
        //data.setGroups(FXCollections.observableArrayList(loadingSet.groups));
        fillDataGroup(data, loadingSet.groups);
        fillDataAccount(data, loadingSet.accounts);
        fillDataExternalAccount(data, loadingSet.externalAccounts);
        fillDataFilter(data, loadingSet.filters, loadingSet.groups, loadingSet.accounts);
        //data.setAccounts(FXCollections.observableArrayList(loadingSet.accounts));
        app.getLogger().info("Create Transactions ...");
        fillDataTransactions(data, loadingSet.transactions, loadingSet.groups, loadingSet.accounts, loadingSet.externalAccounts);
    }

    private void fillDataExternalAccount(Data data, List<MoneyAccountInterface> externalAccounts){
        for(MoneyAccountInterface m : externalAccounts){
            data.addExternalAccount(m);
        }
    }

    private void fillDataFilter(Data data, List<TransactionFilter> filters, List<TransactionGroup> groups, List<MoneyAccountInterface> accounts) throws Exception {
        boolean group = false, account = false;
        for(TransactionFilter f : filters){
            for(GroupTransactionFilter gtf : f.getFilter(GroupTransactionFilter.class)){
                group = false;
                for(TransactionGroup g : groups) {
                    if (gtf.getGroup().getUUID().equals(g.getUUID())) {
                        gtf.setGroup(g);
                        group = true;
                        break;
                    }
                }
                if(!group && f.hasGroupFilter())
                    throw new Exception("Error by fill " + GroupTransactionFilter.class.getSimpleName());
            }

            for(MoneyAccountFilter maf : f.getFilter(MoneyAccountFilter.class)){
                account = false;
                for(MoneyAccountInterface m : accounts){
                    if(maf.getMoneyAccountFilter().getUUID().equals(m.getUUID())){
                        maf.setMoneyAccount(m);
                        account = true;
                        break;
                    }
                }
                if(!account && f.hasMoneyAccountFilter())
                    throw new Exception("Error by fill " + MoneyAccountFilter.class.getSimpleName());
            }

            data.addFilter(f);
        }
    }

    private void fillDataGroup(Data data, List<TransactionGroup> groups){
        for(TransactionGroup g: groups){
            data.addGroup(g);
        }
    }

    private void fillDataAccount(Data data, List<MoneyAccountInterface> accounts){
        for(MoneyAccountInterface m: accounts){
            data.addAccount(m);
        }
    }

    private void fillDataTransactions(Data data, List<Transaction> transactions, List<TransactionGroup> groups, List<MoneyAccountInterface> accounts, List<MoneyAccountInterface> externalAccounts) throws Exception {
        boolean group, from, to;
        for(Transaction t : transactions){
            from = false; group = false; to = false;
            for(TransactionGroup g : groups){
                if(t.getGroup().getUUID().equals(g.getUUID())) {
                    t.setGroup(g, false, false);
                    group = true;
                    break;
                }
            }
            for(MoneyAccountInterface m : accounts){
                if(t.getFrom().getUUID().equals(m.getUUID())) {
//                    t.setMoneyAccountFrom(m, false, false);
                    from = true;
                    t.setFrom(m, false, false);
                    break;
                }
            }
            if(!from){
                for(MoneyAccountInterface m : externalAccounts){
                    if(t.getFrom().getUUID().equals(m.getUUID())) {
//                    t.setMoneyAccountFrom(m, false, false);
                        from = true;
                        t.setFrom(m, false, false);
                        break;
                    }
                }
            }
            for(MoneyAccountInterface m : accounts){
                if(t.getTo() != null)
                    if(t.getTo().getUUID().equals(m.getUUID())){
//                      t.setMoneyAccountTo(m, false, false);
                        to = true;
                        t.setTo(m, false, false);
                        break;
                    }
            }
            if(!to){
                for(MoneyAccountInterface m : externalAccounts){
                    if(t.getTo() != null)
                        if(t.getTo().getUUID().equals(m.getUUID())){
//                      t.setMoneyAccountTo(m, false, false);
                            t.setTo(m, false, false);
                            break;
                        }
                }
            }
            if(!from)
                t.setFrom(null,false, false);
            if(!group)
                t.setGroup(null, false, false);
            data.addTransaction(t);
        }
    }
}
