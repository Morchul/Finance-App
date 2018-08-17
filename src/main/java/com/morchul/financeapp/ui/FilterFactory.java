package com.morchul.financeapp.ui;

import com.morchul.financeapp.data.Data;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class FilterFactory {

    public static TransactionFilter createNewFilter(Data data){
        final Dialog<TransactionFilter> dialog = new Dialog<>();
        dialog.setTitle("Create new Filter");
        dialog.setHeaderText(null);

        final ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.setPadding(new Insets(10,10,10,10));

        final TextField name  = new TextField();
        final CheckBox pinned = new CheckBox("Pin with current date");
        final DatePicker currentDatePin = new DatePicker();
        currentDatePin.disableProperty().bind(pinned.selectedProperty().not());
        pinned.setSelected(false);
//        final TextField group = new TextField();
        final ComboBox<TransactionGroup> group = UIHelper.getTransactionGroupComboBox(data.getGroups());
        final DatePicker dateFrom = new DatePicker();
        final DatePicker dateTo = new DatePicker();
//        final TextField moneyAccount = new TextField();
        final ComboBox<MoneyAccountInterface> moneyAccount = UIHelper.getMoneyAccountComboBox(data.getAccounts());
//        final TextField to = new TextField();

        gridPane.add(new Label("Name"),0,0);
        gridPane.add(name, 1,0,3,1);
        gridPane.add(new Label("Group"),0,1);
        gridPane.add(group,1,1,3,1);
        gridPane.add(new Label("From"),0,2);
        gridPane.add(dateFrom, 1,2);
        gridPane.add(new Label("To"),2,2);
        gridPane.add(dateTo,3,2);
        gridPane.add(pinned, 1,3);
        gridPane.add(currentDatePin,2,3,2,1);
        gridPane.add(new Label("Account"),0,4);
        gridPane.add(moneyAccount,1,4);
//        gridPane.add(new Label("To"),2,3);
//        gridPane.add(to,3,3);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == createButton){
                if(name.getText().isEmpty()) return null;
                TransactionFilter filter = new TransactionFilter(name.getText());
                if(group.getValue() != null)
                    filter.filterGroup(group.getValue());
                if(dateFrom.getValue() != null && dateTo.getValue() != null)
                    filter.filterDate(dateFrom.getValue(), dateTo.getValue(),currentDatePin.getValue(),  pinned.isSelected());
                if(moneyAccount.getValue() != null)
                    filter.filterMoneyAccount(moneyAccount.getValue(), true, true);
                return filter;
            } else
                return null;
        });
        final Optional<TransactionFilter> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
