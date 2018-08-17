package com.morchul.financeapp.message.service;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.message.FinanceAppMessage;
import com.morchul.financeapp.message.MessageBoard;
import com.morchul.financeapp.transaction.group.TransactionGroupMoneySupervisor;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class TransactionGroupMoneySupervisorService {

    private MessageBoard board;
    private FinanceAppApplication app;
    private FinanceAppMessage rememberMessage;
    private TransactionGroupMoneySupervisor supervisor;

    public TransactionGroupMoneySupervisorService(TransactionGroupMoneySupervisor supervisor, MessageBoard board, FinanceAppApplication app) {
        this.board = board;
        this.app = app;
        this.supervisor = supervisor;

        check();
    }

    private void check(){
        String interval = app.getSettings().getGroupMoneySupervisorInterval();
        String lastRemember = app.getSettings().getGroupMoneySupervisorLastRemember();

        if(getNextRememberDate(interval, getLastRemember(lastRemember)).isAfter(LocalDate.now())) {
            return ;
        } else {
            if(supervisor.getMoney() != 0) {
                createMessage();
                board.pinMessage(rememberMessage);
            }
        }
    }

    private LocalDate getLastRemember(String lastRemember){
        return LocalDate.parse(lastRemember, Constants.dateFormatter);
    }

    private LocalDate getNextRememberDate(String interval, LocalDate lastRemember){
        LocalDate nextRememberDate;
        switch (interval){
            case "W": nextRememberDate = lastRemember.with(WeekFields.of(app.getSettings().getLanguageLocale()).dayOfWeek(), 1).plusDays(7); break;
            case "M": nextRememberDate = lastRemember.withDayOfMonth(1).plusMonths(1); break;
            case "Y": nextRememberDate = lastRemember.withDayOfYear(1).plusYears(1); break;
            default: nextRememberDate = lastRemember;
        }
        return nextRememberDate;
    }

    private void createMessage(){
        rememberMessage = new FinanceAppMessage() {
            @Override
            public Type getType() {
                return Type.GROUP_MONEY_SUPERVISER_REMEMBER;
            }

            @Override
            public String getMessageText() {
                return "You have money to distribute to your TransactionGroups";
            }
        };
    }
}
