package com.morchul.financeapp.ui.tabs;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.ui.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class MoneyAccountUI implements FinanceAppTab {

    private static final String name = "Money Accounts";
    private TableView<MoneyAccountInterface> moneyAccountTable;
    private TableView<MoneyAccountInterface> externalMoneyAccountTable;
    private BorderPane borderPane;

    public MoneyAccountUI(FinanceAppApplication app) {

        moneyAccountTable = getTable(true, true);
        moneyAccountTable.setItems(app.getData().getAccounts());

        externalMoneyAccountTable = getTable(false, true);
        externalMoneyAccountTable.setItems(app.getData().getExternalAccounts());

        Button newAccountButton = new Button("new Account");
        newAccountButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create new Account");
            alert.setHeaderText("Choose which Account type you will create");
            alert.setContentText(null);

            ButtonType standard = new ButtonType("Default");
            ButtonType external = new ButtonType("External");
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(standard, external, cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if (result.get() == standard)
                    app.getData().addAccount(app.getFactory().moneyAccountFactory.createMoneyAccount("name"));
                else if (result.get() == external)
                    app.getData().addExternalAccount(app.getFactory().moneyAccountFactory.createExternalMoneyAccount("external"));
            }
        });
        Button removeAccountButton = new Button("Remove Account");
        removeAccountButton.setOnAction(event -> {
            MoneyAccountInterface account = moneyAccountTable.getSelectionModel().getSelectedItem();
            if(account != null && account.noPointers()){
                if(UIHelper.showConfirmDialog("You really want to delete: " + account.getName())) {
                    app.getData().removeAccount(account);
                    app.getStore().deleteMoneyAccount(account);
                }
            } else {
                app.getWindow().setStatus("Can't delete account!");
            }
        });
        Button removeExternalAccountButton = new Button("Remove External Account");
        removeExternalAccountButton.setOnAction(event -> {
            MoneyAccountInterface account = externalMoneyAccountTable.getSelectionModel().getSelectedItem();
            if(account != null && account.noPointers()){
                if(UIHelper.showConfirmDialog("You really want to delete: " + account.getName())) {
                    app.getData().removeExternalAccount(account);
                    app.getStore().deleteExternalMoneyAccount(account);
                }
            } else {
                app.getWindow().setStatus("Can't delete externalAccount!");
            }
        });


        VBox box = new VBox();
        box.setSpacing(5);
        box.setPadding(new Insets(10,10,10,10));
        box.getChildren().addAll(moneyAccountTable, removeAccountButton, externalMoneyAccountTable,removeExternalAccountButton, newAccountButton);
        borderPane = new BorderPane();
        borderPane.setCenter(box);
    }

    private TableView<MoneyAccountInterface> getTable(boolean m, boolean o){
        TableView<MoneyAccountInterface> table = new TableView<>();
        table.setEditable(true);

        TableColumn<MoneyAccountInterface, String> accountName = new TableColumn<>("Name");
        accountName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        accountName.setCellFactory(TextFieldTableCell.forTableColumn());
        accountName.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        if(m) {
            TableColumn<MoneyAccountInterface, Number> money = new TableColumn<>("Money");
            money.setCellValueFactory(cellData -> cellData.getValue().getMoneyProperty());
            table.getColumns().add(money);
        }
        if(o) {
            TableColumn owner = new TableColumn("Owner");
            TableColumn<MoneyAccountInterface, String> ownerName = new TableColumn<>("Name");
            ownerName.setCellValueFactory(cellData -> cellData.getValue().getOwnerNameProperty());
            ownerName.setCellFactory(TextFieldTableCell.forTableColumn());
            ownerName.setOnEditCommit(event -> event.getRowValue().setOwnerName(event.getNewValue()));
            TableColumn<MoneyAccountInterface, String> ownerSurName = new TableColumn<>("Surname");
            ownerSurName.setCellValueFactory(cellData -> cellData.getValue().getOwnerSurnameProperty());
            ownerSurName.setCellFactory(TextFieldTableCell.forTableColumn());
            ownerSurName.setOnEditCommit(event -> event.getRowValue().setOwnerSurName(event.getNewValue()));
            owner.getColumns().addAll(ownerName, ownerSurName);
            table.getColumns().add(owner);
        }

        table.getColumns().addAll(accountName);
        return table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update() { }

    @Override
    public Node getContent() {
        return borderPane;
    }
}
