package com.morchul.financeapp.settings;

import com.morchul.financeapp.FinanceAppApplication;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.*;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

public class Settings implements AppSettings {

    @Override
    public int getLookDaysInFuture() {
        return Integer.parseInt(properties.getProperty(LOOK_DAYS_IN_FUTURE,DEFAULT_LOOK_DAYS_IN_FUTURE));
    }

    @Override
    public String getStoreType() {
        return properties.getProperty(STORE_TYPE, DEFAULT_STORE_TYPE);
    }

    @Override
    public String getExternalMongoDBHost() {
        return properties.getProperty(EXTERNAL_MONGODB_HOST, DEFAULT_EXTERNAL_MONGODB_HOST);
    }

    @Override
    public Locale getLanguageLocale() {
        return Locale.forLanguageTag(properties.getProperty(LANGUAGE_LOCALE, DEFAULT_LANGUAGE_LOCALE));
    }

    @Override
    public String getGroupMoneySupervisorInterval() {
        return properties.getProperty(GROUP_MONEY_SUPERVISOR_INTERVAL, DEFAULT_GROUP_MONEY_SUPERVISOR_INTERVAL);
    }

    @Override
    public String getGroupMoneySupervisorLastRemember() {
        return properties.getProperty(GROUP_MONEY_SUPERVISOR_LAST_REMEMBER, DEFAULT_GROUP_MONEY_SUPERVISOR_LAST_REMEMBER);
    }

    @Override
    public void setGroupMoneySupervisorLastRemember(String lastRemember) {
        properties.setProperty(GROUP_MONEY_SUPERVISOR_LAST_REMEMBER, lastRemember);
    }

    private Properties properties;
    private String fileName;
    private OutputStream output;
    private InputStream input;
    private FinanceAppApplication app;

    public Settings(String fileName, FinanceAppApplication app) {
        this.fileName = fileName;
        this.app = app;
        properties = new Properties();
        load();
    }

    public void showView(){
        Dialog<SettingsData> settingsDialog = new Dialog<>();
        settingsDialog.setTitle("Settings");
        settingsDialog.setHeaderText(null);

        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        settingsDialog.getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.setPadding(new Insets(10,10,10,10));

        TextField lookDaysInFuture = new TextField(getLookDaysInFuture()+"");
        lookDaysInFuture.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                lookDaysInFuture.setText(newValue.replaceAll("[^\\d]", ""));
        });

        TextField storeHost = new TextField(getExternalMongoDBHost());

        ComboBox<String> storeType = new ComboBox<>();
        storeType.getItems().addAll("local","external");
        storeType.valueProperty().addListener((observable, oldValue, newValue) -> {
            storeHost.setDisable(storeType.getValue().equals("local"));
        });
        storeType.getSelectionModel().select(getStoreType());

        ComboBox<Locale> languageLocale = new ComboBox<>();
        languageLocale.getItems().addAll(Locale.GERMANY, Locale.US);
        languageLocale.setValue(getLanguageLocale());
        languageLocale.setCellFactory(new Callback<ListView<Locale>, ListCell<Locale>>() {
            @Override
            public ListCell<Locale> call(ListView<Locale> param) {
                return new ListCell<Locale>(){
                    @Override
                    public void updateItem(Locale item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null && !empty)
                            setText(item.getLanguage());
                    }
                };
            }
        });

        ComboBox<String> rememberInterval = new ComboBox<>();
        rememberInterval.getItems().addAll("W", "M", "Y");
        rememberInterval.setValue(getGroupMoneySupervisorInterval());

        gridPane.add(new Label("Look Days in Future:"),0,0);
        gridPane.add(lookDaysInFuture,1,0);
        gridPane.add(new Label("Language:"),0,1);
        gridPane.add(languageLocale,1,1);
        gridPane.add(new Label("Group Money distribute Remember Interval"),0,2);
        gridPane.add(rememberInterval,1,2);
        gridPane.add(new Label("Store Type"),0,3);
        gridPane.add(storeType,1,3);
        gridPane.add(new Label("MongoDB host"),0,4);
        gridPane.add(storeHost,1,4);

        settingsDialog.getDialogPane().setContent(gridPane);

        settingsDialog.setResultConverter(dialogButton ->{
            if(dialogButton == save){
                return new SettingsData(Integer.parseInt(lookDaysInFuture.getText()),
                        languageLocale.getValue(), rememberInterval.getValue(),
                        storeHost.getText(), storeType.getValue());
            } else return null;
        });

        Optional<SettingsData> result = settingsDialog.showAndWait();
        result.ifPresent(res -> {
            properties.setProperty(LOOK_DAYS_IN_FUTURE,""+res.getLookDaysInFuture());
            properties.setProperty(LANGUAGE_LOCALE, res.getLanguageLocale().getLanguage()+"-"+res.getLanguageLocale().getCountry());
            properties.setProperty(GROUP_MONEY_SUPERVISOR_INTERVAL, res.getGroupMoneyRememberInterval());
            properties.setProperty(EXTERNAL_MONGODB_HOST, res.getExternalMongoDBHost());
            properties.setProperty(STORE_TYPE, res.getStoreType());
        });
    }

    public void store(){
        try {
            output = new FileOutputStream(fileName);
            properties.store(output, null);
        } catch (IOException e){
            app.getLogger().error("Error by store properties in file: " + fileName);
            app.getLogger().error(e.getMessage(), e);
        } finally {
            try{
                if(output != null)
                    output.close();
            }catch (IOException e){
                app.getLogger().error("Error by closing OutputStream to property file!");
                app.getLogger().error(e.getMessage(), e);
            }
        }
    }

    private void load(){
        try{
            if(!new File(fileName).exists())
                input = getClass().getClassLoader().getResourceAsStream(fileName);
            else
                input = new FileInputStream(fileName);
            properties.load(input);
        } catch (IOException e){
            app.getLogger().error("Error by load properties from file: " + fileName);
            app.getLogger().error(e.getMessage(), e);
        } finally {
            try{
                if(input != null)
                    input.close();
            }catch (IOException e){
                app.getLogger().error("Error by closing inputStream to property file!");
                app.getLogger().error(e.getMessage(), e);
            }
        }
    }

    private class SettingsData{

        private int lookDaysInFuture;
        private Locale languageLocale;
        private String groupMoneyRememberInterval;
        private String externalMongoDBHost;
        private String storeType;

        public SettingsData(int lookDaysInFuture, Locale languageLocale, String groupMoneyRememberInterval, String externalMongoDBHost, String storeType){
            this.lookDaysInFuture = lookDaysInFuture;
            this.languageLocale = languageLocale;
            this.groupMoneyRememberInterval = groupMoneyRememberInterval;
            this.externalMongoDBHost = externalMongoDBHost;
            this.storeType = storeType;

        }

        public int getLookDaysInFuture() {
            return lookDaysInFuture;
        }
        public Locale getLanguageLocale(){return languageLocale;}

        private String getExternalMongoDBHost(){
            return externalMongoDBHost;
        }
        private String getStoreType(){
            return storeType;
        }

        public String getGroupMoneyRememberInterval() {
            return groupMoneyRememberInterval;
        }
    }
}
