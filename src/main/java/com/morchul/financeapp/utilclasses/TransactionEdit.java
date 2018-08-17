package com.morchul.financeapp.utilclasses;

import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.TransactionGroup;

import java.time.LocalDate;

public class TransactionEdit {

    private final TransactionGroup group;
    private final MoneyAccountInterface from;
    private final MoneyAccountInterface oldFrom;
    private final MoneyAccountInterface to;
    private final MoneyAccountInterface oldTo;
    private final float amount;
    private final float oldAmount;
    private final LocalDate date;
    private boolean groupDisabled;
    private String description;

    public TransactionEdit(TransactionGroup group, MoneyAccountInterface from, MoneyAccountInterface oldFrom, MoneyAccountInterface to, MoneyAccountInterface oldTo, float amount, float oldAmount, LocalDate date, boolean groupDisabled, String description){
        this.group = group;
        this.from = from;
        this.oldFrom = oldFrom;
        this.to = to;
        this.oldTo = oldTo;
        this.amount = amount;
        this.oldAmount = oldAmount;
        this.date = date;
        this.groupDisabled = groupDisabled;
        this.description = description;
    }

    public TransactionGroup getGroup() {
        return group;
    }

    public MoneyAccountInterface getFrom() {
        return from;
    }

    public MoneyAccountInterface getOldFrom() {
        return oldFrom;
    }

    public MoneyAccountInterface getTo() {
        return to;
    }

    public MoneyAccountInterface getOldTo() {
        return oldTo;
    }

    public float getAmount() {
        return amount;
    }

    public float getOldAmount() {
        return oldAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isGroupDisabled() {
        return groupDisabled;
    }

    public String getDescription() {
        return description;
    }
}
