package GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import olseng.ea.olseng.ea.bitvec.BitVecEAFactory;


/**
 * Created by Olav on 09.03.2015.
 */
public class ControlPanel extends GridPane {

    private App app;

    Button b_run;
    Button b_stop;

    ComboBox<BitVecEAFactory.Problem> problemModeComboBox;
    ComboBox<BitVecEAFactory.AS_MODE> asModeComboBox;
    ComboBox<BitVecEAFactory.PS_MODE> psModeComboBox;


    TextField popCap = new TextField(Integer.toString(BitVecEAFactory.POPULATION_CAP));
    TextField genCap = new TextField(Integer.toString(BitVecEAFactory.GENERATION_CAP));
    TextField utilCap = new TextField(Double.toString(BitVecEAFactory.UTILITY_CAP));

    TextField geneMutaRate = new TextField(Double.toString(BitVecEAFactory.GENE_MUTATION_RATE));

    TextField crossoverRate = new TextField(Double.toString(BitVecEAFactory.CROSSOVER_RATE));

    TextField geneCount = new TextField(Integer.toString(BitVecEAFactory.GENE_COUNT));
    TextField geneSize = new TextField(Integer.toString(BitVecEAFactory.GENE_SIZE));

    TextField overpop = new TextField(Integer.toString(BitVecEAFactory.AS_OVERPOPULATE_COUNT));
    TextField adultRetention = new TextField(Integer.toString(BitVecEAFactory.AS_RETENTION));

    TextField boltzmannT = new TextField(Integer.toString(BitVecEAFactory.BOLTZMANN_T));
    TextField tourneySize = new TextField(Integer.toString(BitVecEAFactory.PS_TOURNAMENT_K));
    TextField tourneyWinnerChance = new TextField(Double.toString(BitVecEAFactory.PS_TOURNAMENT_EPSILON));

    public ControlPanel(App mainApp) {
        this.app = mainApp;


        initComboBoxes();

        initPanel();
            }

    private void initComboBoxes() {
        problemModeComboBox = new ComboBox<BitVecEAFactory.Problem>(FXCollections.observableArrayList(BitVecEAFactory.Problem.values()));
        problemModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.PROBLEM = problemModeComboBox.getValue();
            }
        });

        asModeComboBox = new ComboBox<BitVecEAFactory.AS_MODE>(FXCollections.observableArrayList(BitVecEAFactory.AS_MODE.values()));
        asModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.AS = asModeComboBox.getValue();
            }
        });

        psModeComboBox = new ComboBox<BitVecEAFactory.PS_MODE>(FXCollections.observableArrayList(BitVecEAFactory.PS_MODE.values()));
        psModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.PS = psModeComboBox.getValue();
            }
        });
    }

    private void initPanel() {
        int y = 1;

        popCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.POPULATION_CAP = Integer.parseInt(popCap.getText());
            }
        });
        this.add(new Text("Population Cap: "), 0, y);
        this.add(popCap, 1, y);
        y++;

        genCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.GENERATION_CAP = Integer.parseInt(genCap.getText());
            }
        });
        this.add(new Text("Generation Cap: "), 0, y);
        this.add(genCap, 1, y);
        y++;

        this.utilCap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.UTILITY_CAP = Double.parseDouble(utilCap.getText());
            }
        });
        this.add(new Text("Utility Cap: "), 0, y);
        this.add(utilCap, 1, y);
        y++;

        geneCount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.GENE_COUNT = Integer.parseInt(geneCount.getText());
            }
        });
        this.add(new Text("Gene Count: "), 0, y);
        this.add(geneCount, 1, y);
        y++;

        geneSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.GENE_SIZE = Integer.parseInt(geneSize.getText());
            }
        });
        this.add(new Text("Gene Size: "), 0 ,y);
        this.add(geneSize, 1, y);
        y++;

        geneMutaRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.GENE_MUTATION_RATE = Double.parseDouble(geneMutaRate.getText());
            }
        });
        this.add(new Text("Mutation Rate: "), 0, y);
        this.add(geneMutaRate, 1, y);
        y++;

        crossoverRate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.CROSSOVER_RATE = Double.parseDouble(crossoverRate.getText());
            }
        });
        this.add(new Text("Crossover Rate: "), 0, y);
        this.add(crossoverRate, 1, y);
        y++;


        this.add(new Text("Problem: "), 0, y);
        problemModeComboBox.setValue(BitVecEAFactory.PROBLEM);
        this.add(problemModeComboBox, 1, y);
        y++;

        this.add(new Text("Adult Selection: "), 0, y);
        asModeComboBox.setValue(BitVecEAFactory.AS);
        this.add(asModeComboBox, 1, y);
        y++;


        overpop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.AS_OVERPOPULATE_COUNT = Integer.parseInt(overpop.getText());
            }
        });
        this.add(new Text("Overpopulate: "), 0, y);
        this.add(overpop, 1, y);
        y++;

        adultRetention.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.AS_RETENTION = Integer.parseInt(adultRetention.getText());
            }
        });
        this.add(new Text("Retain: "), 0, y);
        this.add(adultRetention, 1, y);
        y++;

        this.add(new Text("Parent Selection: "), 0, y);
        psModeComboBox.setValue(BitVecEAFactory.PS);
        this.add(psModeComboBox, 1, y);
        y++;

        boltzmannT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.BOLTZMANN_T = Integer.parseInt(boltzmannT.getText());
            }
        });
        this.add(new Text("Boltzmann T: "), 0, y);
        this.add(boltzmannT, 1, y);
        y++;

        tourneySize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.PS_TOURNAMENT_K = Integer.parseInt(tourneySize.getText());
            }
        });
        this.add(new Text("Tournament Size: "), 0, y);
        this.add(tourneySize, 1, y);
        y++;

        tourneyWinnerChance.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BitVecEAFactory.PS_TOURNAMENT_EPSILON = Double.parseDouble(tourneyWinnerChance.getText());
            }
        });
        this.add(new Text("Random Chance: "), 0, y);
        this.add(tourneyWinnerChance, 1, y);
        y++;

        b_run = new Button("Run");
        b_run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.runEa();
            }
        });
        this.add(b_run, 0, y);

        b_stop = new Button("Stop");
        b_stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.stopEa();
            }
        });
    }
}
