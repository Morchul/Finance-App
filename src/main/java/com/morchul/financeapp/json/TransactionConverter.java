package com.morchul.financeapp.json;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TransactionConverter {

    public static final String TRANSACTION_CHAIN = "transactionChain";
    public static final String TRANSACTION_UUID = "transactionUUID";
    public static final String TRANSACTION_GROUP_UUID = "transactionGroupUUID";
    public static final String MONEY_ACCOUNT_UUID = "moneyAccountUUID";
    public static final String TRANSACTION_FILTER_UUID = "filterUUID";

    private static final String TRANSACTION_DATE = "transactionDate";
    private static final String TRANSACTION_AMOUNT = "transactionAmount";
    private static final String TRANSACTION_GROUP = "transactionGroup";
    private static final String TRANSACTION_APPROVED = "transactionApproved";
    private static final String TRANSACTION_DESCRIPTION = "transactionDescription";
    private static final String TRANSACTION_MONEY_ACCOUNT_FROM = "transactionFrom";
    private static final String TRANSACTION_MONEY_ACCOUNT_TO = "transactionTo";

    private static final String TRANSACTION_GROUP_NAME = "transactionGroupName";
    private static final String TRANSACTION_GROUP_MONEY_PER_MONTH = "transactionGroupMoneyPerMonth";

    private static final String MONEY_ACCOUNT_NAME = "moneyAccountName";
    private static final String MONEY_ACCOUNT_MONEY = "moneyAccountMoney";
    private static final String MONEY_ACCOUNT_OWNER_NAME = "moneyAccountOwnerName";
    private static final String MONEY_ACCOUNT_OWNER_SURNAME = "moneyAccountOwnerSurName";

    private FinanceAppApplication app;

    public TransactionConverter(FinanceAppApplication app){
        this.app = app;
    }

    /**
     * This method is for create JSON to store in Mongo
     * @param transaction the transaction
     * @return TransactionChain, Transaction or null if Transaction part of TransactionChain but not first Transaction
     */
    public JSONObject transactionChainToJSON(Transaction transaction){
        if(transaction.hasPrevious()) {
            return null;
        } else if(transaction.hasNext()){
            JSONArray transactionChain = new JSONArray();
            transactionChain.put(toJSON(transaction));
            Transaction next = transaction.getNext();
            while(next.hasNext()){
                transactionChain.put(toJSON(next));
                next = next.getNext();
            }
            transactionChain.put(toJSON(next));
            return new JSONObject().
                    put(TRANSACTION_CHAIN, transactionChain);
        } else {
            return toJSON(transaction);
        }
    }

    public JSONObject toJSON(Transaction transaction){

        ZonedDateTime zdt = transaction.getDate().atStartOfDay(ZoneId.systemDefault());
        Long millis = zdt.toInstant().toEpochMilli();

        return new JSONObject()
                .put(TRANSACTION_DATE, millis)
                .put(TRANSACTION_AMOUNT, transaction.getAmount())
                .put(TRANSACTION_GROUP, (transaction.getGroup() == null) ? "" : transaction.getGroup().getUUID())
                .put(TRANSACTION_APPROVED, transaction.isApproved())
                .put(TRANSACTION_DESCRIPTION, transaction.getDescription())
                .put(TRANSACTION_UUID, transaction.getUUID())
                .put(TRANSACTION_MONEY_ACCOUNT_FROM, transaction.getFrom().getUUID())
                .put(TRANSACTION_MONEY_ACCOUNT_TO, (transaction.getTo() != null) ? transaction.getTo().getUUID() : "");
    }

    public JSONObject toJSON(TransactionGroup group){
        return new JSONObject()
                .put(TRANSACTION_GROUP_NAME, group.getName())
                .put(TRANSACTION_GROUP_MONEY_PER_MONTH, group.getMoney())
                .put(TRANSACTION_GROUP_UUID, group.getUUID());
    }

    public JSONObject toJSON(MoneyAccountInterface account){
        return new JSONObject()
                .put(MONEY_ACCOUNT_NAME, account.getName())
                .put(MONEY_ACCOUNT_MONEY, account.getMoney())
                .put(MONEY_ACCOUNT_UUID, account.getUUID())
                .put(MONEY_ACCOUNT_OWNER_NAME, account.getOwnerName())
                .put(MONEY_ACCOUNT_OWNER_SURNAME, account.getOwnerSurname());
    }

    public Transaction toTransaction(JSONObject json){
        LocalDate date = Instant.ofEpochMilli(json.getJSONObject(TRANSACTION_DATE).getLong("$numberLong")).atZone(ZoneId.systemDefault()).toLocalDate();

        return app.getFactory().transactionFactory.createTransactionSimple(
                date,
                json.getFloat(TRANSACTION_AMOUNT),
                app.getFactory().transactionGroupFactory.createTransactionGroupPlaceHolder(json.getString(TRANSACTION_GROUP)),
                json.getBoolean(TRANSACTION_APPROVED),
                json.getString(TRANSACTION_DESCRIPTION),
                json.getString(TRANSACTION_UUID),
                app.getFactory().moneyAccountFactory.createMoneyAccountPlaceHolder(json.getString(TRANSACTION_MONEY_ACCOUNT_FROM)),
                app.getFactory().moneyAccountFactory.createMoneyAccountPlaceHolder(json.getString(TRANSACTION_MONEY_ACCOUNT_TO)));
    }

    public TransactionGroup toTransactionGroup(JSONObject json){
        return app.getFactory().transactionGroupFactory.createTransactionGroup(
                json.getString(TRANSACTION_GROUP_NAME),
                json.getFloat(TRANSACTION_GROUP_MONEY_PER_MONTH),
                json.getString(TRANSACTION_GROUP_UUID));
    }

    public MoneyAccountInterface toMoneyAccount(JSONObject json){
        return app.getFactory().moneyAccountFactory.createMoneyAccount(
                json.getString(MONEY_ACCOUNT_NAME),
                json.getFloat(MONEY_ACCOUNT_MONEY),
                json.getString(MONEY_ACCOUNT_OWNER_NAME),
                json.getString(MONEY_ACCOUNT_OWNER_SURNAME),
                json.getString(MONEY_ACCOUNT_UUID)
        );
    }

    public MoneyAccountInterface toExternalMoneyAccount(JSONObject json){
        return app.getFactory().moneyAccountFactory.createExternalMoneyAccount(
                json.getString(MONEY_ACCOUNT_NAME),
                json.getString(MONEY_ACCOUNT_OWNER_NAME),
                json.getString(MONEY_ACCOUNT_OWNER_SURNAME),
                json.getString(MONEY_ACCOUNT_UUID)
        );
    }
}
