package olseng.ea.olseng.ea.bitvec;

import olseng.ea.DevelopmentalMethod;
import olseng.ea.EA;
import olseng.ea.FitnessEvaluator;
import olseng.ea.Generation;
import olseng.ea.olseng.ea.adultselectors.AdultSelector;
import olseng.ea.olseng.ea.adultselectors.FullReplacement;
import olseng.ea.olseng.ea.adultselectors.GenerationalMixing;
import olseng.ea.olseng.ea.adultselectors.OverProduction;
import olseng.ea.olseng.ea.parentselection.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 09.03.2015.
 */
public class BitVecEAFactory {

    public static int POPULATION_CAP = 100;
    public static int GENERATION_CAP = 500;
    public static double UTILITY_CAP = 1.0;
    public static double GENE_MUTATION_RATE = 0.005;
    public static double CROSSOVER_RATE = 0.2;

    public static int GENE_COUNT = 40;

    public static int GENE_SIZE = 1;

    public enum Problem {
        ONE_MAX,
        LOLZ,
        SS_LOCAL,
        SS_GLOBAL;

    }

    public static Problem PROBLEM = Problem.ONE_MAX;

    public static int LOLZ_THRESHOLD = 21;
    public static int ALPHABET_SIZE = 5;

    public static FitnessEvaluator getFitnessEvaluator() {
        switch(PROBLEM) {
            case ONE_MAX:
                return new MaxOneEvaluator();
            case LOLZ:
                return new LOLZEvaluator(LOLZ_THRESHOLD);
            case SS_LOCAL:
                return new LSSEvaluator();
            default:
                return null;
        }
    }

    public enum AS_MODE {
        REPLACE,
        OVERPOP,
        GENERATIONAL_MIX;
    }

    public static AS_MODE AS = AS_MODE.REPLACE;
    public static int AS_OVERPOPULATE_COUNT = 20;
    public static int AS_RETENTION = 5;

    public enum PS_MODE {
        PROPORTIONATE,
        SIGMA,
        BOLTZMANN,
        TOURNAMENT;
    }

    public static PS_MODE PS = PS_MODE.PROPORTIONATE;
    public static int BOLTZMANN_T = 50;
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
        for (int i = 0; i < POPULATION_CAP; i++) {
            BinaryGenome g = new BinaryGenome(GENE_COUNT, GENE_SIZE);
            g.randomize();
            initialPopulation.add(g);
        }

        FitnessEvaluator fe = getFitnessEvaluator();
        BitToIntVec dm = new BitToIntVec();
        if(PROBLEM == Problem.SS_GLOBAL || PROBLEM == Problem.SS_LOCAL) {
            dm.setMaxVal(ALPHABET_SIZE);
        }
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
        BitVecEAFactory.AS = AS_MODE.GENERATIONAL_MIX;
        EA ea = buildEa();
        while(run){
            run = !ea.step();
            System.out.println(ea);
        }
    }
}
