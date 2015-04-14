package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import olseng.ea.EA;
import olseng.ea.ctrnn.BeerGame;
import olseng.ea.ctrnn.BeerGameEAFactory;
import olseng.ea.ctrnn.BeerGameEvaluator;
import olseng.ea.ctrnn.CTRNN;

import java.util.Arrays;


/**
 * Created by Olav on 09.03.2015.
 */
public class BeerApp extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;

    public static final double CANVAS_WIDTH = 400.0;

    public static final double TILE_HEIGHT = 400 / 15.0;
    public static final double TILE_WIDTH = 400 / 30;


    Group root;
    ControlPanel cp;
    EA ea;
    Plot plotter;
    Canvas canvas;
    TextArea stats;

    private boolean renderBufferFlushed = true;
    private boolean plotBufferFlushed = true;

    private boolean running;
    private boolean stop;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolutionary Algorithm 0.2 - BeerGame");

        root = new Group();

        cp = new ControlPanel(this);
        plotter = new Plot();
        plotter.setTranslateX(250);

        canvas = new Canvas(400, 400);
        canvas.setTranslateX(775);

        stats = new TextArea("Stats go here!");
        stats.setTranslateX(775);
        stats.setTranslateY(405);
        stats.setPrefColumnCount(33);
        stats.setEditable(false);

        root.getChildren().addAll(cp, plotter, canvas, stats);
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
        this.ea = BeerGameEAFactory.buildEa();
        ea.reevaluateAdults = true;
        plotter.init();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running && !stop) {
                    running = !ea.step();
                    System.out.println(ea);
                    plotter.addData(ea.currentGeneration, ea.bestUtility, ea.avgUtility, ea.standardDeviation);
                    if (plotBufferFlushed) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                plotter.plot();
                                plotBufferFlushed = true;
                            }
                        });
                        plotBufferFlushed = false;
                    }
                }
                running = false;


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

    int move = -1;
    CTRNN agent;
    BeerGame bg = new BeerGame(30,15);
    double[] results;
    public void testAgent() {
        bg.newDrop();
        if(BeerGameEAFactory.LM == BeerGameEAFactory.LEVEL_MODE.NO_WRAP) {
            bg.wrapping = false;
        }
        else {
            bg.wrapping = true;
        }
        results = new double[4];
        renderMap(bg);


        Runnable r = new Runnable() {
            @Override
            public void run() {
                renderBufferFlushed = true;
                agent = (CTRNN)ea.adultPool.get(0);
                agent.flush();
                System.out.println("Running agent: " + agent);
                int i = 0;
                long wakeupTime = 0;

                while (i < 600) {
                    if (agent == null) {
                        System.out.println("Agent == null.");
                        return;
                    }
                    if (!renderBufferFlushed || System.currentTimeMillis() < wakeupTime) {
                        try {
                            Thread.sleep(BeerGameEAFactory.SLEEPTIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    i++;
                    //Do post shit here
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            renderMap(bg);
                            updateStats(results);
                            renderBufferFlushed = true;
                        }
                    });
                    if (bg.done) {
                        BeerGameEvaluator.addResult(bg, results);
                        bg.newDrop();
                        //agent.flush();
                        continue;
                    }
                    BeerGameEvaluator.move(bg, agent);
                    renderBufferFlushed = false;
                    wakeupTime = System.currentTimeMillis() + BeerGameEAFactory.SLEEPTIME;
                }
                agent = null;
                //Print stats here
                System.out.println("RESULTS: " +  Arrays.toString(bg.getResult()));
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void updateStats(double[] results) {
        String s = String.format("Current test run:\n\nSmall captured: %.0f / %.0f\nBig avoided: %.0f / %.0f", results[0], results[1], results[2], results[3]);
        stats.clear();
        stats.setText(s);
    }

    private void renderMap(BeerGame bg) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.DIMGREY);
        if (bg.pulled) {
            g.setFill(Color.LIGHTGOLDENRODYELLOW);
        }
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //DRAW AGENT
        g.setFill(Color.CORNFLOWERBLUE);
        for (int i = 0; i < bg.playerSize; i++) {
            int x = (bg.playerPosition + i) % (bg.width + 1);
            g.fillRect(x * TILE_WIDTH, CANVAS_WIDTH - TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }

        //DRAW OBJECT
        if (bg.objectSize >= 5) {
            g.setFill(Color.ORANGERED);
        }
        else {
            g.setFill(Color.MEDIUMSPRINGGREEN);
        }
        for (int i = 0; i < bg.objectSize; i++) {
            int x = (bg.objectPositionX + i) % (bg.width + 1);
            g.fillRect(x * TILE_WIDTH, CANVAS_WIDTH - (bg.objectPositionY + 1) * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }
        /*
        if(bg.done) {
            int[] res = bg.getResult();
            String s = "";
            if (res[0] == 1) {
                s += "Captured: ";
            }
            else if (res[0] == 0) {
                s += "Avoided: ";
            }
            else {
                s += "Hit: ";
            }
            switch(res[1]){
                case 1:
                    s += "Small";
                break;
                default:
                    s += "Big";
            }
            System.out.println(s);
        }
        */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
