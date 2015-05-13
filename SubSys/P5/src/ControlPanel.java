import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Arrays;


/**
 * Created by Olav on 09.03.2015.
 */
public class ControlPanel extends GridPane {

    private QApp app;

    Button b_run;
    Button b_clear;
    Button b_sequence;

    Button b_random;

    public ControlPanel(QApp mainApp) {
        this.app = mainApp;

        initPanel();

    }

    private void initPanel() {
        /*
        int y = 1;

        popCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.POPULATION_CAP = Integer.parseInt(popCap.getText());
            }
        });
        this.add(new Text("Population Cap: "), 0, y);
        this.add(popCap, 1, y);
        y++;

        genCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.GENERATION_CAP = Integer.parseInt(genCap.getText());
            }
        });
        this.add(new Text("Generation Cap: "), 0, y);
        this.add(genCap, 1, y);
        y++;

        this.utilCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.UTILITY_CAP = Double.parseDouble(utilCap.getText());
            }
        });
        this.add(new Text("Utility Cap: "), 0, y);
        this.add(utilCap, 1, y);
        y++;

        geneSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.GENE_SIZE = Integer.parseInt(geneSize.getText());
            }
        });
        this.add(new Text("Gene Size: "), 0 ,y);
        this.add(geneSize, 1, y);
        y++;

        geneMutaRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.GENE_MUTATION_RATE = Double.parseDouble(geneMutaRate.getText());
            }
        });
        this.add(new Text("Mutation Rate: "), 0, y);
        this.add(geneMutaRate, 1, y);
        y++;

        crossoverRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.CROSSOVER_RATE = Double.parseDouble(crossoverRate.getText());
            }
        });
        this.add(new Text("Crossover Rate: "), 0, y);
        this.add(crossoverRate, 1, y);
        y++;


        this.add(new Text("Adult Selection: "), 0, y);
        asModeComboBox.setValue(FlatlandEAFactory.AS);
        this.add(asModeComboBox, 1, y);
        y++;


        overpop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.AS_OVERPOPULATE_COUNT = Integer.parseInt(overpop.getText());
            }
        });
        this.add(new Text("Overpopulate: "), 0, y);
        this.add(overpop, 1, y);
        y++;

        adultRetention.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.AS_RETENTION = Integer.parseInt(adultRetention.getText());
            }
        });
        this.add(new Text("Retain: "), 0, y);
        this.add(adultRetention, 1, y);
        y++;

        this.add(new Text("Parent Selection: "), 0, y);
        psModeComboBox.setValue(FlatlandEAFactory.PS);
        this.add(psModeComboBox, 1, y);
        y++;

        boltzmannT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.BOLTZMANN_T = Integer.parseInt(boltzmannT.getText());
            }
        });
        this.add(new Text("Boltzmann T: "), 0, y);
        this.add(boltzmannT, 1, y);
        y++;

        tourneySize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.PS_TOURNAMENT_K = Integer.parseInt(tourneySize.getText());
            }
        });
        this.add(new Text("Tournament Size: "), 0, y);
        this.add(tourneySize, 1, y);
        y++;

        tourneyWinnerChance.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.PS_TOURNAMENT_EPSILON = Double.parseDouble(tourneyWinnerChance.getText());
            }
        });
        this.add(new Text("Random Chance: "), 0, y);
        this.add(tourneyWinnerChance, 1, y);
        y++;

        sleepTimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.SLEEPTIME = Long.parseLong(sleepTimer.getText());
            }
        });
        this.add(new Text("Sleep Time: "), 0, y);
        this.add(sleepTimer, 1, y);
        y++;

        mapCount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.MAPS_COUNT = Integer.parseInt(mapCount.getText());
            }
        });
        this.add(new Text("Map count: "), 0, y);
        this.add(mapCount, 1, y);
        y++;

        layers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.LAYERS = parseStringToArray(layers.getText());
            }
        });
        this.add(new Text("Layers: "), 0, y);
        this.add(layers, 1, y);
        y++;

        levelModeComboBox.setValue(FlatlandEAFactory.LM);
        this.add(new Text("Level Mode: "), 0, y);
        this.add(levelModeComboBox, 1, y);
        y++;

        foodValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.VALUE_FOOD = Double.parseDouble(foodValue.getText());
            }
        });
        this.add(new Text("Food Value: "), 0, y);
        this.add(foodValue, 1, y);
        y++;

        poisonValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.VALUE_POISON = Double.parseDouble(poisonValue.getText());
            }
        });
        this.add(new Text("Poison Value: "), 0, y);
        this.add(poisonValue, 1, y);
        y++;

        b_run = new Button("Run EA");
        b_run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.runEa();
            }
        });
        this.add(b_run, 0, y);

        b_clear = new Button("Clear");
        b_clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.clearPlot();
            }
        });
        this.add(b_clear, 1, y);
        y++;

        b_random = new Button("Random Test");
        b_random.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.testAgent(true);
            }
        });
        this.add(b_random, 0, y);

        b_sequence = new Button("Sequence Test");
        b_sequence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.testAgent(false);
            }
        });
        this.add(b_sequence, 1, y);
    }

    private int[] parseStringToArray(String text) {
        text = text.replace("[","");
        text = text.replace("]","");
        text = text.replace(" ","");
        String[] vals = text.split(",");
        int[] array = new int[vals.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(vals[i]);
        }
        return array;
*/
    }

}
