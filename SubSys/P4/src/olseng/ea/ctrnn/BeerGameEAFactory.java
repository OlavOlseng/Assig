package olseng.ea.ctrnn;

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
public class BeerGameEAFactory {

    public static long SLEEPTIME = 50;
    public static int POPULATION_CAP = 100;
    public static int GENERATION_CAP = 300;
    public static double UTILITY_CAP = 0.0;
    public static double GENE_MUTATION_RATE = 0.06;
    public static double CROSSOVER_RATE = 0.1;
    public static int GENE_COUNT = 0;

    public static int GENE_SIZE = 8;

    public static double VALUE_CAPTURE_SMALL = 0.502;
    public static double VALUE_AVOID_BIG = 0.611;
    public static double VALUE_MISS_SMALL = 0.409;
    public static double VALUE_HIT_BIG = 0.503;

    public static int[] LAYERS = {5, 2, 2};
    public static int MAPS_COUNT = 5;

    private static BeerGameEvaluator fe = new BeerGameEvaluator(VALUE_CAPTURE_SMALL, VALUE_AVOID_BIG, VALUE_MISS_SMALL ,VALUE_HIT_BIG);
    public static BeerGameEvaluator getFitnessEvaluator() {
        return fe;
    }

    public enum AS_MODE {
        REPLACE,
        OVERPOP,
        GENERATIONAL_MIX;
    }

    public static AS_MODE AS = AS_MODE.GENERATIONAL_MIX;
    public static int AS_OVERPOPULATE_COUNT = 100;
    public static int AS_RETENTION = 25;

    public enum PS_MODE {
        PROPORTIONATE,
        SIGMA,
        BOLTZMANN,
        TOURNAMENT;
    }

    public static PS_MODE PS = PS_MODE.SIGMA;
    public static int BOLTZMANN_T = 100;
    public static int PS_TOURNAMENT_K = 10;
    public static double PS_TOURNAMENT_EPSILON = 0.1;

    public enum LEVEL_MODE {
        STANDARD,
        PULL,
        NO_WRAP;
    }

    public static LEVEL_MODE LM = LEVEL_MODE.STANDARD;

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
        int genes = 1;
        for(int i = 1; i < LAYERS.length; i++) {
            genes += LAYERS[i] * LAYERS[i - 1] + LAYERS[i] * LAYERS[i] + LAYERS[i] * 3;
        }
        GENE_COUNT = genes;
        for (int i = 0; i < POPULATION_CAP; i++) {
            BinaryGenome g = new BinaryGenome(GENE_COUNT, GENE_SIZE);
            g.randomize();
            initialPopulation.add(g);
        }

        fe = new BeerGameEvaluator(VALUE_CAPTURE_SMALL, VALUE_AVOID_BIG, VALUE_MISS_SMALL, VALUE_HIT_BIG);
        FitnessEvaluator fe = getFitnessEvaluator();

        BitToCTRNN dm = new BitToCTRNN(LAYERS);
        Generation<BinaryGenome> generation = new Generation<BinaryGenome>(
                dm,
                fe,
                new BinaryGenomeOperators(),
                initialPopulation);


        EA ea = new EA(generation, GENERATION_CAP, UTILITY_CAP);
        ea.geneMutationRate = GENE_MUTATION_RATE;
        ea.crossoverRate = CROSSOVER_RATE;
        ea.setAdultSelector(getAsModule()).setParentSelector(getPsModule());
        ea.reevaluateAdults = true;
        return ea;
    }

    public static void main(String[] args) {
        boolean run = true;
        BeerGameEAFactory.AS = AS_MODE.GENERATIONAL_MIX;
        EA ea = buildEa();
        while(run){
            run = !ea.step();
            System.out.println(ea);
        }
    }
}
