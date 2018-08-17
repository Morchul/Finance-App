package com.morchul.financeapp.moneyaccount;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public interface MoneyAccountInterface {
    String getUUID();

    void addPointer();
    void removePointer();
    boolean noPointers();

    String getName();
    void setName(String name);
    StringProperty getNameProperty();

    float getMoney();
    void addMoney(float money);
    FloatProperty getMoneyProperty();

    String getOwnerName();
    StringProperty getOwnerNameProperty();
    String getOwnerSurname();
    StringProperty getOwnerSurnameProperty();
    void setOwnerName(String name);
    void setOwnerSurName(String name);

    MoneyAccountType getType();

    enum MoneyAccountType {
        DEFAULT,
        IMAGINARY,
        EXTERNAL
    }


    class Owner{
        protected StringProperty nameProperty;
        protected StringProperty surNameProperty;

        protected Owner(String name, String surname){
            this.nameProperty  = new SimpleStringProperty(name);
            this.surNameProperty = new SimpleStringProperty(surname);
        }
    }
}
