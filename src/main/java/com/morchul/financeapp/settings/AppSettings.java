package com.morchul.financeapp.settings;

import java.util.Locale;

public interface AppSettings {

    String LOOK_DAYS_IN_FUTURE = "lookDaysInFuture";
    String DEFAULT_LOOK_DAYS_IN_FUTURE = "7";
    int getLookDaysInFuture();

    String LANGUAGE_LOCALE = "languageLocale";
    String DEFAULT_LANGUAGE_LOCALE = "US";
    Locale getLanguageLocale();

    String GROUP_MONEY_SUPERVISOR_INTERVAL = "groupMoneySupervisorInterval";
    String DEFAULT_GROUP_MONEY_SUPERVISOR_INTERVAL = "M";
    String getGroupMoneySupervisorInterval();

    String GROUP_MONEY_SUPERVISOR_LAST_REMEMBER = "groupMoneySupervisorLastRemember";
    String DEFAULT_GROUP_MONEY_SUPERVISOR_LAST_REMEMBER = "01-Jan-2000";
    String getGroupMoneySupervisorLastRemember();
    void setGroupMoneySupervisorLastRemember(String lastRemember);

}
