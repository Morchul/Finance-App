package com.morchul.financeapp.ui.statistic;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class StatisticView {

    private final Pane pane;
    private List<StatisticColumn> columns;
    private List<DataRow> dataRows;
    private final int ANZ_STEPS = 15;

    private StatisticDrawer drawer;

    public StatisticView() {
        columns = new ArrayList<>();
        dataRows = new ArrayList<>();
        pane = new Pane();
        pane.setPadding(new Insets(12,12,12,12));
        drawer = new StatisticDrawer(this);
    }

    public Pane getView(){
        return pane;
    }

    public void show(){
        show((float)pane.getWidth(),(float)pane.getHeight());
    }

    public void show(float width, float height){
        pane.getChildren().clear();

        float highest = 0;
        float lowest = 0;
        float different;
        float stepCount;
        for(StatisticColumn c : columns){
            for(Data d: c.values){
                if(d.value > highest)
                    highest = d.value;
                if(d.value < lowest)
                    lowest = d.value;
            }
        }
        different = highest - lowest;
        if(different == 0) return;
        float temp = ANZ_STEPS;
        int t = 10_000_000;
        while(different < t){
            t /= 10;
        }
        temp *= t / 10;
        stepCount = different + (temp - (different % temp));
        stepCount /= ANZ_STEPS;
        float lowestStep = (lowest == 0 || lowest % stepCount == 0) ? lowest : lowest - ( stepCount - (Math.abs(lowest) % stepCount));
        float highestStep = (highest % stepCount == 0) ? highest : highest + (stepCount - (highest % stepCount));
        float steps = ((highestStep - lowestStep) / stepCount) + 1;

        drawer.drawGrid(pane, width, height, steps, stepCount, lowestStep);
        drawer.drawLegend(pane, width, height, dataRows);
        for(int i = 0; i < columns.size(); ++i){
            drawer.drawColumn(pane, columns.get(i), width, height, i, steps, stepCount, lowestStep);
        }
//        System.out.println(columnWidth);
//        System.out.println(stepCount);
//        System.out.println(lowestStep);
//        System.out.println(highestStep);
//        System.out.println(lowest);
//        System.out.println(highest);
//        System.out.println(different);
//        System.out.println(height);
//        System.out.println(steps);
//        System.out.println(stepHeight);
    }

    public void addColumn(StatisticColumn column){
        columns.add(column);
    }

    public StatisticColumn createColumn(String name){
        return new StatisticColumn(name);
    }

    public DataRow createDataRow(String name, Color color){
        DataRow r = new DataRow(name, color);
        dataRows.add(r);
        return r;
    }

    public List<StatisticColumn> getColumns(){
        return columns;
    }

    public StatisticColumn getColumn(int i){
        return columns.get(i);
    }

    public class DataRow {
        public String name;
        Color color;

        public DataRow(String name, Color color){
            this.name = name;
            this.color = color;
        }

        public Data createData(float value){
            return new Data(this, value);
        }
    }

    public class Data {
        DataRow type;
        float value;
        public Data(DataRow type, float value){
            this.type = type;
            this.value = value;
        }
    }

    public class StatisticColumn{
        public String name;
        public List<Data> values;

        public StatisticColumn(String name){
            this.name = name;
            values = new ArrayList<>();
        }
        public void addData(Data data){
            values.add(data);
        }
    }
}
