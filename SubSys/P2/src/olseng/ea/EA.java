package olseng.ea;

import olseng.ea.olseng.ea.adultselectors.AdultSelector;
import olseng.ea.olseng.ea.adultselectors.GenerationalMixing;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;
import olseng.ea.olseng.ea.bitvec.BinaryGenomeOperators;
import olseng.ea.olseng.ea.bitvec.BitToIntVec;
import olseng.ea.olseng.ea.bitvec.MaxOneEvaluator;
import olseng.ea.olseng.ea.parentselection.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Olav on 06.03.2015.
 */
public class EA {

    public Generation generation;
    public List<Phenotype> adultPool;

    /**
     * Probability that a gene is chosen for mutation.
     */
    public double geneMutationRate = 0.05;

    /**
     * Chance that a gene chosen for mutation will actually mutate.
     */
    public double geneComponentMutationRate = 0.0;

    /**
     * The probability that genes will swap during crossover
     */
    public double crossoverRate = 0.2;

    private AdultSelector adultSelector;
    private ParentSelector parentSelector;

    public int currentGeneration = 0;
    private int generationCap;

    private double utilityThreshold;

    public double bestUtility;
    public double avgUtility;
    public double variance;
    public double standardDeviation;
    public boolean reevaluateAdults = false;

    public EA(Generation initialGenotypes, int generationCap, double utilityThreshold) {
        this.generation = initialGenotypes;
        this.adultPool = new ArrayList<Phenotype>();
        this.generationCap = generationCap;
        this.utilityThreshold = utilityThreshold;
    }

    /**
     * Does parent selection on the current adultPopulation, and fills the generation with new genotypes.
     */
    private void createNextGeneration() {
        int childrenToGenerate = adultSelector.childrenToCreate;
        GeneticOperators operations = generation.getGeneticOperators();
        List<Genotype> children = new ArrayList<Genotype>(childrenToGenerate);

        parentSelector.initialize(adultPool);

        for (int i = 0; i < childrenToGenerate;) {
            List<Genotype> genomes = new ArrayList<Genotype>();
            if (crossoverRate > 0) {
                genomes = parentSelector.getParents(true);
                operations.crossover(genomes.get(0), genomes.get(1), crossoverRate);
                i++;
            }
            if(geneComponentMutationRate + geneMutationRate > 0.0) {
                if(genomes.size() == 0) {
                    genomes = parentSelector.getParents(false);
                }
                for (Genotype g : genomes) {
                    operations.mutate(g, geneMutationRate, geneComponentMutationRate);
                }
            }
            children.addAll(genomes);
            i++;
        }
        generation.genotypes = children;
    }

    /**
     *Develops the population by one generation.
     * @return true if algorithm has terminated.
     */
    public boolean step() {
        this.currentGeneration++;
        this.generation.developGenotypes();
        if(reevaluateAdults) {
            this.generation.evaluatePhenotypes((this.adultPool));
        }
        this.generation.evaluatePhenotypes(generation.phenotypes);
        this.adultPool = this.adultSelector.getSelection(adultPool, generation.phenotypes);
        Collections.sort(adultPool);
        calculateStatistics();
        if(adultPool.get(0).getUtilty() >= utilityThreshold) {
            System.out.println("Utility cap reached at generation: " + this.currentGeneration);
            return true;
        }
        if (currentGeneration >= generationCap) {
            System.out.println("Generation cap reached at generation: " + this.currentGeneration);
            return true;
        }
        createNextGeneration();
        return false;
    }

    private void calculateStatistics() {
        avgUtility = 0;
        variance = 0;
        standardDeviation = 0;

        for (int i = 0; i < adultPool.size(); i++) {
            Phenotype p = adultPool.get(i);
            avgUtility += p.getUtilty();
        }
        avgUtility /= adultPool.size();

        for (Phenotype p : adultPool) {
            variance += Math.pow(p.getUtilty() - avgUtility, 2);
        }
        variance /= adultPool.size();
        standardDeviation = Math.sqrt(variance);
        this.bestUtility = adultPool.get(0).getUtilty();
    }

    //====================GETTERS & SETTERS========================
    public AdultSelector getAdultSelector() {
        return adultSelector;
    }

    public EA setAdultSelector(AdultSelector adultSelector) {
        this.adultSelector = adultSelector;
        return this;
    }

    public ParentSelector getParentSelector() {
        return parentSelector;
    }

    public EA setParentSelector(ParentSelector parentSelector) {
        this.parentSelector = parentSelector;
        return this;
    }

    @Override
    public String toString() {
        String s = "\nGeneration: " + currentGeneration + "\n" +
                "Average utility: " + avgUtility + "\n" +
                "Standard Deviation: " + standardDeviation + "\n"+
                "Best utilty: " + adultPool.get(0).getUtilty() + "\n" +
                adultPool.get(0) + "\n";
        return s;
    }

    //=========================TEST============================
    public static void main(String[] args) {
        List<BinaryGenome> genomes = new ArrayList<BinaryGenome>();
        for (int i = 0; i < 100; i++) {
            BinaryGenome g = new BinaryGenome(40,1);
            g.randomize();
            genomes.add(g);
        }
        Generation<BinaryGenome> generation = new Generation<BinaryGenome>(new BitToIntVec(), new MaxOneEvaluator(), new BinaryGenomeOperators(), genomes);
        EA ea = new EA(generation, 500, 1.0).setAdultSelector(new GenerationalMixing(100, 150, 5)).setParentSelector(new FitnessProportionate());
        ea.geneMutationRate = 0.01;

        boolean run = true;
        while(run) {
            run = !ea.step();
            System.out.println(ea.adultPool.get(0));
        }
    }
}
