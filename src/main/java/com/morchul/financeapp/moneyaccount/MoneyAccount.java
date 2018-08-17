package com.morchul.financeapp.moneyaccount;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MoneyAccount implements MoneyAccountInterface{

    private FloatProperty moneyProperty;
    private StringProperty nameProperty;
    private Owner owner;
    private String UUID;
    private int pointerTo = 0;


    public MoneyAccount(String name) {
        this(name, 0, java.util.UUID.randomUUID().toString());
    }

    public MoneyAccount(String name, float money, String UUID){
        this(name, money, "unknown", "unknown", UUID);
    }

    public MoneyAccount(String name, float money, String ownerName, String ownerSurname, String UUID){
        this.nameProperty = new SimpleStringProperty(name);
        this.moneyProperty = new SimpleFloatProperty(money);
        this.UUID = UUID;
        owner = new Owner(ownerName, ownerSurname);
    }

    public String getUUID() {
        return UUID;
    }

    public void addPointer(){
        ++pointerTo;
    }

    public void removePointer(){
        --pointerTo;
    }

    public boolean noPointers(){
        return pointerTo == 0;
    }

    public String getName(){return nameProperty.get();}
    public void setName(String name){nameProperty.set(name);}
    public StringProperty getNameProperty(){return nameProperty;}

    public float getMoney(){return moneyProperty.get();}
    public void addMoney(float money){
        moneyProperty.set(moneyProperty.get() + money);
    }
    public FloatProperty getMoneyProperty(){return moneyProperty;}

    public String getOwnerName(){
        return owner.nameProperty.get();
    }
    public StringProperty getOwnerNameProperty(){return owner.nameProperty;}

    public String getOwnerSurname(){
        return owner.surNameProperty.get();
    }
    public StringProperty getOwnerSurnameProperty(){return owner.surNameProperty;}

    public void setOwnerName(String name){
        owner.nameProperty.set(name);
    }

    public void setOwnerSurName(String surName){
        owner.surNameProperty.set(surName);
    }

    @Override
    public MoneyAccountType getType() {
        return MoneyAccountType.DEFAULT;
    }


}
