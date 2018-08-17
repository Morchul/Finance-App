package com.morchul.financeapp;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FinanceAppPreLoader extends Preloader {

    private Stage stage;
    private ProgressBar bar;
    private Label title;

    public BorderPane getLoadingScreen(){
        BorderPane borderPane = new BorderPane();

        bar = new ProgressBar();
        bar.setMinWidth(200);
        title = new Label("FinanceApp");
        title.setMinWidth(200);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(title,0,0);
        gridPane.add(bar, 0,1);
        borderPane.setCenter(bar);

        return borderPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        Scene scene = new Scene(getLoadingScreen(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Load ...");
        stage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification arg){
        if(arg instanceof ProgressNotification){
            bar.setProgress(((ProgressNotification)arg).getProgress());
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification){
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
}
