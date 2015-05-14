package com.olseng.QL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Created by Olav on 09.03.2015.
 */
public class QApp extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;

    private static int CANVAS_WIDTH = 1000;
    private static int CANVAS_HEIGTH = 550;
    public static final int CANVAS_TILE_PADDING = 2;
    private static double MAP_DIMESNSIONS_X = 10;
    private static double MAP_DIMESNSIONS_Y = 10;

    private double tileSize = CANVAS_HEIGTH/MAP_DIMESNSIONS_Y;


    Group root;
    ControlPanel cp;
    Canvas canvas;
    ProgressBar progressBar;


    private boolean running;
    private boolean stop;
    private Level selectedLevel;
    private Level levelCopy;
    QLearner learner;
    public double delay = 100;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolutionary Algorithm 0.2 - Flatland");

        root = new Group();

        this.learner = new QLearner(4);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGTH);
        canvas.setTranslateX(200);
        cp = new ControlPanel(this);
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(WIDTH);
        progressBar.setPrefHeight(40);
        progressBar.setTranslateY(560);

        root.getChildren().addAll(cp, progressBar, canvas);
        Scene s =  new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    float progress = 0;
    boolean progressDirty = false;

    public void runQL(boolean fresh, final int iterations) {
        if(running) {
            stopQL();
            return;
        }
        else {
            running = true;
            stop = false;
        }

        setProgressStyle("");

        if (fresh) {
            this.learner.initialize();
            cp.setStatusMessage("Started new run!");
        }
        else {
            cp.setStatusMessage("Expanding training!");
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int passes = 0;
                progress = 0;
                while(running && !stop) {

                    progress = (float)passes / iterations;
                    learner.train(progress);
                    passes++;
                    running = passes < iterations;
                    //Update progress bar here
                    if(!progressDirty) {
                        progressDirty = true;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setProgress(progress);
                                progressDirty = false;
                            }
                        });
                    }
                }
                cp.setStatusMessage("Finished training!");
                running = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        renderMap(selectedLevel);
                        setProgress(progress);
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public void stopQL() {
        this.stop = true;
        this.learner.escape = true;
    }

    boolean renderBufferFlushed;
    byte[][] policy;
    long eaten;
    boolean testing = false;

    public void testAgent() {
        if(testing) {
            testing = false;
            return;
        }
        testing = true;
        levelCopy = selectedLevel.copy();
        eaten = levelCopy.foodEaten;
        policy = learner.getPolicy(eaten);
        Runnable r = new Runnable() {
            @Override
            public void run() {
            cp.setStatusMessage("Testing agent!");
            renderBufferFlushed = true;
            int i = 0;
            long wakeupTime = 0;

            while (!levelCopy.gameOver() && testing) {
                if (!renderBufferFlushed || System.currentTimeMillis() < wakeupTime) {
                    try {
                        Thread.sleep((long)delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                i++;
                int action = policy[levelCopy.getPlayerY()][levelCopy.getPlayerX()];
                if (action < 0) {
                    testing = false;
                    break;
                }
                levelCopy.movePlayer(action);

                if(eaten != levelCopy.foodEaten) {
                    eaten = levelCopy.foodEaten;
                    policy = learner.getPolicy(eaten);
                }
                //Do post shit here

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        renderMap(levelCopy);
                        renderBufferFlushed = true;

                    }
                });
                renderBufferFlushed = false;
                wakeupTime = System.currentTimeMillis() + (long)delay;
            }
            //Print stats here
            cp.setStatusMessage("Result: \nSteps: " + i + "\nPoisonEaten: " + levelCopy.consumedPoison);
            if (levelCopy.consumedPoison > 0) {
                setProgressStyle("-fx-accent: red");

            }
            else {
                setProgressStyle("");
            }
            testing = false;
        }
    };
    Thread t = new Thread(r);
    t.start();
}

    private void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    private void setProgressStyle(final String style) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setStyle(style);
            }
        });
    }

    private void renderMap(Level l) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGTH);
        l.setShadowing(true);
        byte[][] policy = learner.getPolicy(l.foodEaten);

        for (int y = 0; y < MAP_DIMESNSIONS_Y; y++){
            for (int x = 0; x < MAP_DIMESNSIONS_X; x++) {
                g.setFill(Color.gray(0.95 - 0.95*Math.min((l.shadow[y][x]/8.0), 1)));
                if (x == l.startX && y == l.startY) {
                    g.setFill(Color.DARKSLATEBLUE);
                }
                g.fillRect(x * tileSize + CANVAS_TILE_PADDING, y * tileSize + CANVAS_TILE_PADDING, tileSize - 2 * CANVAS_TILE_PADDING, tileSize - 2 * CANVAS_TILE_PADDING);

                if (l.map[y][x] > 0) {
                    g.setFill(Color.INDIANRED);
                    g.fillOval(x * tileSize + 3 * CANVAS_TILE_PADDING, y * tileSize + 3 * CANVAS_TILE_PADDING, tileSize - 6 * CANVAS_TILE_PADDING, tileSize - 6 * CANVAS_TILE_PADDING);
                }
                else if (l.map[y][x] == Level.TILE_POISON) {
                    g.setFill(Color.CHARTREUSE);
                    g.fillOval(x * tileSize + 3 * CANVAS_TILE_PADDING, y * tileSize + 3 * CANVAS_TILE_PADDING, tileSize - 6 * CANVAS_TILE_PADDING, tileSize - 6 * CANVAS_TILE_PADDING);
                }
                else if (l.map[y][x] == Level.TILE_PLAYER) {
                    g.setFill(Color.CORNFLOWERBLUE);
                    g.fillOval(x * tileSize + 2 * CANVAS_TILE_PADDING, y * tileSize + 2 * CANVAS_TILE_PADDING, tileSize - 4 * CANVAS_TILE_PADDING, tileSize - 4 * CANVAS_TILE_PADDING);
                }

                drawArrow(x, y, policy[y][x], g);
            }
        }

    }

    public void drawArrow(int x, int y, int orientation, GraphicsContext g) {
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);

        double x1 = (x + 0.5) * tileSize;
        double y1 = (y + 0.5) * tileSize;
        double x2 = x1;
        double y2 = y1;
        double dx = 0;
        double dy = 0;

        if (orientation == 3) {
            x2 += (tileSize * 0.2);
            dy = tileSize * 0.15;
        }
        else if (orientation == 2) {
            y2 += (tileSize * 0.2);
            dx = tileSize * 0.15;
        }
        else if (orientation == 1) {
            x2 -= (tileSize * 0.2);
            dy = tileSize * 0.15;
        }
        else if (orientation == 0) {
            y2 -= (tileSize  * 0.2);
            dx = tileSize * 0.15;
        }
        else {
            //No action found
            return;
        }

        //g.strokeLine(x1, y1, x2, y2);
        g.strokeLine(x2, y2, x1 + dx, y1 - dy);
        g.strokeLine(x2, y2, x1 - dx, y1 + dy);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadLevel(String value) {
        try {
            System.out.println(value);
            this.selectedLevel = Level.fromFile(value);
            this.MAP_DIMESNSIONS_X = (double)selectedLevel.width;
            this.MAP_DIMESNSIONS_Y = (double)selectedLevel.height;
            this.tileSize = Math.min((double)CANVAS_HEIGTH/MAP_DIMESNSIONS_Y,(double)CANVAS_WIDTH/MAP_DIMESNSIONS_X);
            learner.setMap(this.selectedLevel.copy());
            renderMap(this.selectedLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
