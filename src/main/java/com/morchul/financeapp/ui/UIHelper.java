package com.morchul.financeapp.ui;

import com.morchul.financeapp.factory.TransactionFactory;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.ImaginaryMoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.ImaginaryTransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.utilclasses.TransactionEdit;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Optional;

public class UIHelper {

    public static TransactionEdit showTransactionEditDialog(Transaction t){
        final Dialog<TransactionEdit> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText(null);

        final ButtonType edit = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(edit, ButtonType.CANCEL);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.setPadding(new Insets(10,10,10,10));

        final ComboBox<TransactionGroup> group = getTransactionGroupComboBox(t.getData().getGroups());
        group.getSelectionModel().select(t.getGroup());
        final ComboBox<MoneyAccountInterface> from = getMoneyAccountComboBox(t.getData().getAccounts(), t.getData().getExternalAccounts());
        from.getSelectionModel().select(t.getFrom().getType() == MoneyAccountInterface.MoneyAccountType.IMAGINARY ? null : t.getFrom());
        final ComboBox<MoneyAccountInterface> to = getMoneyAccountComboBox(t.getData().getAccounts(), t.getData().getExternalAccounts());
        to.getSelectionModel().select(t.getTo().getType() == MoneyAccountInterface.MoneyAccountType.IMAGINARY ? null : t.getTo());

        group.setDisable(!(from.getValue() != null && to.getValue() == null));
        from.valueProperty().addListener((observable, oldValue, newValue) -> group.setDisable(!(from.getValue() != null && (to.getValue() == null || to.getValue().getType() == MoneyAccountInterface.MoneyAccountType.EXTERNAL))));
        to.valueProperty().addListener((observable, oldValue, newValue) -> group.setDisable(!(from.getValue() != null && (to.getValue() == null || to.getValue().getType() == MoneyAccountInterface.MoneyAccountType.EXTERNAL))));
        final DatePicker datePicker = new DatePicker();
        datePicker.setValue(t.getDate());
        final TextField amount = getAmountField();
        amount.setText(t.getAmount()+"");

        gridPane.add(new Label("From"),0,0);
        gridPane.add(from, 1, 0);
        gridPane.add(UIHelper.comboBoxClearer(from),2,0);
        gridPane.add(new Label("To"),0,1);
        gridPane.add(to, 1, 1);
        gridPane.add(UIHelper.comboBoxClearer(to),2,1);
        gridPane.add(new Label("Group"),0,2);
        gridPane.add(group, 1, 2, 2, 1);
        gridPane.add(new Label("Amount"),0,3);
        gridPane.add(amount, 1,3, 2, 1);
        gridPane.add(new Label("Date"),0,4);
        gridPane.add(datePicker,1,4, 2, 1);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == edit){
                if(to.getValue() == null && from.getValue() == null) return null;
                return new TransactionEdit(
                        group.isDisabled() ? null : group.getValue(),
                        from.getValue(),
                        t.getFrom(),
                        to.getValue(),
                        t.getTo(),
                        Float.parseFloat(amount.getText()),
                        t.getAmount(),
                        datePicker.getValue(),
                        group.isDisabled()
                );
            } else
                return null;
        });

        final Optional<TransactionEdit> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     *
     * @return  -1 = cancel // 0 = false, false // 1 = false, true // 2 = true, false // 3 = true, true
     */
    public static int showTransactionDeleteDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setContentText("You delete a transaction of a chain would you like to delete only this or entire chain?");

        ButtonType buttonTypeOne = new ButtonType("Only this");
        ButtonType buttonTypeTwo = new ButtonType("this and future");
        ButtonType buttonTypeThree = new ButtonType("this and previous");
        ButtonType buttonTypeFour = new ButtonType("all");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            return 0;
        } else if (result.get() == buttonTypeTwo) {
            return 1;
        } else if (result.get() == buttonTypeThree) {
            return 2;
        } else if(result.get() == buttonTypeFour) {
            return 3;
        } else {
            return -1;
        }
    }

    public static boolean showConfirmDialog(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

//    public static ComboBox<TransactionFilter> getTransactionFilterComboBox(ObservableList<TransactionFilter> filter){
//        ComboBox<TransactionFilter> comboBox = new ComboBox<>();
//        comboBox.getItems().addAll(filter);
//        Callback<ListView<TransactionFilter>, ListCell<TransactionFilter>> i = new Callback<ListView<TransactionFilter>, ListCell<TransactionFilter>>() {
//            @Override
//            public ListCell<TransactionFilter> call(ListView<TransactionFilter> param) {
//                return new ListCell<TransactionFilter>() {
//                    @Override
//                    public void updateItem(TransactionFilter item, boolean empty){
//                        super.updateItem(item, empty);
//                        if(item != null && !empty)
//                            setText(item.getName());
//                        else
//                            setText(null);
//                    }
//                };
//            }
//        };
//        filter.addListener((ListChangeListener<TransactionFilter>) c  -> {
//            comboBox.getItems().clear();
//            comboBox.getItems().addAll(filter);
//            comboBox.valueProperty().set(null);
//        });
//        comboBox.setCellFactory(i);
//        comboBox.setButtonCell(i.call(null));
//        return comboBox;
//    }

    public static <T> Button comboBoxClearer(ComboBox<T> comboBox){
        Button b = new Button("x");
        b.setOnAction(event -> comboBox.setValue(null));
        return b;
    }

    public static ComboBox<TransactionGroup> getTransactionGroupComboBox(ObservableList<TransactionGroup> groups ){
        ComboBox<TransactionGroup> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(groups);
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue instanceof ImaginaryTransactionGroup)
                comboBox.getSelectionModel().select(null);
        });
        Callback<ListView<TransactionGroup>, ListCell<TransactionGroup>> i = new Callback<ListView<TransactionGroup>, ListCell<TransactionGroup>>() {
            @Override
            public ListCell<TransactionGroup> call(ListView<TransactionGroup> param) {
                return new ListCell<TransactionGroup>() {
                    @Override
                    public void updateItem(TransactionGroup item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null && !empty)
                            setText(item.getName());
                        else
                            setText(null);
                    }
                };
            }
        };
        groups.addListener((ListChangeListener<TransactionGroup>) c -> {
            comboBox.getItems().clear();
            comboBox.getItems().addAll(groups);
            comboBox.valueProperty().set(null);
        });

        comboBox.setCellFactory(i);
        comboBox.setButtonCell(i.call(null));
        return comboBox;
    }

    public static TextField getAmountField(){
        TextField amount = new TextField();
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d{0,7}([.]\\d{0,2})?"))
                amount.setText(oldValue);
        });
        return amount;
    }

    @SafeVarargs
    public static ComboBox<MoneyAccountInterface> getMoneyAccountComboBox(ObservableList<MoneyAccountInterface> ... accounts){
        ComboBox<MoneyAccountInterface> comboBox = new ComboBox<>();
        for(ObservableList<MoneyAccountInterface> observableMoneyAccounts: accounts) {
            comboBox.getItems().addAll(observableMoneyAccounts);
            observableMoneyAccounts.addListener((ListChangeListener<MoneyAccountInterface>) c -> {
                comboBox.getItems().clear();
                comboBox.getItems().addAll(observableMoneyAccounts);
                comboBox.valueProperty().set(null);
            });
        }
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && newValue.getType() == MoneyAccountInterface.MoneyAccountType.IMAGINARY)
                comboBox.getSelectionModel().select(null);
        });
        Callback<ListView<MoneyAccountInterface>, ListCell<MoneyAccountInterface>> i = new Callback<ListView<MoneyAccountInterface>, ListCell<MoneyAccountInterface>>() {
            @Override
            public ListCell<MoneyAccountInterface> call(ListView<MoneyAccountInterface> param) {
                return new ListCell<MoneyAccountInterface>() {
                    @Override
                    public void updateItem(MoneyAccountInterface item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null && !empty)
                            setText(item.getName());
                    }
                };
            }
        };


        comboBox.setButtonCell(i.call(null));
        comboBox.setCellFactory(i);
        return comboBox;
    }

    public static ComboBox<TransactionFactory.RepetitionType> getEnumComboBox(){
        ComboBox<TransactionFactory.RepetitionType> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(TransactionFactory.RepetitionType.values());
        return comboBox;
    }

    public static String validInput(TransactionGroup group, boolean groupDisabled, String amount, LocalDate date, MoneyAccountInterface from, MoneyAccountInterface to, MoneyAccountInterface oldTo){
        String message = "";
        if(group == null && !groupDisabled) message += "Invalid Group / ";
        if(amount.isEmpty()) message += "Invalid Amount / ";
        if(date == null) message += "Invalid Date / ";
//        if(from == null) from.setValue(new ImaginaryMoneyAccount());
        if(from == to) message += "From and To can't be the same / ";

        MoneyAccountInterface maFrom = (from == null) ? new ImaginaryMoneyAccount() : from;
        float a = Float.parseFloat(amount);
        if(date != null && !date.isAfter(LocalDate.now())){
            if(maFrom.getMoney() - ((maFrom == oldTo) ? a * 2: a) < 0) {
                message += "The From MoneyAccount has not enough money / ";
            } else if(oldTo != null && oldTo.getMoney() - a < 0) {
                message += "The old To MoneyAccount has not enough money / ";
            }
        }
        return message;
    }

    public static String validInput(TransactionEdit te){
        return validInput(te.getGroup(), te.isGroupDisabled(), te.getAmount()+"", te.getDate(), te.getFrom(), te.getTo(), te.getOldTo());
    }
}
