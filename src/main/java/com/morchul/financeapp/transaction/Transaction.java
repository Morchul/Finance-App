package com.morchul.financeapp.transaction;

import com.morchul.financeapp.data.Data;
import com.morchul.financeapp.moneyaccount.ImaginaryMoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.ImaginaryTransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.utilclasses.TransactionEdit;

import java.time.LocalDate;

public class Transaction {

    private float amount;
    private boolean approved = false;
    private LocalDate date;
    private TransactionGroup group;
    private MoneyAccountInterface from;
    private MoneyAccountInterface to;
    private String description;
    private TransactionEventHandler eventHandler;
    private Data data;

    private Transaction next;
    private Transaction previous;

    private String UUID;

    public Transaction(LocalDate date, float amount, TransactionGroup group, boolean approved, String description, Transaction next, Transaction previous, String UUID, MoneyAccountInterface from, MoneyAccountInterface to){
        this.date = date;
        this.amount = amount;
        setGroup(group, false, false);
        this.description = description;
        this.next = next;
        this.previous = previous;
        this.UUID = UUID;
        setFrom(from, false, false);
        setTo(to, false , false);
        setApproved(approved);
    }

    public Transaction(LocalDate date, float amount, TransactionGroup group, boolean approved, String description, String UUID, MoneyAccountInterface from, MoneyAccountInterface to){
        this(date, amount, group, approved, description, null, null, UUID, from ,to);
    }

    public Transaction(LocalDate date, float amount, TransactionGroup group, boolean approved, String description, MoneyAccountInterface from, MoneyAccountInterface to){
        this(date, amount, group, approved, description, java.util.UUID.randomUUID().toString(), from, to);
    }

    public void setHandler(TransactionEventHandler handler){
        eventHandler = handler;
    }

    public String getUUID() { return UUID; }

    public void bind(Transaction next, Transaction previous){this.next = next; this.previous = previous;}

    public boolean hasNext(){ return next != null;}
    public boolean hasPrevious(){return previous != null;}

    public Transaction getNext() { return next; }
    public Transaction getPrevious() { return previous; }

    public MoneyAccountInterface getFrom() { return from; }
    public MoneyAccountInterface getTo() { return to; }

    public void setApproved(boolean approved){
        this.approved = approved;
        if(eventHandler != null)
            eventHandler.approved();
        if(approved) {
            from.addMoney(-amount);
            getGroup().addMoney(-amount);
            if(to != null)
                to.addMoney(amount);
        }
    }
    public boolean isApproved(){return approved;}
    public void setDate(LocalDate date){ this.date = date; }
    public LocalDate getDate() { return date; }
    public void setAmount(float amount){
        from.addMoney(this.amount - amount);
        to.addMoney(amount - this.amount);
        group.addMoney(this.amount - amount);
        this.amount = amount;
    }
    public float getAmount() { return amount; }
    public TransactionGroup getGroup() { return group; }
    public String getDescription() { return description; }

    public void setGroup(TransactionGroup group, boolean toNext, boolean toPrevious){
        setGroup(group, toNext, toPrevious, false);
    }
    public void setGroup(TransactionGroup group, boolean toNext, boolean toPrevious, boolean edit) {
        if(group == null)
            group = new ImaginaryTransactionGroup();
        if(approved && edit) {
            if (getGroup() != null) getGroup().addMoney(amount);
            group.addMoney(-amount);
        }
        this.group = group;
        group.addPointer();
        if(next != null && toNext){
            next.setGroup(group, true, false);
        }
        if(previous != null && toPrevious){
            previous.setGroup(group, false, true);
        }
    }

    public void setData(Data data) { this.data = data; }

    public Data getData() { return data; }

//    public void setFrom(MoneyAccountInterface m){
//        this.from = m;
//        from.addPointer();
//    }

    public void setFrom(MoneyAccountInterface from, boolean toNext, boolean toPrevious){
        setFrom(from, toNext, toPrevious, false);
    }

    public void setFrom(MoneyAccountInterface from, boolean toNext, boolean toPrevious, boolean edit){
        if(from == null)
            from = new ImaginaryMoneyAccount();
        if(approved && edit && this.from != from){ //from has changed
            this.from.addMoney(amount);
            from.addMoney(-amount);
        }
        this.from = from;
        from.addPointer();
        if(next != null && toNext){
            next.setFrom(from, true, false);
        }
        if(previous != null && toPrevious){
            previous.setFrom(from, false, true);
        }
    }

//    public void setTo(MoneyAccountInterface m){
//        if(m == null || m.getName().equals(""))
//            return;
//        this.to = m;
//        to.addPointer();
//    }

    public void setTo(MoneyAccountInterface to, boolean toNext, boolean toPrevious){
        setTo(to, toNext, toPrevious, false);
    }

    public void setTo(MoneyAccountInterface to, boolean toNext, boolean toPrevious, boolean edit){
        if(to == null || to.getUUID().equals(""))
            to = new ImaginaryMoneyAccount();
        if(approved && edit && this.to != to){ //to has changed
            this.to.addMoney(-amount);
            to.addMoney(amount);
        }
        this.to = to;
        to.addPointer();
        if(next != null && toNext){
            next.setTo(to, true, false);
        }
        if(previous != null && toPrevious){
            previous.setTo(to, false, true);
        }
    }

    public void setDescription(String description, boolean toNext, boolean toPrevious) {
        this.description = description;
        if(next != null && toNext){
            next.setDescription(description, true, false);
        }
        if(previous != null && toPrevious){
            previous.setDescription(description, false, true);
        }
    }

    public void delete(boolean toNext, boolean toPrevious){
        if(next != null && toNext) {
            next.delete(true, false); //delete next
        } else if(next != null){
            next.previousDelete(toPrevious ? null : previous); //notify next about deletion and give him newPrevious
        }
        if(previous != null && toPrevious) {
            previous.delete(false, true);
        } else if(previous != null){
            previous.nextDelete(toNext ? null: next);
        }
        if(group != null) {
            group.removePointer();
            if(approved)
                group.addMoney(amount);
        }

        if(from != null) {
            from.removePointer();
            if(approved)
                from.addMoney(amount);
        }
        if(to != null) {
            to.removePointer();
            if(approved)
                to.addMoney(-amount);
        }

        next = null;
        previous = null;
        data.getTransactions().remove(this);
    }

    public void setEdit(TransactionEdit te, boolean toNext, boolean toPrevious){
        setGroup(te.getGroup(), toNext, toPrevious, true);
        setFrom(te.getFrom(), toNext, toPrevious, true);
        setTo(te.getTo(), toNext, toPrevious, true);
        setAmount(te.getAmount());
        setDate(te.getDate());
        setDescription(te.getDescription(), toNext, toPrevious);
    }

    public void previousDelete(Transaction newPrevious){ previous = newPrevious; }

    public void nextDelete(Transaction newNext){ next = newNext; }
}
