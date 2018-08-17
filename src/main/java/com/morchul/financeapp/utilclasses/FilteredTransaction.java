package com.morchul.financeapp.utilclasses;

import com.morchul.financeapp.moneyaccount.ImaginaryMoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.ui.TextDisplayHelper;
import javafx.beans.property.*;

public class FilteredTransaction {

    private FloatProperty amount;
    private StringProperty date;
    private TransactionGroup group;
    private MoneyAccountInterface from;
    private MoneyAccountInterface to;
    private StringProperty description;
    private BooleanProperty approved;

    private Transaction parent;

    public TransactionGroup getGroup() {
        return group;
    }

    public MoneyAccountInterface getFrom() {
        return from;
    }

    public MoneyAccountInterface getTo() {
        return to;
    }

    public Transaction getParent() {
        return parent;
    }

    public FilteredTransaction(Transaction t){
        parent = t;
        approved = new SimpleBooleanProperty(t.isApproved());
        amount = new SimpleFloatProperty(t.getAmount());
        date = new SimpleStringProperty(TextDisplayHelper.showDate(t.getDate()));
        group = t.getGroup();
        from = t.getFrom();
        if(t.getTo() != null)
            to = t.getTo();
        else
            to = new ImaginaryMoneyAccount(); // empty placeholder don't create it in factory
        description = new SimpleStringProperty(t.getDescription());
    }

    public FloatProperty amountProperty() {
        return amount;
    }

    public BooleanProperty approvedProperty() {
        return approved;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
