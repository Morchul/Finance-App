package com.morchul.financeapp.ui.tabs;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.factory.TransactionFactory;
import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.moneyaccount.ImaginaryMoneyAccount;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.ui.MessageBoardUIActionHandler;
import com.morchul.financeapp.ui.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;


public class MainTransactionUI implements FinanceAppTab {

    private TextField amountField;
    private DatePicker datePicker;
    private ComboBox<TransactionGroup> groups;
    private ComboBox<MoneyAccountInterface> from;
    private ComboBox<MoneyAccountInterface> to;
    private TextField descriptionField;
    private ComboBox<TransactionFactory.RepetitionType> repetitionType;
    private Spinner<Integer> repetitionCount;

    private MessageBoardUIActionHandler messageBoardUIActionHandler;

    private ListView<FinanceAppMessage> messageBoardListView;
    private BorderPane borderPane;
    private FinanceAppApplication app;

    public MainTransactionUI(FinanceAppApplication app) {
        this.app = app;
        messageBoardUIActionHandler = new MessageBoardUIActionHandler(app);

        borderPane = new BorderPane();
        borderPane.setLeft(getLeftSide());
        borderPane.setRight(getRightSide());
    }

    private GridPane getLeftSide(){

        amountField = UIHelper.getAmountField();
        datePicker = new DatePicker();
        groups = UIHelper.getTransactionGroupComboBox(app.getData().getGroups());
        groups.setDisable(true);
        from = UIHelper.getMoneyAccountComboBox(app.getData().getAccounts(), app.getData().getExternalAccounts());
        from.valueProperty().addListener((observable, oldValue, newValue) -> groups.setDisable(!(from.getValue() != null && (to.getValue() == null || to.getValue().getType() == MoneyAccountInterface.MoneyAccountType.EXTERNAL))));
        to = UIHelper.getMoneyAccountComboBox(app.getData().getAccounts(), app.getData().getExternalAccounts());
        to.valueProperty().addListener((observable, oldValue, newValue) -> groups.setDisable(!(from.getValue() != null && (to.getValue() == null || to.getValue().getType() == MoneyAccountInterface.MoneyAccountType.EXTERNAL))));
        descriptionField = new TextField();
        repetitionType = UIHelper.getEnumComboBox();
        repetitionType.getSelectionModel().select(TransactionFactory.RepetitionType.NONE);
        repetitionType.valueProperty().addListener((observable, oldValue, newValue) -> repetitionCount.setDisable(newValue == TransactionFactory.RepetitionType.NONE));
        repetitionCount = new Spinner<>();
        repetitionCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,365,2));
        repetitionCount.setEditable(true);
        repetitionCount.setDisable(true);
        Button plusButton = new Button("+");
        plusButton.setOnAction(event -> {
            String message;
            if(!(message = UIHelper.validInput(groups.getValue(), groups.isDisabled(), amountField.getText(), datePicker.getValue(), from.getValue(), to.getValue(), null)).equals("")) {
                app.getWindow().setStatus(message);
                return;
            }
            float amount = Float.parseFloat(amountField.getText());
            app.getData().addAllTransaction(
                    app.getFactory().transactionFactory.createTransaction(
                            datePicker.getValue(),
                            amount,
                            (groups.isDisabled()) ? null : groups.getValue(),
                            descriptionField.getText(),
                            from.getValue() == null ? new ImaginaryMoneyAccount() : from.getValue(),
                            to.getValue(),
                            repetitionCount.getValue(),
                            repetitionType.getValue()));
            app.getWindow().setStatus("Transaction/s created");
            if(to.getValue() != null && to.getValue().getType() == MoneyAccountInterface.MoneyAccountType.DEFAULT
                    && (from.getValue() == null || from.getValue().getType() != MoneyAccountInterface.MoneyAccountType.DEFAULT)
                    && !datePicker.getValue().isAfter(LocalDate.now())){
                app.getGroupMoneySupervisor().addMoney(amount);
            }
            app.getWindow().update();
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(12,12,12,12));

        int i = -1;
        gridPane.add(new Label("From: "), 0,++i);
        gridPane.add(from, 1, i);
        gridPane.add(UIHelper.comboBoxClearer(from),2,i);
        gridPane.add(new Label("To: "),0 ,++i);
        gridPane.add(to,1, i);
        gridPane.add(UIHelper.comboBoxClearer(to),2,i);
        gridPane.add(new Label("Group: *"),0,++i);
        gridPane.add(groups ,1,i, 2 ,1);
        gridPane.add(new Label("Amount: *"),0,++i);
        gridPane.add(amountField, 1, i, 2 ,1);
        gridPane.add(new Label("Date: *"),0,++i);
        gridPane.add(datePicker,1,i, 2 ,1);
        gridPane.add(new Label("Description:"),0,++i);
        gridPane.add(descriptionField, 1,i, 2 ,1);
        gridPane.add(new Label("Repetition:"),0,++i);
        gridPane.add(repetitionType,1,i, 2 ,1);
        gridPane.add(new Label("RepetitionCount:"),0,++i);
        gridPane.add(repetitionCount,1,i, 2 ,1);
        gridPane.add(plusButton,0,++i);

        return gridPane;
    }

    private ListView<FinanceAppMessage> getRightSide() {
        messageBoardListView = new ListView<>();
        messageBoardListView.setMinWidth(350);
        messageBoardListView.setItems(app.getMessageBoard().getMessages());
        messageBoardListView.setEditable(false);
        messageBoardListView.setOnMouseClicked(event -> {
            FinanceAppMessage message = messageBoardListView.getSelectionModel().getSelectedItem();
            if(message != null) {
                try {
                    if(messageBoardUIActionHandler.act(message))
                        app.getMessageBoard().unpinMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    app.getWindow().setStatus(e.toString());
                }
            }
        });
        messageBoardListView.setCellFactory(param -> new ListCell<FinanceAppMessage>() {
            @Override
            protected void updateItem(FinanceAppMessage item, boolean empty){
                super.updateItem(item, empty);
                if(item == null || empty)
                    setText(null);
                else
                    setText(item.getMessageText());
            }
        });
        return messageBoardListView;
    }

    @Override
    public String getName() {
        return app.getLanguage().getString("MainTransactionTitle");
    }

    @Override
    public void update() {

    }

    @Override
    public Node getContent() {
        return borderPane;
    }
}
