package com.morchul.financeapp.ui.statistic;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class StatisticDrawer {

    private final float labelWidth = 50;
    private final float labelHeight = 20;

    private final float bottomBarHeight = 70;

    private final float legendWidth = 100;
    private final int columnDistance = 20;

    private final StatisticView view;

    public StatisticDrawer(StatisticView view){
        this.view = view;
    }

    public void drawGrid(Pane parent, float width, float height, float steps, float stepCount, float lowestStep){

        float lineWidth = width - labelWidth - legendWidth;
        float gridHeight = height - bottomBarHeight;
        float stepHeight = gridHeight / steps;

        for(int i = 0; i < steps; ++i){
            Label l = new Label((lowestStep + (i * stepCount)) + "");
            l.setLayoutX(0);
            l.setLayoutY((gridHeight - (i * stepHeight)) - (labelHeight / 2));
            Line line = new Line(
                    labelWidth,
                    gridHeight - (i * stepHeight),
                    labelWidth + lineWidth,
                    gridHeight - (i * stepHeight)
            );
            parent.getChildren().addAll(line, l);
        }
    }

    public void drawLegend(Pane parent, float width, float height, List<StatisticView.DataRow> rows){
        final float distanceToGrid = 10, legendHeight = 40;
        float posX = width - legendWidth + distanceToGrid;
        for(int i = 0; i < rows.size(); ++i){
            StatisticView.DataRow row = rows.get(i);
            Label l = new Label(row.name);
            Rectangle r = new Rectangle(15,15,row.color);

            HBox box = new HBox();
            box.getChildren().addAll(r,l);
            box.setSpacing(2);
            box.setLayoutX(posX);
            box.setLayoutY((legendHeight * i) + ((height-bottomBarHeight) / 2) - (legendHeight * (rows.size() / 2)));
            parent.getChildren().add(box);
        }
    }

    public void drawColumn(Pane parent, StatisticView.StatisticColumn column, float width, float height, int columnNumber, float steps, float stepCount, float lowestStep){

        float lineWidth = width - labelWidth - legendWidth;
        float columnWidth = (lineWidth - (columnDistance * (view.getColumns().size() - 1))) / view.getColumns().size();
        float gridHeight = height - bottomBarHeight;
        float stepHeight = gridHeight / steps;
        float posX = columnNumber * columnWidth + (columnNumber * columnDistance);

        float stepOfZeroPoint = steps - Math.abs(lowestStep / stepCount);
        float zeroPointY = stepHeight * stepOfZeroPoint;
        float dataWidth = columnWidth / column.values.size();

        for(int i = 0; i < column.values.size(); ++i){
            StatisticView.Data data = column.values.get(i);
            float dataHeight = Math.abs(data.value / stepCount) * stepHeight;
            float dataPosY = (data.value > 0) ? zeroPointY - dataHeight : zeroPointY;
            float dataPosX = posX + labelWidth + (i * dataWidth);
            Rectangle rectangle = new Rectangle(dataPosX, dataPosY, dataWidth, dataHeight);
            rectangle.setFill(data.type.color);
            Tooltip.install(rectangle, new Tooltip(data.value+""));
            parent.getChildren().add(rectangle);
        }

        final float bottomDistance = 45;

        Label l = new Label(column.name);
        l.setLayoutX(posX + labelWidth);
        l.setLayoutY(height - bottomDistance);
        //l.setRotate(-90);
        parent.getChildren().add(l);
    }

}
