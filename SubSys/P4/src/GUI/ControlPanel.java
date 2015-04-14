package GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import olseng.ea.ctrnn.BeerGameEAFactory;

import java.util.Arrays;


/**
 * Created by Olav on 09.03.2015.
 */
public class ControlPanel extends GridPane {

    private BeerApp app;

    Button b_run;
    Button b_clear;
    Button b_sequence;

    Button b_testAgent;
    ComboBox<BeerGameEAFactory.AS_MODE> asModeComboBox;


    ComboBox<BeerGameEAFactory.PS_MODE> psModeComboBox;
    ComboBox<BeerGameEAFactory.LEVEL_MODE> levelModeComboBox;

    TextField popCap = new TextField(Integer.toString(BeerGameEAFactory.POPULATION_CAP));

    TextField genCap = new TextField(Integer.toString(BeerGameEAFactory.GENERATION_CAP));

    TextField utilCap = new TextField(Double.toString(BeerGameEAFactory.UTILITY_CAP));

    TextField geneMutaRate = new TextField(Double.toString(BeerGameEAFactory.GENE_MUTATION_RATE));
    TextField crossoverRate = new TextField(Double.toString(BeerGameEAFactory.CROSSOVER_RATE));

    TextField geneSize = new TextField(Integer.toString(BeerGameEAFactory.GENE_SIZE));
    TextField overpop = new TextField(Integer.toString(BeerGameEAFactory.AS_OVERPOPULATE_COUNT));
    TextField adultRetention = new TextField(Integer.toString(BeerGameEAFactory.AS_RETENTION));
    TextField boltzmannT = new TextField(Integer.toString(BeerGameEAFactory.BOLTZMANN_T));
    TextField tourneySize = new TextField(Integer.toString(BeerGameEAFactory.PS_TOURNAMENT_K));

    TextField tourneyWinnerChance = new TextField(Double.toString(BeerGameEAFactory.PS_TOURNAMENT_EPSILON));
    TextField sleepTimer = new TextField(Long.toString(BeerGameEAFactory.SLEEPTIME));
    TextField layers = new TextField(Arrays.toString(BeerGameEAFactory.LAYERS));
    TextField captureSmall = new TextField(Double.toString(BeerGameEAFactory.VALUE_CAPTURE_SMALL));
    TextField avoidBig = new TextField(Double.toString(BeerGameEAFactory.VALUE_AVOID_BIG));
    TextField missSmall = new TextField(Double.toString(BeerGameEAFactory.VALUE_MISS_SMALL));
    TextField hitBig = new TextField(Double.toString(BeerGameEAFactory.VALUE_HIT_BIG));


    public ControlPanel(BeerApp mainApp) {
        this.app = mainApp;


        initComboBoxes();

        initPanel();
            }

    private void initComboBoxes() {
        asModeComboBox = new ComboBox<BeerGameEAFactory.AS_MODE>(FXCollections.observableArrayList(BeerGameEAFactory.AS_MODE.values()));
        asModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.AS = asModeComboBox.getValue();
            }
        });

        psModeComboBox = new ComboBox<BeerGameEAFactory.PS_MODE>(FXCollections.observableArrayList(BeerGameEAFactory.PS_MODE.values()));
        psModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.PS = psModeComboBox.getValue();
            }
        });

        levelModeComboBox = new ComboBox<BeerGameEAFactory.LEVEL_MODE>(FXCollections.observableArrayList(BeerGameEAFactory.LEVEL_MODE.values()));
        levelModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.LM = levelModeComboBox.getValue();
            }
        });
    }

    private void initPanel() {
        int y = 1;

        popCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.POPULATION_CAP = Integer.parseInt(popCap.getText());
            }
        });
        this.add(new Text("Population Cap: "), 0, y);
        this.add(popCap, 1, y);
        y++;

        genCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.GENERATION_CAP = Integer.parseInt(genCap.getText());
            }
        });
        this.add(new Text("Generation Cap: "), 0, y);
        this.add(genCap, 1, y);
        y++;

        this.utilCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.UTILITY_CAP = Double.parseDouble(utilCap.getText());
            }
        });
        this.add(new Text("Utility Cap: "), 0, y);
        this.add(utilCap, 1, y);
        y++;

        geneSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.GENE_SIZE = Integer.parseInt(geneSize.getText());
            }
        });
        this.add(new Text("Gene Size: "), 0 ,y);
        this.add(geneSize, 1, y);
        y++;

        geneMutaRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.GENE_MUTATION_RATE = Double.parseDouble(geneMutaRate.getText());
            }
        });
        this.add(new Text("Mutation Rate: "), 0, y);
        this.add(geneMutaRate, 1, y);
        y++;

        crossoverRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.CROSSOVER_RATE = Double.parseDouble(crossoverRate.getText());
            }
        });
        this.add(new Text("Crossover Rate: "), 0, y);
        this.add(crossoverRate, 1, y);
        y++;


        this.add(new Text("Adult Selection: "), 0, y);
        asModeComboBox.setValue(BeerGameEAFactory.AS);
        this.add(asModeComboBox, 1, y);
        y++;


        overpop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.AS_OVERPOPULATE_COUNT = Integer.parseInt(overpop.getText());
            }
        });
        this.add(new Text("Overpopulate: "), 0, y);
        this.add(overpop, 1, y);
        y++;

        adultRetention.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.AS_RETENTION = Integer.parseInt(adultRetention.getText());
            }
        });
        this.add(new Text("Retain: "), 0, y);
        this.add(adultRetention, 1, y);
        y++;

        this.add(new Text("Parent Selection: "), 0, y);
        psModeComboBox.setValue(BeerGameEAFactory.PS);
        this.add(psModeComboBox, 1, y);
        y++;

        boltzmannT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.BOLTZMANN_T = Integer.parseInt(boltzmannT.getText());
            }
        });
        this.add(new Text("Boltzmann T: "), 0, y);
        this.add(boltzmannT, 1, y);
        y++;

        tourneySize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.PS_TOURNAMENT_K = Integer.parseInt(tourneySize.getText());
            }
        });
        this.add(new Text("Tournament Size: "), 0, y);
        this.add(tourneySize, 1, y);
        y++;

        tourneyWinnerChance.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.PS_TOURNAMENT_EPSILON = Double.parseDouble(tourneyWinnerChance.getText());
            }
        });
        this.add(new Text("Random Chance: "), 0, y);
        this.add(tourneyWinnerChance, 1, y);
        y++;

        sleepTimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.SLEEPTIME = Long.parseLong(sleepTimer.getText());
            }
        });
        this.add(new Text("Sleep Time: "), 0, y);
        this.add(sleepTimer, 1, y);
        y++;

        layers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.LAYERS = parseStringToArray(layers.getText());
            }
        });
        this.add(new Text("Layers: "), 0, y);
        this.add(layers, 1, y);
        y++;

        levelModeComboBox.setValue(BeerGameEAFactory.LM);
        this.add(new Text("Level Mode: "), 0, y);
        this.add(levelModeComboBox, 1, y);
        y++;

        captureSmall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.VALUE_CAPTURE_SMALL = Double.parseDouble(captureSmall.getText());
            }
        });
        this.add(new Text("Capture Small: "), 0, y);
        this.add(captureSmall, 1, y);
        y++;

        avoidBig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.VALUE_AVOID_BIG = Double.parseDouble(avoidBig.getText());
            }
        });
        this.add(new Text("Avoid Big: "), 0, y);
        this.add(avoidBig, 1, y);
        y++;

        missSmall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.VALUE_MISS_SMALL = Double.parseDouble(missSmall.getText());
            }
        });
        this.add(new Text("Miss Small: "), 0, y);
        this.add(missSmall, 1, y);
        y++;

        hitBig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BeerGameEAFactory.VALUE_HIT_BIG= Double.parseDouble(hitBig.getText());
            }
        });
        this.add(new Text("Hit Big: "), 0, y);
        this.add(hitBig, 1, y);
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

        b_testAgent = new Button("Test");
        b_testAgent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.testAgent();
            }
        });
        this.add(b_testAgent, 0, y);

        /*
        b_sequence = new Button("Sequence Test");
        b_sequence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.testAgent();
            }
        });
        this.add(b_sequence, 1, y);
        */
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
    }
}
