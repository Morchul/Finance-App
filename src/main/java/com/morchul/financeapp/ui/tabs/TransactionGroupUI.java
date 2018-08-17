package com.morchul.financeapp.ui.tabs;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroupMoneySupervisor;
import com.morchul.financeapp.ui.UIHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class TransactionGroupUI implements FinanceAppTab {

    private final String name = "Transaction Group";
    private FinanceAppApplication app;
    private TableView<TransactionGroup> groupTable;
    private BorderPane borderPane;
    private TransactionGroupMoneySupervisor groupMoneySupervisor;

    public TransactionGroupUI(FinanceAppApplication app) {
        this.app = app;
        this.groupMoneySupervisor = app.getGroupMoneySupervisor();
        groupTable = new TableView<>();
        groupTable.setEditable(true);
        groupTable.setItems(app.getData().getGroups());

        TableColumn<TransactionGroup, String> groupName = new TableColumn<>("Name");
        groupName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        groupName.setCellFactory(TextFieldTableCell.forTableColumn());
        groupName.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));

        TableColumn<TransactionGroup, Number> money = new TableColumn<>("Money");
        money.setCellValueFactory(cellData -> cellData.getValue().getMoneyProperty());
        money.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        money.setOnEditCommit(event -> {
            if(event.getNewValue().floatValue() < 0) return ;
            float different = event.getNewValue().floatValue() - event.getOldValue().floatValue();
            if(groupMoneySupervisor.canGetMoney(different)){
                groupMoneySupervisor.getMoney(different);
                event.getRowValue().setMoney(event.getNewValue().floatValue());
                app.getData().monitor.groupChangedEvent();
            } else {
                update();
                app.getWindow().setStatus("Not enough money");
            }
        });

        groupTable.getColumns().addAll(groupName, money);

        update();

        Button newGroupButton = new Button("New Group");
        newGroupButton.setOnAction(event -> app.getData().addGroup(app.getFactory().transactionGroupFactory.createTransactionGroup("name", 0)));
        Button removeGroupButton = new Button("Remove Group");
        removeGroupButton.setOnAction(event -> {
            TransactionGroup group = groupTable.getSelectionModel().getSelectedItem();
            if(group != null && group.noPointers()) {
                if(UIHelper.showConfirmDialog("You really want to delete: " + group.getName())) {
                    app.getData().removeGroup(group);
                    app.getStore().deleteTransactionGroup(group);
                }
            } else {
                app.getWindow().setStatus("Can't delete group!");
            }
        });

        Label moneyToSpend = new Label();
        moneyToSpend.textProperty().bind(Bindings.convert(groupMoneySupervisor.moneyToSpendProperty()));


        VBox box1 = new VBox(newGroupButton,removeGroupButton);
        box1.setSpacing(5);
        box1.setPadding(new Insets(0,20,0,0));
        HBox box2 = new HBox(new Label("Money to distribute:"), moneyToSpend);
        box2.setSpacing(5);

        BorderPane bp = new BorderPane();
        bp.setLeft(box1);
        bp.setCenter(box2);

        VBox box = new VBox(groupTable, bp);
        box.setSpacing(5);
        box.setPadding(new Insets(12,12,12,12));

        borderPane = new BorderPane();
        borderPane.setCenter(box);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update() {

        groupTable.getColumns().remove(2,groupTable.getColumns().size());

        TableColumn<TransactionGroup, Number> moneyThisWeek = new TableColumn<>("Spend this Week");
        moneyThisWeek.setCellValueFactory(cellData -> {
            LocalDate start = LocalDate.now().with(WeekFields.of(app.getSettings().getLanguageLocale()).dayOfWeek(), 1);
            LocalDate end = start.plusDays(6);
            TransactionFilter filter = new TransactionFilter("");
            filter.filterGroup(cellData.getValue());
            return new SimpleFloatProperty(
                    app.getData().utils.getTotalMoney(start,end,true,filter,2)
            );
        });

        TableColumn<TransactionGroup, Number> moneyThisMonth = new TableColumn<>("Spend this Month");
        moneyThisMonth.setCellValueFactory(cellData -> {
            LocalDate start = LocalDate.now().withDayOfMonth(1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            TransactionFilter filter = new TransactionFilter("");
            filter.filterGroup(cellData.getValue());
            return new SimpleFloatProperty(
                    app.getData().utils.getTotalMoney(start,end,true,filter,2)
            );
        });

        TableColumn<TransactionGroup, Number> moneyThisYear = new TableColumn<>("Spend this Year");
        moneyThisYear.setCellValueFactory(cellData -> {
            LocalDate start = LocalDate.now().withDayOfYear(1);
            LocalDate end = start.withDayOfYear(start.lengthOfYear());
            TransactionFilter filter = new TransactionFilter("");
            filter.filterGroup(cellData.getValue());
            return new SimpleFloatProperty(
                    app.getData().utils.getTotalMoney(start,end,true,filter,2)
            );
        });

        groupTable.getColumns().addAll(moneyThisWeek, moneyThisMonth, moneyThisYear);
    }

    @Override
    public Node getContent() {
        return borderPane;
    }
}
