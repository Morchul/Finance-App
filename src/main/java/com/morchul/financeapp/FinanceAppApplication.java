package com.morchul.financeapp;


import com.morchul.financeapp.data.Data;
import com.morchul.financeapp.data.DataHelper;
import com.morchul.financeapp.factory.Factory;
import com.morchul.financeapp.json.TransactionConverter;
import com.morchul.financeapp.loader.Loader;
import com.morchul.financeapp.loader.LoadingSet;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.settings.Language;
import com.morchul.financeapp.settings.Settings;
import com.morchul.financeapp.store.Store;
import com.morchul.financeapp.store.StoreFactory;
import com.morchul.financeapp.store.StoreHelper;
import com.morchul.financeapp.transaction.group.TransactionGroupMoneySupervisor;
import com.morchul.financeapp.ui.MainUI;
import com.morchul.financeapp.ui.Window;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class FinanceAppApplication extends Application {

    private Logger logger;
    private MainUI window;
    private Data data;
    private Stage stage;
    private Store store;
    private StoreHelper storeHelper;
    private MessageBoard messageBoard;
    private Settings settings;
    private Factory factory;
    private TransactionConverter converter;
    private Language language;
    private TransactionGroupMoneySupervisor groupMoneySupervisor;

    @Override
    public void init() throws Exception {
        this.logger = LoggerFactory.getLogger(FinanceAppApplication.class);
        logger.info("Initialization ...");
        factory = new Factory(this);
        converter = new TransactionConverter(this);
        groupMoneySupervisor = new TransactionGroupMoneySupervisor(this);
        notifyPreloader(new Preloader.ProgressNotification(0.1));
        logger.info("Read properties ...");
        settings = new Settings("settings.properties",this);
        language = Language.getInstance();
        language.changeLanguage(settings.getLanguageLocale());
        notifyPreloader(new Preloader.ProgressNotification(0.2));
        logger.info("Create Store and Data ...");
        this.store = StoreFactory.createStore(StoreFactory.StoreKind.LOCAL_MONGO);
        this.storeHelper = new StoreHelper(store, this);
        data = new Data();
        notifyPreloader(new Preloader.ProgressNotification(0.3));
        logger.info("Create Monitors ...");
        messageBoard = new MessageBoard();
        notifyPreloader(new Preloader.ProgressNotification(0.5));
        logger.info("Create loader ...");
        Loader loader = new Loader(storeHelper);
        DataHelper dataHelper = new DataHelper(this);
        notifyPreloader(new Preloader.ProgressNotification(0.6));
        logger.info("Load from store " + store.getClass().getSimpleName() + " ...");
        LoadingSet loadingSet = loader.load();
        notifyPreloader(new Preloader.ProgressNotification(0.8));
        logger.info("Create Data ...");
        dataHelper.fillData(data, loadingSet);
        notifyPreloader(new Preloader.ProgressNotification(1));
        groupMoneySupervisor.calc();
        factory.monitorFactory.createTransactionGroupMoneySupervisorMonitor(groupMoneySupervisor, messageBoard);
        logger.info("Finish initialization");

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        window = new MainUI(this);

        final Scene scene = new Scene(window.getContent(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle(language.getString("WindowTitle"));
        stage.show();

        stage.widthProperty().addListener((observable, oldValue, newValue) -> window.getStatistic().update());
        stage.heightProperty().addListener((observable, oldValue, newValue) -> window.getStatistic().update());
        stage.setOnCloseRequest(event -> preDestroy());

        window.update();
    }

    private void preDestroy(){
        logger.info("Save to Store " + store.getClass().getSimpleName() + " ...");
        store.clear();
        storeHelper.saveTransactionGroups(data.getGroups());
        storeHelper.saveTransactions(data.getTransactions());
        storeHelper.saveMoneyAccounts(data.getAccounts());
        storeHelper.saveExternalMoneyAccounts(data.getExternalAccounts());
        storeHelper.saveTransactionFilters(data.getFilters());
        settings.store();
        logger.info("Data saved exit Application");
        System.exit(0);
    }

    public Stage getStage(){return stage;}

    public Data getData(){
        return data;
    }

    public Factory getFactory() {
        return factory;
    }

    public Language getLanguage() {
        return language;
    }

    public Window getWindow(){
        return window;
    }

    public Settings getSettings() { return settings; }

    public TransactionConverter getConverter() {
        return converter;
    }

    public Store getStore() { return store; }

    public StoreHelper getStoreHelper() { return storeHelper; }

    public Logger getLogger() { return logger; }

    public MessageBoard getMessageBoard() {
        return messageBoard;
    }

    public TransactionGroupMoneySupervisor getGroupMoneySupervisor() {
        return groupMoneySupervisor;
    }
}
