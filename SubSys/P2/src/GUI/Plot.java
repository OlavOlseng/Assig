package GUI;

import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Created by Olav on 10/03/2015.
 */
public class Plot extends Group{

    LineChart<Number, Number> chart;
    XYChart.Series bestUtils;
    XYChart.Series avgUtils;
    XYChart.Series standardDeviations;

    public Plot() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        //creating the chart
        chart = new LineChart<Number,Number>(xAxis,yAxis);

        chart.setTitle("Generation development");


        init();

        this.getChildren().add(chart);
    }

    public void init() {
        chart.getData().clear();
        bestUtils = new XYChart.Series();
        bestUtils.setName("Best");
        avgUtils = new XYChart.Series();
        avgUtils.setName("Average");
        standardDeviations = new XYChart.Series();
        standardDeviations.setName("SD");
    }

    public void addData(int generation, double bestUtil, double avgUtil, double standardDeviation) {
        bestUtils.getData().add(new XYChart.Data(generation, bestUtil));
        avgUtils.getData().add(new XYChart.Data(generation, avgUtil));
        standardDeviations.getData().add(new XYChart.Data(generation, standardDeviation));
    }

    public void plot() {
        chart.getData().addAll(bestUtils, avgUtils, standardDeviations);
    }
}
