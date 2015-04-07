package olseng.ea.flatland;

import olseng.ea.EA;
import olseng.ea.FitnessEvaluator;
import olseng.ea.Generation;
import olseng.ea.olseng.ea.adultselectors.AdultSelector;
import olseng.ea.olseng.ea.adultselectors.FullReplacement;
import olseng.ea.olseng.ea.adultselectors.GenerationalMixing;
import olseng.ea.olseng.ea.adultselectors.OverProduction;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;
import olseng.ea.olseng.ea.bitvec.BinaryGenomeOperators;
import olseng.ea.olseng.ea.parentselection.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 07.04.2015.
 */
public class FlatlandEAFactory {

    public static int POPULATION_CAP = 100;
    public static int GENERATION_CAP = 300;
    public static double UTILITY_CAP = 1.0;
    public static double GENE_MUTATION_RATE = 0.1;
    public static double CROSSOVER_RATE = 0.3;

    public static int GENE_COUNT = 36;

    public static int GENE_SIZE = 16;

    public static FitnessEvaluator getFitnessEvaluator() {
        return new FlatlandEvaluator(5, 0.3, 0.3);
    }

    public enum AS_MODE {
        REPLACE,
        OVERPOP,
        GENERATIONAL_MIX;
    }

    public static AS_MODE AS = AS_MODE.GENERATIONAL_MIX;
    public static int AS_OVERPOPULATE_COUNT = 50;
    public static int AS_RETENTION = 2;

    public enum PS_MODE {
        PROPORTIONATE,
        SIGMA,
        BOLTZMANN,
        TOURNAMENT;
    }

    public static PS_MODE PS = PS_MODE.BOLTZMANN;
    public static int BOLTZMANN_T = 150;
    public static int PS_TOURNAMENT_K = 10;
    public static double PS_TOURNAMENT_EPSILON = 0.1;



    private static AdultSelector getAsModule() {
        switch (AS) {
            case REPLACE:
                return new FullReplacement(POPULATION_CAP);
            case OVERPOP:
                return new OverProduction(POPULATION_CAP, POPULATION_CAP + AS_OVERPOPULATE_COUNT);
            case GENERATIONAL_MIX:
                return new GenerationalMixing(POPULATION_CAP, POPULATION_CAP + AS_OVERPOPULATE_COUNT, AS_RETENTION);
            default:
                throw new InvalidStateException("Invalid AS_MODE");
        }
    }

    private static ParentSelector getPsModule() {
        switch(PS) {
            case PROPORTIONATE:
                return new FitnessProportionate();
            case SIGMA:
                return new SigmaScaling();
            case BOLTZMANN:
                return new BoltzmannScaling(BOLTZMANN_T);
            case TOURNAMENT:
                return new TournamentSelection(PS_TOURNAMENT_K, PS_TOURNAMENT_EPSILON);
            default:
                throw new InvalidStateException("Invalid PS_MODE");
        }
    }

    /**
     * Builds a binary EA given the set parameters.
     * @return
     */
    public static EA buildEa() {
        List<BinaryGenome> initialPopulation = new ArrayList<BinaryGenome>();
        int genes = 0;
        for(int i = 1; i < BitToANN.layerSizes.length; i++) {
            genes += BitToANN.layerSizes[i] * BitToANN.layerSizes[i - 1];
        }
        GENE_COUNT = genes;
        for (int i = 0; i < POPULATION_CAP; i++) {
            BinaryGenome g = new BinaryGenome(GENE_COUNT, GENE_SIZE);
            g.randomize();
            initialPopulation.add(g);
        }

        FitnessEvaluator fe = getFitnessEvaluator();

        BitToANN dm = new BitToANN();
        Generation<BinaryGenome> generation = new Generation<BinaryGenome>(
                dm,
                fe,
                new BinaryGenomeOperators(),
                initialPopulation);


        EA ea = new EA(generation, GENERATION_CAP, UTILITY_CAP);
        ea.geneMutationRate = GENE_MUTATION_RATE;
        ea.crossoverRate = CROSSOVER_RATE;
        ea.setAdultSelector(getAsModule()).setParentSelector(getPsModule());
        return ea;
    }

    public static void main(String[] args) {
        boolean run = true;
        FlatlandEAFactory.AS = AS_MODE.GENERATIONAL_MIX;
        EA ea = buildEa();
        while(run){
            run = !ea.step();
            System.out.println(ea);
        }
    }
}
