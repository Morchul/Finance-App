package com.morchul.financeapp.ui;

public interface Window {
    void setStatus(String status);
    void update();
    void selectTab(TabType tab);

    enum TabType{
        MAIN_TRANSACTION,
        MONEY_ACCOUNT,
        STATISTIC,
        FILTER,
        TRANSACTION_GROUP
    }
}
