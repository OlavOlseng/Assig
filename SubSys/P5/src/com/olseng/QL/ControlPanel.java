package com.olseng.QL;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;


/**
 * Created by Olav on 09.03.2015.
 */
public class ControlPanel extends GridPane {

    private QApp app;

    Button b_run;
    Button b_extend;
    Button b_test;

    TextField iterations;
    TextField foodValue;
    TextField poisonValue;
    TextField stepCost;
    TextField tdCount;

    TextField discountFactor;
    TextField learningRate;
    TextField wobble;

    Slider delay;

    private ComboBox<String> filePaths;

    private TextArea statusField;

    public ControlPanel(QApp mainApp) {
        this.app = mainApp;
        this.setPrefWidth(200);
        initSpecials();
        initPanel();

    }

    private void initSpecials() {
        File folder = new File("Levels");
        String[] paths = folder.list();

        this.filePaths = new ComboBox<String>(FXCollections.observableArrayList(paths));

        this.filePaths.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.loadLevel(filePaths.getValue());
            }
        });
        this.filePaths.setValue(paths[0]);
        this.filePaths.setPrefWidth(this.getPrefWidth());

        this.iterations = new TextField("10000");
        this.iterations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int nums = 10000;
                try {
                    nums = Integer.valueOf(iterations.getText());
                }
                catch (Exception e) {
                    iterations.setText("10000");
                }
            }
        });

        this.discountFactor = new TextField("" + app.learner.discountRate);
        this.discountFactor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 0.99f;
                try {
                    nums = Float.parseFloat(discountFactor.getText());
                }
                catch (Exception e) {
                    discountFactor.setText("0.9");
                }
                app.learner.discountRate = nums;
            }
        });


        this.learningRate = new TextField("" + app.learner.learningRate);
        this.learningRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 0.5f;
                try {
                    nums = Float.parseFloat(learningRate.getText());
                }
                catch (Exception e) {
                    learningRate.setText("0.5");
                }
                app.learner.learningRate = nums;
            }
        });

        this.wobble = new TextField("" + app.learner.wobble);
        this.wobble.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 0.5f;
                try {
                    nums = Float.parseFloat(wobble.getText());
                }
                catch (Exception e) {
                    discountFactor.setText("0.5");
                }
                app.learner.wobble= nums;
            }
        });

        this.foodValue = new TextField("" + app.learner.SCORE_FOOD);
        this.foodValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 20;
                try {
                    nums = Float.parseFloat(foodValue.getText());
                }
                catch (Exception e) {
                    foodValue.setText("20");
                }
                app.learner.SCORE_FOOD = nums;
            }
        });

        this.poisonValue = new TextField("" + app.learner.SCORE_POISON * -1f);
        this.poisonValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 20;
                try {
                    nums = Float.parseFloat(poisonValue.getText());
                }
                catch (Exception e) {
                    poisonValue.setText("20");
                }
                app.learner.SCORE_POISON = -nums;
            }
        });

        this.stepCost = new TextField("" + app.learner.SCORE_NOTHING * -1f);
        this.stepCost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float nums = 0.01f;
                try {
                    nums = Float.parseFloat(stepCost.getText());
                }
                catch (Exception e) {
                    stepCost.setText("0.01");
                }
                app.learner.SCORE_NOTHING = -nums;
            }
        });

        this.tdCount = new TextField("" + app.learner.getMaxBackstack());
        this.tdCount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int nums = 5;
                try {
                    nums = Integer.valueOf(tdCount.getText());
                }
                catch (Exception e) {
                    tdCount.setText("5");
                }
                app.learner.setMaxBackstack(nums);
            }
        });

        this.delay = new Slider(1,300,30);
        delay.setBlockIncrement(10);
        delay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                app.delay = delay.getValue();
                System.out.println("New delay: " + app.delay);
            }
        });

        this.statusField = new TextArea("");
        this.statusField.setPrefWidth(this.getPrefWidth());
        this.statusField.setPrefHeight(80);
        this.statusField.setEditable(false);

    }

    private void initPanel() {

        int y = 1;
        this.add(new Text("com.olseng.QL.Level:"), 0, y);
        y++;
        this.add(filePaths, 0, y);
        y++;

        this.add(new Text("Iterations:"), 0, y);
        y++;
        this.add(iterations, 0, y);
        y++;

        this.add(new Text("Learning Rate: "), 0, y);
        y++;
        this.add(learningRate, 0, y);
        y++;

        this.add(new Text("Discount Factor: "), 0, y);
        y++;
        this.add(discountFactor, 0, y);
        y++;

        this.add(new Text("Wobble: "), 0, y);
        y++;
        this.add(wobble, 0, y);
        y++;

        this.add(new Text("TD-N: "), 0, y);
        y++;
        this.add(tdCount, 0, y);
        y++;

        this.add(new Text("Food Value:"), 0, y);
        y++;
        this.add(foodValue, 0, y);
        y++;

        this.add(new Text("Poison Value: "), 0, y);
        y++;
        this.add(poisonValue, 0, y);
        y++;

        this.add(new Text("Step Cost: "), 0, y);
        y++;
        this.add(stepCost, 0, y);
        y++;



        this.add(new Text("Delay: "), 0, y);
        y++;
        this.add(delay, 0, y);
        y++;

        b_run = new Button("New");
        b_run.setPrefWidth(this.getPrefWidth());
        b_run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.runQL(true, Integer.valueOf(iterations.getText()));
            }
        });
        this.add(b_run, 0, y);
        y++;

        b_extend = new Button("Extend");
        b_extend.setPrefWidth(this.getPrefWidth());
        b_extend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.runQL(false, Integer.valueOf(iterations.getText()));
            }
        });
        this.add(b_extend, 0, y);
        y++;

        b_test = new Button("Test");
        b_test.setPrefWidth(this.getPrefWidth());
        b_test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.testAgent();
            }
        });
        this.add(b_test, 0, y);
        y++;

        this.add(statusField, 0, y);
        y++;

    }

    public void setStatusMessage(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusField.setText(message);
            }
        });
    }

}
