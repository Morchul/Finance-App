package com.morchul.financeapp.factory;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.ExternalMoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;

public class MoneyAccountFactory {

    private FinanceAppApplication app;

    public MoneyAccountFactory(FinanceAppApplication app){
        this.app = app;
    }

    public MoneyAccountInterface createMoneyAccount(String name){
        MoneyAccountInterface a = new MoneyAccount(name);
        app.getFactory().monitorFactory.createMoneyAccountMonitor(a, app.getMessageBoard());
        return a;
    }

    public MoneyAccountInterface createExternalMoneyAccount(String name){
        return new ExternalMoneyAccount(name);
    }
    public MoneyAccountInterface createExternalMoneyAccount(String name, String ownerName, String ownerSurname, String UUID){
        return new ExternalMoneyAccount(name, ownerName, ownerSurname, UUID);
    }

    public MoneyAccountInterface createMoneyAccount(String name, Float money, String ownerName, String ownerSurname, String UUID){
        MoneyAccountInterface m = new MoneyAccount(name, money, ownerName, ownerSurname, UUID);
        app.getFactory().monitorFactory.createMoneyAccountMonitor(m, app.getMessageBoard());
        return m;
    }

    public MoneyAccountInterface createMoneyAccountPlaceHolder(String UUID){
        return new MoneyAccount("", 0, UUID);
    }
}
