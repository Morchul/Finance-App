package com.morchul.financeapp.transaction.group;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleTransactionGroup implements TransactionGroup{

    public StringProperty nameProperty;
    public FloatProperty moneyProperty;
    private String UUID;
    private int pointerTo = 0;
    private TransactionGroupMoneySupervisor supervisor;

    public SimpleTransactionGroup(String name, float money) {
        this(name, money, java.util.UUID.randomUUID().toString());
    }

    public SimpleTransactionGroup(String name, float money, String UUID){
        this.nameProperty = new SimpleStringProperty(name);
        this.moneyProperty = new SimpleFloatProperty(money);
        this.UUID = UUID;
    }

    public void setSupervisor(TransactionGroupMoneySupervisor s){
        supervisor = s;
    }

    public void setName(String name){
        nameProperty.set(name);
    }

    public String getUUID() { return UUID; }
    public String getName() {
        return nameProperty.get();
    }

    public boolean noPointers(){return pointerTo == 0;}
    public void addPointer(){++pointerTo;}
    public void removePointer(){--pointerTo;}

    public float getMoney() {
        return moneyProperty.get();
    }

    @Override
    public void addMoney(float money) {
        moneyProperty.set(getMoney() + money);
    }

    @Override
    public void setMoney(float money) {
        moneyProperty.set(money);
    }

    @Override
    public TransactionGroupMoneySupervisor getSupervisor() {
        return supervisor;
    }

    @Override
    public void delete() {
        supervisor.addMoney(getMoney());
    }

    @Override
    public FloatProperty getMoneyProperty() {
        return moneyProperty;
    }

    @Override
    public StringProperty getNameProperty() {
        return nameProperty;
    }
}
