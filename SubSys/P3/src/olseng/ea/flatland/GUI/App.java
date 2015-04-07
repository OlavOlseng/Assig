package olseng.ea.flatland.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import olseng.ea.EA;
import olseng.ea.flatland.FlatlandEAFactory;


/**
 * Created by Olav on 09.03.2015.
 */
public class App extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;

    Group root;
    ControlPanel cp;
    EA ea;
    Plot plotter;
    Canvas canvas;

    private boolean running;
    private boolean stop;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolutionary Algorithm 0.2 - Flatland");

        root = new Group();

        cp = new ControlPanel(this);
        plotter = new Plot();
        plotter.setTranslateX(250);

        canvas = new Canvas(400, 400);
        canvas.setTranslateX(775);

        root.getChildren().addAll(cp, plotter, canvas);
        Scene s =  new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public void runEa() {
        canvas.getGraphicsContext2D().setFill(Color.OLDLACE);
        canvas.getGraphicsContext2D().fillRect(0,0,400,400);
        if(running) {
            stopEa();
            return;
        }
        else {
            running = true;
            stop = false;
        }
        this.ea = FlatlandEAFactory.buildEa();
        plotter.init();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running && !stop) {
                    running = !ea.step();
                    System.out.println(ea);
                    plotter.addData(ea.currentGeneration, ea.bestUtility, ea.avgUtility, ea.standardDeviation);
                }
                running = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        plotter.plot();
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public void stopEa() {
        this.stop = true;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void clearPlot() {
        plotter.clearPlot();
    }
}
