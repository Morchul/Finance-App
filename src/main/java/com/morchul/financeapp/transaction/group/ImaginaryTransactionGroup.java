package com.morchul.financeapp.transaction.group;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ImaginaryTransactionGroup implements TransactionGroup{

    public ImaginaryTransactionGroup() {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getUUID() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public float getMoney() {
        return 0;
    }

    @Override
    public void addMoney(float money) {

    }

    @Override
    public void setMoney(float money) {

    }

    @Override
    public void setSupervisor(TransactionGroupMoneySupervisor s) {

    }

    @Override
    public TransactionGroupMoneySupervisor getSupervisor() {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public FloatProperty getMoneyProperty() {
        return new SimpleFloatProperty(0);
    }

    @Override
    public StringProperty getNameProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public boolean noPointers() {
        return true;
    }

    @Override
    public void addPointer() {

    }

    @Override
    public void removePointer() {

    }
}
