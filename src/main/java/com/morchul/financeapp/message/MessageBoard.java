package com.morchul.financeapp.message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MessageBoard {

    private ObservableList<FinanceAppMessage> messages;

    public MessageBoard() {
        messages = FXCollections.observableArrayList();
    }

    public void pinMessage(FinanceAppMessage message){
        messages.add(message);
    }

    public ObservableList<FinanceAppMessage> getMessages() {
        return messages;
    }

    public void unpinMessage(FinanceAppMessage message){
        messages.remove(message);
    }
}
