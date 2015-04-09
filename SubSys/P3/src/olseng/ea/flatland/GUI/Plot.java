package olseng.ea.flatland.GUI;

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
        final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
        xAxis.setLabel("Generation");
        //creating the chart
        chart = new LineChart<Number,Number>(xAxis,yAxis);
        chart.setTitle("Generation development");
        chart.setCreateSymbols(false);
        init();

        this.getChildren().add(chart);
    }
    private int run = 0;
    public void init() {
        bestUtils = new XYChart.Series();
        bestUtils.setName("Best r: " + run);
        avgUtils = new XYChart.Series();
        avgUtils.setName("Average r: " + run);
        standardDeviations = new XYChart.Series();
        standardDeviations.setName("SD r: " + run);
        run++;
    }

    public void clearPlot() {
        chart.getData().clear();
        this.run = 1;
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
