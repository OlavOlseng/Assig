package GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import olseng.ea.EA;
import olseng.ea.olseng.ea.bitvec.BitVecEAFactory;

/**
 * Created by Olav on 09.03.2015.
 */
public class App extends Application {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;


    Group root;
    ControlPanel cp;
    EA ea;
    private boolean running;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolutionary Algorithm 0.1 - Binary Sequences");

        root = new Group();

        cp = new ControlPanel(this);
        root.getChildren().add(cp);
        Scene s =  new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public void runEa() {
        this.ea = BitVecEAFactory.buildEa();
        running = true;
        while(running) {
            running = !ea.step();
            System.out.println(ea);
        }
    }

    public void stopEa() {
        this.running = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
