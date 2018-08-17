package com.morchul.financeapp.moneyaccount;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExternalMoneyAccount implements MoneyAccountInterface {

    private String UUID;
    private FloatProperty moneyProperty;
    private StringProperty nameProperty;
    private Owner owner;
    private int pointerTo = 0;

    public ExternalMoneyAccount(String name){this(name, java.util.UUID.randomUUID().toString());}

    public ExternalMoneyAccount(String name, String UUID){this(name, "unknown", "unknown", UUID); }

    public ExternalMoneyAccount(String name, String ownerName, String ownerSurname, String UUID) {
        this.nameProperty = new SimpleStringProperty(name);
        this.moneyProperty = new SimpleFloatProperty(0);
        this.UUID = UUID;
        owner = new Owner(ownerName, ownerSurname);
    }

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public void addPointer() {
        ++pointerTo;
    }

    @Override
    public void removePointer() {
        --pointerTo;
    }

    @Override
    public boolean noPointers() {
        return pointerTo == 0;
    }

    @Override
    public String getName() {
        return nameProperty.get();
    }

    @Override
    public void setName(String name) {
        nameProperty.set(name);
    }

    @Override
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    @Override
    public float getMoney() { return 1_000_000; }

    @Override
    public void addMoney(float money) { }

    @Override
    public FloatProperty getMoneyProperty() { return moneyProperty; }

    @Override
    public String getOwnerName() {
        return owner.nameProperty.get();
    }

    @Override
    public StringProperty getOwnerNameProperty() {
        return owner.nameProperty;
    }

    @Override
    public String getOwnerSurname() {
        return owner.surNameProperty.get();
    }

    @Override
    public StringProperty getOwnerSurnameProperty() {
        return owner.surNameProperty;
    }

    @Override
    public void setOwnerName(String name) {
        owner.nameProperty.set(name);
    }

    @Override
    public void setOwnerSurName(String name) {
        owner.surNameProperty.set(name);
    }

    @Override
    public MoneyAccountType getType() {
        return MoneyAccountType.EXTERNAL;
    }
}
