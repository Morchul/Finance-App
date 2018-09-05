package com.morchul.financeapp.store;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.excludeId;
import static com.morchul.financeapp.json.TransactionConverter.*;

public abstract class MongoStore implements Store {

    protected Logger log = LoggerFactory.getLogger(LocalMongoDB.class);

    protected final String HOST;
    protected final int PORT = 27017;
    protected final String USERNAME = "financeAppUser";
    protected final String PWD = "financeAppPassword";
    protected final String DB = "financeApp";
    protected final String AUTH_DB = DB;

    protected final String TRANSACTION_COLLECTION = "transaction";
    protected final String TRANSACTION_GROUP_COLLECTION = "transactionGroup";
    protected final String TRANSACTION_MONEY_ACCOUNT_COLLECTION = "moneyAccount";
    protected final String TRANSACTION_FILTER_COLLECTION = "transactionFilter";
    protected final String TRANSACTION_EXTERNAL_MONEY_ACCOUNT_COLLECTION = "externalMoneyAccount";

    protected MongoClient mongoClient;
    protected MongoDatabase database;
    protected MongoCollection<Document> collection;

    public MongoStore(String host) {
        HOST = host;
        log.info("Open MongoDB Connection on: " + HOST + ":" + PORT);
        MongoCredential credential = MongoCredential.createScramSha1Credential(USERNAME,AUTH_DB, PWD.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().build();
        mongoClient = new MongoClient(new ServerAddress(HOST, PORT), credential, options);
        database = mongoClient.getDatabase(DB);
    }

    public List<JSONObject> getTransactions(){
        return get(TRANSACTION_COLLECTION);
    }

    @Override
    public void saveTransactions(List<JSONObject> transactions) {
        set(transactions, TRANSACTION_COLLECTION);
    }

    @Override
    public List<JSONObject> getTransactionGroups() {
        return get(TRANSACTION_GROUP_COLLECTION);
    }

    @Override
    public void saveTransactionGroups(List<JSONObject> groups) {
        set(groups, TRANSACTION_GROUP_COLLECTION);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        collection = database.getCollection(TRANSACTION_COLLECTION);
        Document doc = new Document().append(TRANSACTION_UUID, transaction.getUUID());
        collection.deleteOne(doc);
    }

    @Override
    public void deleteTransactionGroup(TransactionGroup transactionGroup) {
        collection = database.getCollection(TRANSACTION_GROUP_COLLECTION);
        Document doc = new Document().append(TRANSACTION_GROUP_UUID, transactionGroup.getUUID());
        collection.deleteOne(doc);
    }

    @Override
    public List<JSONObject> getMoneyAccounts() {
        return get(TRANSACTION_MONEY_ACCOUNT_COLLECTION);
    }

    @Override
    public void saveMoneyAccounts(List<JSONObject> accounts) {
        set(accounts, TRANSACTION_MONEY_ACCOUNT_COLLECTION);
    }

    @Override
    public void deleteMoneyAccount(MoneyAccountInterface account) {
        collection = database.getCollection(TRANSACTION_MONEY_ACCOUNT_COLLECTION);
        Document doc = new Document().append(MONEY_ACCOUNT_UUID, account.getUUID());
        collection.deleteOne(doc);
    }

    @Override
    public List<JSONObject> getExternalMoneyAccounts() {
        return get(TRANSACTION_EXTERNAL_MONEY_ACCOUNT_COLLECTION);
    }

    @Override
    public void saveExternalMoneyAccounts(List<JSONObject> accounts) {
        set(accounts, TRANSACTION_EXTERNAL_MONEY_ACCOUNT_COLLECTION);
    }

    @Override
    public void deleteExternalMoneyAccount(MoneyAccountInterface account) {
        collection = database.getCollection(TRANSACTION_EXTERNAL_MONEY_ACCOUNT_COLLECTION);
        Document doc = new Document().append(MONEY_ACCOUNT_UUID, account.getUUID());
        collection.deleteOne(doc);
    }

    @Override
    public List<JSONObject> getFilter() {
        return get(TRANSACTION_FILTER_COLLECTION);
    }

    @Override
    public void saveFilter(List<JSONObject> filter) {
        set(filter, TRANSACTION_FILTER_COLLECTION);
    }

    @Override
    public void deleteFilter(TransactionFilter filter) {
        collection = database.getCollection(TRANSACTION_FILTER_COLLECTION);
        Document doc = new Document().append(TRANSACTION_FILTER_UUID, filter.getUUID());
        collection.deleteOne(doc);
    }

    @Override
    public void clear() {
        remove(TRANSACTION_MONEY_ACCOUNT_COLLECTION);
        remove(TRANSACTION_GROUP_COLLECTION);
        remove(TRANSACTION_COLLECTION);
        remove(TRANSACTION_FILTER_COLLECTION);
        remove(TRANSACTION_EXTERNAL_MONEY_ACCOUNT_COLLECTION);
    }

    private void remove(String collectionName){
        collection = database.getCollection(collectionName);
        Document doc = new Document();
        collection.deleteMany(doc);
    }

    private List<JSONObject> get(String collectionName){
        collection = database.getCollection(collectionName);
        MongoCursor<Document> cursor = collection.find().projection(excludeId()).iterator();
        List<JSONObject> json = new ArrayList<>();
        while(cursor.hasNext()){
            json.add(new JSONObject(cursor.next().toJson()));
        }
        return json;
    }

    private void set(List<JSONObject> list, String collectionName){
        collection = database.getCollection(collectionName);
        for(JSONObject json: list){
            collection.insertOne(Document.parse(json.toString()));
        }
    }
}
