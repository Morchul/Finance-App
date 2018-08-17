package com.morchul.financeapp.filter;

import com.morchul.financeapp.Constants;
import com.morchul.financeapp.moneyaccount.MoneyAccount;
import com.morchul.financeapp.transaction.group.SimpleTransactionGroup;
import org.json.JSONObject;

import java.time.LocalDate;

import static com.morchul.financeapp.json.TransactionConverter.TRANSACTION_FILTER_UUID;

public class FilterConverter {

    static final String TRANSACTION_GROUP_FILTER = "transactionFilterGroup";
    static final String TRANSACTION_DATE_FILTER_FROM = "transactionFilterDateFrom";
    static final String TRANSACTION_DATE_FILTER_TO = "transactionFilterDateTo";
    static final String TRANSACTION_DATE_PINNED = "transactionDatePinned";
    static final String TRANSACTION_DATE_FILTER_PINNED_DATE = "transactionDateFilterPinnedDate";
    static final String TRANSACTION_MONEY_ACCOUNT_FILTER = "transactionFilterMoneyAccount";
    private static final String TRANSACTION_FILTER_NAME = "transactionFilterName";


    public static JSONObject filterToJSON(TransactionFilter filter){
        JSONObject json = new JSONObject()
                .put(TRANSACTION_FILTER_UUID, filter.getUUID())
                .put(TRANSACTION_FILTER_NAME, filter.getName());
        for(TransactionFilterInterface f : filter.getFilter()){
            f.addFilterToJSONObject(json);
        }
        return json;
    }

    public static TransactionFilter JSONToFilter(JSONObject json){
        TransactionFilter filter = new TransactionFilter(json.getString(TRANSACTION_FILTER_UUID), json.getString(TRANSACTION_FILTER_NAME));
        if(json.has(TRANSACTION_DATE_FILTER_FROM)){
            filter.filterDate(LocalDate.parse(json.getString(TRANSACTION_DATE_FILTER_FROM), Constants.dateFormatter) ,LocalDate.parse(json.getString(TRANSACTION_DATE_FILTER_TO), Constants.dateFormatter),LocalDate.parse(json.getString(TRANSACTION_DATE_FILTER_PINNED_DATE), Constants.dateFormatter), json.getBoolean(TRANSACTION_DATE_PINNED));
        }
        if(json.has(TRANSACTION_GROUP_FILTER)){
            filter.filterGroup(new SimpleTransactionGroup("",0,json.getString(TRANSACTION_GROUP_FILTER))); //placeholder
        }
        if(json.has(TRANSACTION_MONEY_ACCOUNT_FILTER)){
            filter.filterMoneyAccount(new MoneyAccount("",0,json.getString(TRANSACTION_MONEY_ACCOUNT_FILTER)), true, true); //placeholder
        }
        return filter;
    }
}
