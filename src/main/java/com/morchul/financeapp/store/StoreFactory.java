package com.morchul.financeapp.store;

public class StoreFactory {

    public static Store createStore(StoreKind kind) throws Exception {
        switch (kind){
            case LOCAL_MONGO: return new LocalMongoDB();
            case EXTERNAL:
                throw new Exception("Not Implemented yet");
            case FILE:
                throw new Exception("Not Implemented yet");
        }
        return null;
    }

    public enum StoreKind {
        LOCAL_MONGO,
        EXTERNAL,
        FILE
    }
}
