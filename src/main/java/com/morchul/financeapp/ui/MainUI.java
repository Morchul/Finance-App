package com.morchul.financeapp.ui;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.ui.tabs.*;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

public class MainUI implements Window {

    private MainTransactionUI mainTransaction;
    private StatisticUI statistic;
    private FilterViewUI filterView;
    private MoneyAccountUI moneyAccount;
    private TransactionGroupUI transactionGroup;

    private Label statusLabel;
    private Timer t;
    private TabPane tabPane;
    private Tab tab1;
    private Tab tab2;
    private Tab tab3;
    private Tab tab4;
    private Tab tab5;

    private FinanceAppApplication app;

    public MainUI(FinanceAppApplication app) {
        this.app = app;
        mainTransaction = new MainTransactionUI(app);
        statistic = new StatisticUI(app);
        filterView = new FilterViewUI(app);
        moneyAccount = new MoneyAccountUI(app);
        transactionGroup = new TransactionGroupUI(app);

        statusLabel = new Label();
    }

    public BorderPane getContent(){
        BorderPane borderPane = new BorderPane();

        tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);

        tab1 = new Tab(mainTransaction.getName(), mainTransaction.getContent());
        tab2 = new Tab(statistic.getName(), statistic.getContent());
        tab3 = new Tab(filterView.getName(), filterView.getContent());
        tab4 = new Tab(moneyAccount.getName(), moneyAccount.getContent());
        tab5 = new Tab(transactionGroup.getName(), transactionGroup.getContent());

        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        tab4.setClosable(false);
        tab5.setClosable(false);


        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);

        borderPane.setTop(createMenuBar());
        borderPane.setCenter(tabPane);
        borderPane.setBottom(statusLabel);

        return borderPane;
    }

    public void setStatus(String status){
        statusLabel.setText(status);
        app.getLogger().info(status);
        if(t != null) t.cancel();
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->statusLabel.setText(""));
            }
        }, 4000);
    }

    public void update(){
        mainTransaction.update();
        statistic.update();
        filterView.update();
        moneyAccount.update();
        transactionGroup.update();
    }

    public void selectTab(TabType tab){
        switch (tab){
            case MAIN_TRANSACTION: tabPane.getSelectionModel().select(tab1); break;
            case FILTER: tabPane.getSelectionModel().select(tab3); break;
            case TRANSACTION_GROUP: tabPane.getSelectionModel().select(tab5); break;
            case STATISTIC: tabPane.getSelectionModel().select(tab2); break;
            case MONEY_ACCOUNT: tabPane.getSelectionModel().select(tab4); break;

        }
    }

    public MainTransactionUI getMainTransaction() { return mainTransaction; }

    public StatisticUI getStatistic() { return statistic; }

    public MoneyAccountUI getMoneyAccount() { return moneyAccount; }

    public FilterViewUI getFilterView() { return filterView; }

    public TransactionGroupUI getTransactionGroup() {
        return transactionGroup;
    }

    private MenuBar createMenuBar(){

        MenuItem settings = new MenuItem("Settings");
        settings.setOnAction(event -> app.getSettings().showView());
        Menu date = new Menu(Constants.dateFormatter.format(LocalDate.now()));

        Menu file = new Menu("File");
        file.getItems().add(settings);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, date);
        return menuBar;
    }
}
