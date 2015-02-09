package GUI;

import boids.Bird;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;

/**
 * Created by Olav on 03.02.2015.
 */
public class ControlPanel extends VBox {

    Slider cohesionSlider;
    Slider alignmentSlider;
    Slider separationSlider;
    Slider evasionSlider;

    public ControlPanel(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
        this.setPadding(new Insets(5, 5, 5, 5));
        initSliders();
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

    private void addChild(Node n) {
        this.setMargin(n, new Insets(5,5,5,5));
        this.getChildren().add(n);
    }
}
