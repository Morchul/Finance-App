package com.morchul.financeapp.data;

import com.morchul.financeapp.filter.MoneyAccountFilter;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

public class DataUtils {

    private Data data;

    public DataUtils(Data data){
        this.data = data;
    }

    private boolean contains(List<MoneyAccountFilter> a, MoneyAccountInterface m){
        for(MoneyAccountFilter maf : a){
            if(maf.getMoneyAccountFilter().getName().equals(m.getName())) return true;
        }
        return false;
    }

    /**
     *
     * @param from LocalDate
     * @param to LocalDate
     * @param mustBeApproved must Transaction be approved
     * @param filter a TransactionFilter
     * @param method 0 = total, 1 = positive, 2 = negative
     * @return the totalMoney
     */
    public float getTotalMoney(LocalDate from, LocalDate to, boolean mustBeApproved, TransactionFilter filter, int method){
        float sum = 0;
        boolean mAF = filter != null && filter.hasMoneyAccountFilter();
        for(Transaction t : (filter == null) ? data.getTransactions() : filter.filterAndGetList(data.getTransactions())){
            if(t.getDate().isAfter(from) && !t.getDate().isAfter(to)) {
                if (mAF) {
                    List<MoneyAccountFilter> a = filter.getFilter(MoneyAccountFilter.class);
                    MoneyAccountInterface fromA = t.getFrom();
                    MoneyAccountInterface toA = t.getTo();
                    if (mustBeApproved) {
                        if (t.isApproved()) {
                            if(toA instanceof MoneyAccount && contains(a, toA)){
                                if(fromA instanceof MoneyAccount && contains(a, fromA)){
                                    continue;
                                } else {
                                    if(method == 0 || method == 1)
                                        sum += t.getAmount();
                                }
                            } else if(fromA instanceof MoneyAccount && contains(a, fromA)){
                                if(method == 0)
                                    sum -= t.getAmount();
                                else if(method == 2)
                                    sum += t.getAmount();
                            }
                        }
                    } else {
                        if(toA instanceof MoneyAccount && contains(a, toA)){
                            if(fromA instanceof MoneyAccount && contains(a, fromA)){
                                continue;
                            } else {
                                if(method == 0 || method == 1)
                                    sum += t.getAmount();
                            }
                        } else if(fromA instanceof MoneyAccount && contains(a, fromA)){
                            if(method == 0)
                                sum -= t.getAmount();
                            else if(method == 2)
                                sum += t.getAmount();
                        }
                    }
                } else {
                    if (mustBeApproved) {
                        if (t.isApproved()) {
                            if (t.getTo() instanceof MoneyAccount && t.getFrom() instanceof MoneyAccount) continue;
                            if (t.getTo() instanceof MoneyAccount){
                                if(method == 0 || method == 1)
                                    sum += t.getAmount();
                            }
                            if (t.getFrom() instanceof MoneyAccount){
                                if(method == 0)
                                    sum -= t.getAmount();
                                else if(method == 2)
                                    sum += t.getAmount();
                            }
                        }
                    } else {
                        if (t.getTo() instanceof MoneyAccount && t.getFrom() instanceof MoneyAccount) continue;
                        if (t.getTo() instanceof MoneyAccount){
                            if(method == 0 || method == 1)
                                sum += t.getAmount();
                        }
                        if (t.getFrom() instanceof MoneyAccount){
                            if(method == 0)
                                sum -= t.getAmount();
                            else if(method == 2)
                                sum += t.getAmount();
                        }
                    }
                }
            }
        }
        return sum;
    }
}
