package com.morchul.financeapp.transaction.group;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public interface TransactionGroup {
    void setName(String name);

    String getUUID();
    String getName();

    FloatProperty getMoneyProperty();
    StringProperty getNameProperty();

    boolean noPointers();
    void addPointer();
    void removePointer();

    float getMoney();
    void addMoney(float money);
    void setMoney(float money);

    void setSupervisor(TransactionGroupMoneySupervisor s);
    TransactionGroupMoneySupervisor getSupervisor();

    void delete();
}
