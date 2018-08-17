package com.morchul.financeapp.ui.tabs;

import javafx.scene.Node;

public interface FinanceAppTab {
    String getName();
    void update();
    Node getContent();
}
