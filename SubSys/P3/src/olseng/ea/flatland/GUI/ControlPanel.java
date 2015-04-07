package olseng.ea.flatland.GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import olseng.ea.flatland.FlatlandEAFactory;


/**
 * Created by Olav on 09.03.2015.
 */
public class ControlPanel extends GridPane {

    private App app;

    Button b_run;
    Button b_clear;

    ComboBox<FlatlandEAFactory.AS_MODE> asModeComboBox;
    ComboBox<FlatlandEAFactory.PS_MODE> psModeComboBox;


    TextField popCap = new TextField(Integer.toString(FlatlandEAFactory.POPULATION_CAP));
    TextField genCap = new TextField(Integer.toString(FlatlandEAFactory.GENERATION_CAP));
    TextField utilCap = new TextField(Double.toString(FlatlandEAFactory.UTILITY_CAP));

    TextField geneMutaRate = new TextField(Double.toString(FlatlandEAFactory.GENE_MUTATION_RATE));

    TextField crossoverRate = new TextField(Double.toString(FlatlandEAFactory.CROSSOVER_RATE));

    TextField geneSize = new TextField(Integer.toString(FlatlandEAFactory.GENE_SIZE));

    TextField overpop = new TextField(Integer.toString(FlatlandEAFactory.AS_OVERPOPULATE_COUNT));
    TextField adultRetention = new TextField(Integer.toString(FlatlandEAFactory.AS_RETENTION));

    TextField boltzmannT = new TextField(Integer.toString(FlatlandEAFactory.BOLTZMANN_T));
    TextField tourneySize = new TextField(Integer.toString(FlatlandEAFactory.PS_TOURNAMENT_K));
    TextField tourneyWinnerChance = new TextField(Double.toString(FlatlandEAFactory.PS_TOURNAMENT_EPSILON));

    public ControlPanel(App mainApp) {
        this.app = mainApp;


        initComboBoxes();

        initPanel();
            }

    private void initComboBoxes() {
        asModeComboBox = new ComboBox<FlatlandEAFactory.AS_MODE>(FXCollections.observableArrayList(FlatlandEAFactory.AS_MODE.values()));
        asModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.AS = asModeComboBox.getValue();
            }
        });

        psModeComboBox = new ComboBox<FlatlandEAFactory.PS_MODE>(FXCollections.observableArrayList(FlatlandEAFactory.PS_MODE.values()));
        psModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FlatlandEAFactory.PS = psModeComboBox.getValue();
            }
        });
    }

    private void initPanel() {
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

        b_run = new Button("Run");
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
    }
}
