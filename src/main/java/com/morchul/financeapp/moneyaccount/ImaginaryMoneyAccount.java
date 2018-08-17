package com.morchul.financeapp.moneyaccount;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ImaginaryMoneyAccount implements MoneyAccountInterface {

    @Override
    public String getUUID() {
        return "";
    }

    @Override
    public void addPointer() {

    }

    @Override
    public void removePointer() {

    }

    @Override
    public boolean noPointers() {
        return true;
    }

    @Override
    public String getName() {
        return "Unknown";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public StringProperty getNameProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public float getMoney() {
        return 1_000_000;
    }

    @Override
    public void addMoney(float money) {

    }

    @Override
    public FloatProperty getMoneyProperty() {
        return new SimpleFloatProperty(0);
    }

    @Override
    public String getOwnerName() {
        return "";
    }

    @Override
    public StringProperty getOwnerNameProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public String getOwnerSurname() {
        return "";
    }

    @Override
    public StringProperty getOwnerSurnameProperty() {
        return new SimpleStringProperty("");
    }

    @Override
    public void setOwnerName(String name) {

    }

    @Override
    public void setOwnerSurName(String name) {

    }

    @Override
    public MoneyAccountType getType() {
        return MoneyAccountType.IMAGINARY;
    }
}
