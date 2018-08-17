package com.morchul.financeapp.settings;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {

    private ResourceBundle bundle;
    private static Language instance = null;

    public static Language getInstance(){
        if(instance == null)
            instance = new Language();
        return instance;
    }

    private Language() { }

    public String getString(String key){
        return bundle.getString(key);
    }

    public void changeLanguage(Locale locale){
        bundle = ResourceBundle.getBundle("language", locale);
    }
}
