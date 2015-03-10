package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import olseng.ea.EA;
import olseng.ea.olseng.ea.bitvec.BitVecEAFactory;

/**
 * Created by Olav on 09.03.2015.
 */
public class App extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    Group root;
    ControlPanel cp;
    EA ea;
    Plot plotter;

    private boolean running;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolutionary Algorithm 0.1 - Binary Sequences");

        root = new Group();

        cp = new ControlPanel(this);
        plotter = new Plot();
        plotter.setTranslateX(300);


        root.getChildren().addAll(cp, plotter);
        Scene s =  new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public void runEa() {
        if(running) {
            return;
        }
        running = true;
        this.ea = BitVecEAFactory.buildEa();
        plotter.init();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running) {
                    running = !ea.step();
                    System.out.println(ea);
                    plotter.addData(ea.currentGeneration, ea.bestUtility, ea.avgUtility, ea.standardDeviation);
                }
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
        this.running = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
