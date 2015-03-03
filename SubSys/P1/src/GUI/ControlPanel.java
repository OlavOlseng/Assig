package GUI;

import boids.Bird;
import boids.Boid;
import boids.BoidWorld;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Created by Olav on 03.02.2015.
 */
public class ControlPanel extends VBox {

    Slider cohesionSlider;
    Slider alignmentSlider;
    Slider separationSlider;
    Slider evasionSlider;

    FlowPane obstaclePane;
    FlowPane predatorPane;

    Button addPredatorButton;
    Button wipePredatorButton;
    Button addObstacleButton;
    Button wipeObstacleButton;

    BoidWorld world;

    public ControlPanel(int width, int height, BoidWorld world) {
        this.setWidth(width);
        this.setHeight(height);
        this.setPadding(new Insets(5, 5, 5, 5));
        this.world = world;
        initSliders();
        initButtons();
    }


    private void initSliders() {
        cohesionSlider = new Slider();
        cohesionSlider.setMax(Bird.SCALE_COHESION_MAX);
        cohesionSlider.setMin(0);
        cohesionSlider.setBlockIncrement(5);
        cohesionSlider.setMajorTickUnit(10);

        cohesionSlider.setValue(Bird.SCALE_COHESION);
        cohesionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Bird.SCALE_COHESION = newValue.floatValue();
            }
        });


        alignmentSlider = new Slider();
        alignmentSlider.setMax(Bird.SCALE_ALIGNMENT_MAX);
        alignmentSlider.setMin(0);
        alignmentSlider.setBlockIncrement(0.20);

        alignmentSlider.setValue(Bird.SCALE_ALIGNMENT);
        alignmentSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Bird.SCALE_ALIGNMENT = newValue.floatValue();
            }
        });

        separationSlider = new Slider();
        separationSlider.setMax(Bird.SCALE_SEPARATION_MAX);
        separationSlider.setMin(0);
        separationSlider.setBlockIncrement(5);

        separationSlider.setValue(Bird.SCALE_SEPARATION);
        separationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Bird.SCALE_SEPARATION = newValue.floatValue();
            }
        });

        evasionSlider = new Slider();
        evasionSlider.setMax(Bird.SCALE_EVASION_MAX);
        evasionSlider.setMin(0);
        evasionSlider.setBlockIncrement(50);
        evasionSlider.setMajorTickUnit(200);

        evasionSlider.setValue(Bird.SCALE_EVASION);
        evasionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Bird.SCALE_EVASION = newValue.floatValue();
            }
        });

        addChild(new Text("Cohesion"));
        addChild(cohesionSlider);
        addChild(new Text("Alignment"));
        addChild(alignmentSlider);
        addChild(new Text("Separation"));
        addChild(separationSlider);
        addChild(new Text("Evasion"));
        addChild(evasionSlider);



    }

    private void initButtons() {
        obstaclePane = new FlowPane();
        addObstacleButton = new Button("Add");
        addObstacleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.addBoid(Boid.Type.OBSTACLE);
            }
        });
        wipeObstacleButton = new Button("Wipe");
        wipeObstacleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.wipe(Boid.Type.OBSTACLE);
            }
        });
        obstaclePane.getChildren().addAll(addObstacleButton, wipeObstacleButton);

        predatorPane = new FlowPane();
        addPredatorButton = new Button("Add");
        addPredatorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.addBoid(Boid.Type.PREDATOR);
            }
        });
        wipePredatorButton = new Button("Wipe");
        wipePredatorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.wipe(Boid.Type.PREDATOR);
            }
        });
        predatorPane.getChildren().addAll(addPredatorButton, wipePredatorButton);

        this.addChild(new Text("Obstacles"));
        this.addChild(obstaclePane);
        this.addChild(new Text("Preadtors"));
        this.addChild(predatorPane);
    }

    private void addChild(Node n) {
        this.setMargin(n, new Insets(5,5,5,5));
        this.getChildren().add(n);
    }
}
