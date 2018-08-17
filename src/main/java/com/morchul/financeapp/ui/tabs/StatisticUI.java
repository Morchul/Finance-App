package com.morchul.financeapp.ui.tabs;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.filter.TransactionFilter;
import com.morchul.financeapp.moneyaccount.MoneyAccountInterface;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import com.morchul.financeapp.ui.statistic.StatisticView;
import com.morchul.financeapp.ui.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

public class StatisticUI implements FinanceAppTab {

    private FinanceAppApplication app;
    private BorderPane borderPane;

    private ComboBox<TransactionGroup> group;
    private ComboBox<MoneyAccountInterface> account;
    private Button back;

    private Spinner<Integer> yearValue;
    private Spinner<Integer> monthValue;
    private ComboBox<String> show;

    private final StatisticView s;

    private final StatisticView.StatisticColumn january;
    private final StatisticView.StatisticColumn february;
    private final StatisticView.StatisticColumn march;
    private final StatisticView.StatisticColumn april;
    private final StatisticView.StatisticColumn mai;
    private final StatisticView.StatisticColumn june;
    private final StatisticView.StatisticColumn july;
    private final StatisticView.StatisticColumn august;
    private final StatisticView.StatisticColumn september;
    private final StatisticView.StatisticColumn october;
    private final StatisticView.StatisticColumn november;
    private final StatisticView.StatisticColumn december;

    private final StatisticView.DataRow plus;
    private final StatisticView.DataRow minus;
    private final StatisticView.DataRow total;

    private SpinnerValueFactory<Integer> monthValueFactory;
    private SpinnerValueFactory<Integer> yearValueFactory;

    public StatisticUI(FinanceAppApplication app) {
        this.app = app;
        borderPane = new BorderPane();

        monthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue());
        monthValue = new Spinner<>();
        monthValue.setValueFactory(monthValueFactory);
        monthValue.valueProperty().addListener((observable, oldValue, newValue) -> update());
        monthValue.setDisable(true);
        Label monthLabel = new Label(app.getLanguage().getString("Month") + ":");

        yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2100, LocalDate.now().getYear());
        yearValue = new Spinner<>();
        yearValue.setValueFactory(yearValueFactory);
        yearValue.valueProperty().addListener((observable, oldValue, newValue) -> update());
        Label yearLabel = new Label(app.getLanguage().getString("Year") + ":");

        show = new ComboBox<>();
        show.getItems().addAll("M", "Y");
        show.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    if (item.equals("M")) setText(app.getLanguage().getString("Month"));
                    else if (item.equals("Y")) setText(app.getLanguage().getString("Year"));
                    else setText(null);
                } else
                    setText(null);
            }
        });
        show.getSelectionModel().select("Y");
        show.valueProperty().addListener((observable, oldValue, newValue) -> {
            monthValue.setDisable(!show.getSelectionModel().getSelectedItem().equals("M"));
            update();
        });


        s = new StatisticView(app);
        january = s.createColumn(app.getLanguage().getString("JanuaryShort"));
        february = s.createColumn(app.getLanguage().getString("FebruaryShort"));
        march = s.createColumn(app.getLanguage().getString("MarchShort"));
        april = s.createColumn(app.getLanguage().getString("AprilShort"));
        mai = s.createColumn(app.getLanguage().getString("MaiShort"));
        june = s.createColumn(app.getLanguage().getString("JuneShort"));
        july = s.createColumn(app.getLanguage().getString("JulyShort"));
        august = s.createColumn(app.getLanguage().getString("AugustShort"));
        september = s.createColumn(app.getLanguage().getString("SeptemberShort"));
        october = s.createColumn(app.getLanguage().getString("OctoberShort"));
        november = s.createColumn(app.getLanguage().getString("NovemberShort"));
        december = s.createColumn(app.getLanguage().getString("DecemberShort"));

        plus = s.createDataRow(app.getLanguage().getString("Revenue"), Color.GREEN);
        minus = s.createDataRow(app.getLanguage().getString("Expenditure"), Color.RED);
        total = s.createDataRow(app.getLanguage().getString("Total"), Color.BLUE);

        HBox box = new HBox();
        group = UIHelper.getTransactionGroupComboBox(app.getData().getGroups());
        group.valueProperty().addListener((observable, oldValue, newValue) -> update());
        account = UIHelper.getMoneyAccountComboBox(app.getData().getAccounts());
        account.valueProperty().addListener((observable, oldValue, newValue) -> update());
        back = new Button(app.getLanguage().getString("Reset"));
        back.setOnAction(event -> {
            group.getSelectionModel().select(-1);
            account.getSelectionModel().select(-1);
        });
        box.setSpacing(7);
        box.setPadding(new Insets(10, 10, 0, 10));
        box.getChildren().addAll(group, account, back, yearLabel, yearValue, monthLabel, monthValue, show);
        borderPane.setTop(box);
        borderPane.setCenter(s.getView());

        update();
    }

    @Override
    public String getName() {
        return app.getLanguage().getString("StatisticTabTitle");
    }

    @Override
    public void update() {

        TransactionFilter filter = new TransactionFilter("ChartFilter");
        if(group.getSelectionModel().getSelectedItem() != null)
            filter.filterGroup(group.getValue());
        if(account.getSelectionModel().getSelectedItem() != null)
            filter.filterMoneyAccount(account.getValue(), true, true);

        s.getColumns().clear();
        switch (show.getValue()){
            case "M": showMonth(filter); break;
            case "Y": showYear(filter); break;
            default: app.getWindow().setStatus("Error by displaying statistic for: " + show.getValue());
        }
        s.show();
    }

    private final DateTimeFormatter shortDate = DateTimeFormatter.ofPattern("dd.MM");
    private void showMonth(TransactionFilter filter){

        LocalDate firstOfMonth = LocalDate.of(yearValue.getValue(), monthValue.getValue(), 1);

        LocalDate startWeek = firstOfMonth.with(WeekFields.of(app.getSettings().getLanguageLocale()).dayOfWeek(), 1);
        LocalDate endWeek = startWeek.plusDays(6);

        while(!endWeek.isAfter(LocalDate.of(yearValue.getValue(), monthValue.getValue(), firstOfMonth.lengthOfMonth()))){
            StatisticView.StatisticColumn c = s.createColumn(shortDate.format(startWeek) + "-" + shortDate.format(endWeek));
            s.addColumn(c);
            c.addData(plus.createData(app.getData().utils.getTotalMoney(startWeek, endWeek, true, filter, 1)));
            c.addData(minus.createData(app.getData().utils.getTotalMoney(startWeek, endWeek, true, filter, 2)));
            c.addData(total.createData(app.getData().utils.getTotalMoney(startWeek,endWeek,true, filter, 0)));

            startWeek = endWeek.plusDays(1);
            endWeek = startWeek.plusDays(6);
        }

        StatisticView.StatisticColumn c = s.createColumn(shortDate.format(startWeek) + "-" + shortDate.format(endWeek));
        s.addColumn(c);
        c.addData(plus.createData(app.getData().utils.getTotalMoney(startWeek, endWeek, true, filter, 1)));
        c.addData(minus.createData(app.getData().utils.getTotalMoney(startWeek, endWeek, true, filter, 2)));
        c.addData(total.createData(app.getData().utils.getTotalMoney(startWeek,endWeek,true, filter, 0)));

    }

    private void showYear(TransactionFilter filter){

        s.addColumn(january);
        s.addColumn(february);
        s.addColumn(march);
        s.addColumn(april);
        s.addColumn(mai);
        s.addColumn(june);
        s.addColumn(july);
        s.addColumn(august);
        s.addColumn(september);
        s.addColumn(october);
        s.addColumn(november);
        s.addColumn(december);

        LocalDate start = LocalDate.of((yearValue.getValue() == 0) ? LocalDate.now().getYear() : yearValue.getValue() ,1,1);
        LocalDate end;

        for(int i = 1; i <= 12; ++i){
            start = start.withMonth(i).withDayOfMonth(1);
            end = start.withDayOfMonth(start.lengthOfMonth());
            StatisticView.StatisticColumn c = s.getColumn(i-1);
            c.values.clear();
            c.addData(plus.createData(app.getData().utils.getTotalMoney(start, end, true, filter, 1)));
            c.addData(minus.createData(app.getData().utils.getTotalMoney(start, end, true, filter, 2)));
            c.addData(total.createData(app.getData().utils.getTotalMoney(start,end,true, filter, 0)));
        }
    }

    @Override
    public Node getContent() {
        return borderPane;
    }
}
