package com.morchul.financeapp.json;

import com.morchul.financeapp.FinanceAppApplication;
import com.morchul.financeapp.moneyaccount.MoneyAccount;
import com.morchul.financeapp.transaction.Transaction;
import com.morchul.financeapp.transaction.group.SimpleTransactionGroup;
import com.morchul.financeapp.transaction.group.TransactionGroup;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class TransactionConverterTest {

    private String resString =
            "{\"transactionUUID\":\"TransactionUUID\"," +
                    "\"transactionApproved\":true," +
                    "\"transactionGroup\":\"GroupUUID\"," +
                    "\"transactionAmount\":25," +
                    "\"transactionFrom\":\"FromUUID\"," +
                    "\"transactionDescription\":\"description\"," +
                    "\"transactionDate\":1534456800000," +
                    "\"transactionTo\":\"ToUUID\"}";

    private TransactionConverter converter;

    @Before
    public void init(){
        converter = new TransactionConverter(new FinanceAppApplication());
    }
    @Test
    public void TransactionToJSON(){
        LocalDate date = LocalDate.of(2018,8,17);
        TransactionGroup group = new SimpleTransactionGroup("Tg",0,"GroupUUID");
        MoneyAccount from = new MoneyAccount("from", 0, "FromUUID");
        MoneyAccount to = new MoneyAccount("to", 0, "ToUUID");
        Transaction t = new Transaction(date,25,group,true,"description","TransactionUUID", from, to);

        JSONObject res = converter.toJSON(t);
        assertEquals(res.toString(), resString);
    }

}