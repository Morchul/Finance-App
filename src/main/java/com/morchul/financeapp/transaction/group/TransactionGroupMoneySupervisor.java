package com.morchul.financeapp.transaction.group;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class TransactionGroupMoneySupervisor {

    private FinanceAppApplication app;
    private FloatProperty moneyToSpendProperty;

    public TransactionGroupMoneySupervisor(FinanceAppApplication app) {
        this.app = app;
        moneyToSpendProperty = new SimpleFloatProperty(0);
    }

    public void calc(){
        float totalMoney = 0;
        for(MoneyAccountInterface m : app.getData().getAccounts()){
            totalMoney += m.getMoney();
        }

        for(TransactionGroup g : app.getData().getGroups()){
            totalMoney -= g.getMoney() > 0 ? g.getMoney() : 0;
        }
        moneyToSpendProperty.set(totalMoney);
    }

    public void addMoney(float money){
        moneyToSpendProperty.set(moneyToSpendProperty.get() + money);
    }

    public void getMoney(float money){
        moneyToSpendProperty.set(moneyToSpendProperty.get() - money);
    }

    public boolean canGetMoney(float money){
        return moneyToSpendProperty.get() - money >= 0;
    }

    public float getMoney(){
        return moneyToSpendProperty.get();
    }

    public FloatProperty moneyToSpendProperty() {
        return moneyToSpendProperty;
    }
}
