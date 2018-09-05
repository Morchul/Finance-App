package com.morchul.financeapp.store;

import com.morchul.financeapp.FinanceAppApplication;

public class StoreFactory {

    public static Store createStore(FinanceAppApplication app) throws Exception {
        switch (app.getSettings().getStoreType()){
            case "local": return new LocalMongoDB();
            case "external": return new ExternalMongoDB(app.getSettings().getExternalMongoDBHost());
            case "file":
                throw new Exception("Not Implemented yet");
        }
        return null;
    }
}
