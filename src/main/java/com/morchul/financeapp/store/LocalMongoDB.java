package com.morchul.financeapp.store;

public class LocalMongoDB extends MongoStore {

    public LocalMongoDB() {
        super("localhost");
    }
}
