package com.morchul.financeapp.ui.tabs;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.utilclasses.FilteredTransaction;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.ui.FilterFactory;
import com.morchul.financeapp.ui.UIHelper;
import com.morchul.financeapp.utilclasses.TransactionEdit;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class FilterViewUI implements FinanceAppTab {


    private BorderPane borderPane;
    private static final String NAME = "FilterView";
    private FinanceAppApplication app;
    private ListView<TransactionFilter> filterView;
    private TableView<FilteredTransaction> filteredTransactions;

    public FilterViewUI(FinanceAppApplication app) {
        this.app = app;
        borderPane = new BorderPane();
        borderPane.setCenter(createCenter());
        borderPane.setLeft(createLeftSide());
    }

    private BorderPane createLeftSide(){
        BorderPane left = new BorderPane();
        filterView = new ListView<>();
        filterView.setItems(app.getData().getFilters());
        filterView.setCellFactory(param ->
            new ListCell<TransactionFilter>(){
                @Override
                protected void updateItem(TransactionFilter item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null || empty)
                        setText(null);
                    else
                        setText(item.getName());
                }
        });

        filterView.setOnMouseClicked(event -> updateFilteredTransactionsItems());

        Button createFilter = new Button(app.getLanguage().getString("AddFilter"));
        createFilter.setOnAction(event -> {
            TransactionFilter filter = FilterFactory.createNewFilter(app.getData());
            if(filter != null)
                app.getData().addFilter(filter);
            else
                app.getWindow().setStatus(app.getLanguage().getString("ErrorCreateNewFilter"));
        });
        Button removeFilter = new Button(app.getLanguage().getString("RemoveFilter"));
        removeFilter.setOnAction(event -> {
            TransactionFilter filter = filterView.getSelectionModel().getSelectedItem();
            if(filter != null && UIHelper.showConfirmDialog(app.getLanguage().getString("AreYouSureToRemove") + " " + filter.getName())){
                app.getData().removeFilter(filter);
                app.getLogger().info("Delete filter: " + filter.getName());
            }
        });

        HBox box = new HBox();
        box.setSpacing(5);
        box.setPadding(new Insets(0,0,10,0));
        box.getChildren().addAll(createFilter, removeFilter);
        left.setPadding(new Insets(10,10,10,10));
        left.setTop(box);
        left.setCenter(filterView);
        return left;
    }

    private void updateFilteredTransactionsItems(){
        filteredTransactions.getItems().clear();
        TransactionFilter filter = filterView.getSelectionModel().getSelectedItem();
        if(filter != null)
            filteredTransactions.setItems(filter.filter(app.getData().getTransactions()));
    }

    private BorderPane createCenter(){
        BorderPane center = new BorderPane();
        filteredTransactions = new TableView<>();
        filteredTransactions.setEditable(false);

        TableColumn<FilteredTransaction, String> description = new TableColumn<>(app.getLanguage().getString("Description"));
        description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        TableColumn<FilteredTransaction, Number> amount = new TableColumn<>(app.getLanguage().getString("Amount"));
        amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        TableColumn<FilteredTransaction, String> date = new TableColumn<>(app.getLanguage().getString("Date"));
        date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        TableColumn<FilteredTransaction, String> groupName = new TableColumn<>(app.getLanguage().getString("Group"));
        groupName.setCellValueFactory(cellData -> cellData.getValue().getGroup().getNameProperty());
        TableColumn<FilteredTransaction, String> fromMoneyAccount = new TableColumn<>(app.getLanguage().getString("From"));
        fromMoneyAccount.setCellValueFactory(cellData -> cellData.getValue().getFrom().getNameProperty());
        TableColumn<FilteredTransaction, String> toMoneyAccount = new TableColumn<>(app.getLanguage().getString("To"));
        toMoneyAccount.setCellValueFactory(cellData -> cellData.getValue().getTo().getNameProperty());
        TableColumn<FilteredTransaction, Boolean> approved = new TableColumn<>(app.getLanguage().getString("Approved"));
        approved.setCellValueFactory(cellData -> cellData.getValue().approvedProperty());

        filteredTransactions.getColumns().addAll(description, amount, date, groupName, fromMoneyAccount, toMoneyAccount,approved);

        Button remove = new Button(app.getLanguage().getString("Remove"));
        remove.setOnAction(event ->{
            Transaction t = filteredTransactions.getSelectionModel().getSelectedItem().getParent();
            if(t != null){
                if(t.hasNext() || t.hasPrevious()){
                    int answer = UIHelper.showTransactionDeleteDialog();
                    if(answer >= 0) {
                        app.getData().removeTransaction(t, answer == 1 || answer == 3, answer == 2 || answer == 3);
                        app.getLogger().info("Delete transaction/s");
                    }
                } else {
                    if(UIHelper.showConfirmDialog("Do you want to delete this transaction: " + t.getDescription())){
                        app.getData().removeTransaction(t, false, false);
                        app.getLogger().info("Delete single transaction");
                    }
                }
                app.getWindow().update();
            }
        });
        Button edit = new Button(app.getLanguage().getString("Edit"));
        edit.setOnAction(event -> {
            Transaction t = filteredTransactions.getSelectionModel().getSelectedItem().getParent();
            if(t != null) {
                TransactionEdit te = UIHelper.showTransactionEditDialog(t);
                String message = "";
                if(te != null && (message = UIHelper.validInput(te)).equals("")) {
                    if (t.hasNext() || t.hasPrevious()) {
                        int answer = UIHelper.showTransactionDeleteDialog();
                        if (answer >= 0) {
                            t.setEdit(te, answer == 1 || answer == 3, answer == 2 || answer == 3);
                            app.getLogger().info("Edit transaction/s");
                        }
                    } else {
                        t.setEdit(te, false, false);
                        app.getLogger().info("Edit single transaction");
                    }
                    app.getData().monitor.dataChangedEvent();
                    app.getGroupMoneySupervisor().calc();
//                    if(te.getOldTo() != te.getTo()) {
//                        if (te.getOldTo() != null && te.getOldTo().getType() == MoneyAccountInterface.MoneyAccountType.DEFAULT) {
//                            app.getGroupMoneySupervisor().addMoney(-te.getOldAmount());
//                        }
//                        if (te.getTo() != null && te.getTo().getType() == MoneyAccountInterface.MoneyAccountType.DEFAULT) {
//                            app.getGroupMoneySupervisor().addMoney(te.getAmount());
//                        }
//                    }
//                    if(te.getOldFrom() != te.getFrom()) {
//                        if (te.getOldFrom() != null && te.getOldFrom().getType() == MoneyAccountInterface.MoneyAccountType.DEFAULT) {
//                            app.getGroupMoneySupervisor().addMoney(te.getOldAmount());
//                        }
//                        if (te.getFrom() != null && te.getFrom().getType() == MoneyAccountInterface.MoneyAccountType.DEFAULT) {
//                            app.getGroupMoneySupervisor().addMoney(-te.getAmount());
//                        }
//                    }

                } else {
                    app.getWindow().setStatus(app.getLanguage().getString("ErrorByEdit") + " " + message);
                }
                app.getWindow().update();
            }
        });

        HBox box = new HBox();
        box.setSpacing(5);
        box.setPadding(new Insets(0,0,10,0));
        box.getChildren().addAll(edit, remove);
        center.setPadding(new Insets(10, 10, 10, 0));
        center.setTop(box);
        center.setCenter(filteredTransactions);
        return center;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void update() {
        updateFilteredTransactionsItems();
    }

    @Override
    public Node getContent() {
        return borderPane;
    }
}
