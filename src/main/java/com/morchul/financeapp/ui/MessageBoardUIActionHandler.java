package com.morchul.financeapp.ui;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.transaction.Transaction;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Optional;

public class MessageBoardUIActionHandler {

    private FinanceAppApplication app;

    public MessageBoardUIActionHandler(FinanceAppApplication app){
        this.app = app;
    }

    /**
     *
     * @param message the FinanceAppMessage which will perform
     * @return true if the message was clear false if the user canceled
     * @throws Exception if any error occurs
     */
    public boolean act(FinanceAppMessage message) throws Exception {
        switch (message.getType()){
            case TRANSACTION_APPROVE: return transactionApprove(message);
            case MONEY_ACCOUNT_GOES_BANKRUPT: return moneyAccountGoesBankrupt(message);
            case SPEND_TO_MUCH_MONEY: return spendToMuchMoney();
            case GROUP_MONEY_SUPERVISER_REMEMBER: return groupMoneySupervisorRemember();
            default: return false;
        }
    }

    private boolean groupMoneySupervisorRemember(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Distribute money");
        alert.setHeaderText("You have money to distribute to your TransactionGroups");
        alert.setContentText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            app.getSettings().setGroupMoneySupervisorLastRemember(Constants.dateFormatter.format(LocalDate.now()));
            return true;
        } else {
            return false;
        }
    }

    private boolean spendToMuchMoney() throws Exception {
        app.getWindow().selectTab(Window.TabType.TRANSACTION_GROUP);
        return false;
    }

    private boolean moneyAccountGoesBankrupt(FinanceAppMessage message) throws Exception{
         if(!message.hasTransactions() || message.getTransactions() == null || message.getTransactions().size() == 0)
            throw new Exception("FinanceAppMessage TRANSACTION_APPROVE must have Transactions!");

        Dialog info = new Dialog();
        info.setTitle("Money account goes Bankrupt");
        info.setHeaderText(null);
        info.setContentText(null);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.setPadding(new Insets(12,12,12,12));

        for(int i = 0; i < message.getTransactions().size(); ++i){
            Transaction t = message.getTransactions().get(i);
            Label l = new Label();
            l.setText(t.getDescription() + " / " + -t.getAmount() + " / " + t.getDate());
            Button b = new Button("remove");
            b.setOnAction(event -> {
                if(t.hasNext() || t.hasPrevious()){
                    int answer = UIHelper.showTransactionDeleteDialog();
                    if(answer >= 0) {
                        app.getData().removeTransaction(t, answer == 1 || answer == 3, answer == 2 || answer == 3);
                        app.getLogger().info("Delete transaction/s");
                        gridPane.getChildren().removeAll(l,b);
                    }
                } else {
                    if(UIHelper.showConfirmDialog("Do you want to delete this transaction: " + t.getDescription())){
                        app.getData().removeTransaction(t, false, false);
                        app.getLogger().info("Delete single transaction");
                        gridPane.getChildren().removeAll(l,b);
                    }
                }
                app.getWindow().update();
            });
            gridPane.add(l, 0, i);
            gridPane.add(b, 1, i);
        }

        info.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        info.getDialogPane().setContent(gridPane);
        info.showAndWait();

        return false;
    }

    private boolean transactionApprove(FinanceAppMessage message) throws Exception {
        if(!message.hasTransaction())
            throw new Exception("FinanceAppMessage TRANSACTION_APPROVE must have Transaction!");
        Dialog<Float> approveDialog = new Dialog<>();

        approveDialog.setTitle("Approve Transaction");
        approveDialog.setHeaderText(null);

        ButtonType approveButton = new ButtonType("Approve", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.OTHER);
        approveDialog.getDialogPane().getButtonTypes().addAll(approveButton, deleteButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(12,12,12,12));

        TextField amount = UIHelper.getAmountField();
        amount.setText(message.getTransaction().getAmount()+"");

        gridPane.add(new Label(app.getLanguage().getString("Amount")),0,0);
        gridPane.add(amount, 1, 0);
        approveDialog.getDialogPane().setContent(gridPane);

        approveDialog.setResultConverter(dialogButton -> {
            if(dialogButton == approveButton) return Float.parseFloat(amount.getText());
            if(dialogButton == deleteButton) return 0f;
            return null;
        });

        boolean res = false;
        Optional<Float> result = approveDialog.showAndWait();
        if(result.isPresent()){
            if(result.get() == 0) {
                app.getData().removeTransaction(message.getTransaction(), false, false);
                res = true;
            } else {
                if(-result.get() + message.getTransaction().getFrom().getMoney() < 0){
                    app.getWindow().setStatus("Not enough money!");
                    return false;
                }
                message.getTransaction().setAmount(result.get());
                message.getTransaction().setApproved(true);
                app.getGroupMoneySupervisor().addMoney(result.get());
                res = true;
            }
        }
        app.getWindow().update();
        return res;
    }
}
