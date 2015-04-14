package GUI;

import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

/**
 * Created by Olav on 10/03/2015.
 */
public class Plot extends Group{

    LineChart<Number, Number> chart;
    XYChart.Series bestUtils;
    XYChart.Series avgUtils;
    XYChart.Series standardDeviations;
    ArrayList<XYChart.Data> bestBuffer;
    ArrayList<XYChart.Data> avgBuffer;
    ArrayList<XYChart.Data> sdBuffer;

    public Plot() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        //creating the chart
        chart = new LineChart<Number,Number>(xAxis,yAxis);
        chart.setTitle("Generation development");
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        init();

        this.getChildren().add(chart);
    }
    private int run = 0;

    public void init() {
        bestBuffer = new ArrayList<XYChart.Data>();
        avgBuffer = new ArrayList<XYChart.Data>();
        sdBuffer = new ArrayList<XYChart.Data>();
        flushLists();
        run++;
    }

    private void flushLists() {
        bestUtils = new XYChart.Series();
        bestUtils.setName("Best r: " + run);
        avgUtils = new XYChart.Series();
        avgUtils.setName("Average r: " + run);
        standardDeviations = new XYChart.Series();
        standardDeviations.setName("SD r: " + run);
        chart.getData().addAll(bestUtils, avgUtils, standardDeviations);
    }

    public void clearPlot() {
        chart.getData().clear();
        this.run = 1;
    }

    public void addData(int generation, double bestUtil, double avgUtil, double standardDeviation) {
        bestBuffer.add(new XYChart.Data(generation, bestUtil));
        avgBuffer.add(new XYChart.Data(generation, avgUtil));
        sdBuffer.add(new XYChart.Data(generation, standardDeviation));
    }

    public void plot() {
        if(!bestBuffer.isEmpty()) {
            bestUtils.getData().addAll(bestBuffer);
            bestBuffer.clear();
            avgUtils.getData().addAll(avgBuffer);
            avgBuffer.clear();
            standardDeviations.getData().addAll(sdBuffer);
            sdBuffer.clear();
        }
    }
}
