package GUI;

import boids.Boid;
import boids.BoidWorld;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



/**
 * Created by Olav on 30.01.2015.
 */
public class App extends Application {

    Group root;
    ControlPanel controlPanel;
    BoidRenderer canvas;
    long lastTick = 0;

    private BoidWorld world = new BoidWorld();

    private static AnimationTimer loop;

    public static int SIZE_CANVAS_WIDTH = 1000;
    public static int SIZE_CANVAS_HEIGHT = 720;
    public static int SIZE_SCENE_WIDTH = 1280;
    public static int SIZE_SCENE_HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initButtonGroup();
        initBoidWorld();
        initCanvas();
        initRootPane();
        initSimulation(world);

        Scene pane = new Scene(root, SIZE_SCENE_WIDTH, SIZE_SCENE_HEIGHT);

        pane.setFill(Color.LIGHTGREY);


        primaryStage.setScene(pane);
        primaryStage.setTitle("Boid Sim v420");
        primaryStage.show();
    }

    public void initCanvas() {
        this.canvas = new BoidRenderer(this.world, SIZE_CANVAS_WIDTH, SIZE_CANVAS_HEIGHT);
    }

    public void initButtonGroup() {

        this.controlPanel = new ControlPanel(SIZE_SCENE_WIDTH - SIZE_CANVAS_WIDTH, SIZE_SCENE_HEIGHT, this.world);
        controlPanel.setTranslateX(SIZE_CANVAS_WIDTH);

    }

    public void initRootPane() {
        root = new Group();
        root.getChildren().addAll(canvas, controlPanel);
    }

    public void initSimulation(BoidWorld world) {
        this.loop =  new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt = now - lastTick;
                lastTick = now;
                onTick(1 / 60f /*dt / Math.pow(10, 9)*/);
            }
        };
        lastTick = System.nanoTime();
        loop.start();
    }

    private void onTick(double dt) {
        world.onTick(dt);
        canvas.onTick(dt);
    }

    public void initBoidWorld() {
        for (int i = 0; i < world.NUMBER_BIRDS; i++) {
            this.world.addBoid(Boid.Type.BIRD);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
