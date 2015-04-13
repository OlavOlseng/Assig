package olseng.ea.flatland.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import olseng.ea.EA;
import olseng.ea.flatland.ANN;
import olseng.ea.flatland.FlatlandEAFactory;
import olseng.ea.flatland.FlatlandEvaluator;
import olseng.ea.flatland.Level;


/**
 * Created by Olav on 09.03.2015.
 */
public class Flatland extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;

    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_TILE_PADDING = 2;
    public static final int MAP_DIMESNSIONS = 10;

    private int tileWidth = CANVAS_WIDTH/MAP_DIMESNSIONS;


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
        if(running) {
            stopEa();
            return;
        }
        else {
            running = true;
            stop = false;
        }
        this.ea = FlatlandEAFactory.buildEa();
        if (FlatlandEAFactory.LM == FlatlandEAFactory.LEVEL_MODE.DYNAMIC) {
            ea.reevaluateAdults = true;
        }
            plotter.init();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running && !stop) {
                    running = !ea.step();
                    System.out.println(ea);
                    plotter.addData(ea.currentGeneration, ea.bestUtility, ea.avgUtility, ea.standardDeviation);
                    if (FlatlandEAFactory.LM == FlatlandEAFactory.LEVEL_MODE.DYNAMIC) {
                        FlatlandEAFactory.generateLevels();
                    }
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

    public void clearPlot() {
        plotter.clearPlot();
    }

    Level l;
    int mapCounter = 0;
    int move = -1;
    boolean bufferFlushed = true;
    ANN agent = null;

    public void testAgent(boolean random) {
        mapCounter = (mapCounter + 1) % FlatlandEAFactory.MAPS_COUNT;
        if (!random) {
            l = FlatlandEAFactory.getFitnessEvaluator().levels.get(mapCounter).copy();
        }
        else {
            l = new Level(MAP_DIMESNSIONS, MAP_DIMESNSIONS);
            l.initialize(0.3, 0.3);
        }
        renderMap(l);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                bufferFlushed = true;
                agent = (ANN)ea.adultPool.get(0);
                System.out.println("Running agent: " + agent);
                int i = 0;
                long wakeupTime = 0;

                while (i < 60) {
                    if (agent == null) {
                        System.out.println("Agent == null.");
                        return;
                    }
                    if (!bufferFlushed || System.currentTimeMillis() < wakeupTime) {
                        try {
                            Thread.sleep(FlatlandEAFactory.SLEEPTIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    i++;
                    move = FlatlandEvaluator.getMove(l, agent);
                    l.movePlayer(move);
                    bufferFlushed = false;
                    wakeupTime = System.currentTimeMillis() + 250;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            renderMap(l);
                            bufferFlushed = true;
                        }
                    });
                }
                agent = null;
                System.out.println("Food: " + l.consumedFood + "/" + l.foodCount + " = " + (float)l.consumedFood/l.foodCount + "\nPoison: " + l.consumedPoison + "/" + l.poisonCount + " = " + (float)l.consumedPoison/l.poisonCount + "\n");
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void renderMap(Level l) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        l.setShadowing(true);
        for (int y = 0; y < MAP_DIMESNSIONS; y++){
            for (int x = 0; x < MAP_DIMESNSIONS; x++) {
                g.setFill(Color.gray(0.95 - 0.95*Math.min((l.shadow[y][x]/8.0), 1)));
                g.fillRect(x * tileWidth + CANVAS_TILE_PADDING, y * tileWidth + CANVAS_TILE_PADDING, tileWidth - 2 * CANVAS_TILE_PADDING, tileWidth - 2 * CANVAS_TILE_PADDING);

                if (l.map[y][x] == Level.TILE_FOOD) {
                    g.setFill(Color.INDIANRED);
                    g.fillOval(x * tileWidth + 3 * CANVAS_TILE_PADDING, y * tileWidth + 3 * CANVAS_TILE_PADDING, tileWidth - 6 * CANVAS_TILE_PADDING, tileWidth - 6 * CANVAS_TILE_PADDING);
                }
                else if (l.map[y][x] == Level.TILE_POISON) {
                    g.setFill(Color.CHARTREUSE);
                    g.fillOval(x * tileWidth + 3 * CANVAS_TILE_PADDING, y * tileWidth + 3 * CANVAS_TILE_PADDING, tileWidth - 6 * CANVAS_TILE_PADDING, tileWidth - 6 * CANVAS_TILE_PADDING);
                }
                else if (l.map[y][x] == Level.TILE_PLAYER) {
                    g.setFill(Color.CORNFLOWERBLUE);
                    g.fillOval(x * tileWidth + 2 * CANVAS_TILE_PADDING, y * tileWidth + 2 * CANVAS_TILE_PADDING, tileWidth - 4 * CANVAS_TILE_PADDING, tileWidth - 4 * CANVAS_TILE_PADDING);
                    g.setStroke(Color.BLACK);
                    double x1 = (x + 0.5) * tileWidth;
                    double y1 = (y + 0.5) * tileWidth;
                    double x2 = x1;
                    double y2 = y1;
                    if (l.orientation == 0) {
                        x2 = (x + 1) * tileWidth - 2 * CANVAS_TILE_PADDING;
                    }
                    else if (l.orientation == 90) {
                        y2 = y * tileWidth + 2 * CANVAS_TILE_PADDING;
                    }
                    else if (l.orientation == 180) {
                        x2 = x * tileWidth + 2 * CANVAS_TILE_PADDING;
                    }
                    else if (l.orientation == 270) {
                        y2 = (y + 1) * tileWidth - 2 * CANVAS_TILE_PADDING;
                    }
                    g.strokeLine(x1, y1, x2, y2);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
