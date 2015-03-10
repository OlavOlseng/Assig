package olseng.ea.olseng.ea.bitvec;

import olseng.ea.GeneticOperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Currently does not support component mutation, only gene mutation
 * Created by Olav on 06.03.2015.
 */
public class BinaryGenomeOperators extends GeneticOperators<BinaryGenome> {


    @Override
    public BinaryGenome mutate(BinaryGenome genome, double geneMutationRate, double componentMutationRate) {
        for (int i = 0; i < genome.geneCount; i++) {
            //check if gene should mutate
            if((Math.random() < geneMutationRate)) {
                int geneIndex = (int)(Math.random() * genome.geneSize);
                genome.genotype.flip(i * genome.geneSize + geneIndex);
            }
        }

        return genome;
    }

    @Override
    public List<BinaryGenome> crossover(BinaryGenome genome1, BinaryGenome genome2, double crossoverRate) {
        for (int i = 0; i < genome1.geneCount; i++) {
            if (!(Math.random() < crossoverRate)) {
                continue;
            }
            BitSet temp = genome1.getGene(i);
            genome1.setGene(genome2.getGene(i), i);
            genome2.setGene(temp, i);
        }

        return new ArrayList<BinaryGenome>(Arrays.asList(genome1, genome2));
    }


    //=====================TEST============================
    public static void main(String[] args) {
        //Mutation test
        BinaryGenomeOperators go = new BinaryGenomeOperators();
        BinaryGenome j = new BinaryGenome(20,1);
        j.randomize();
        System.out.println(j.genotype);
        BinaryGenome mutated = go.mutate(j, 1,0.5);
        System.out.println(mutated.genotype);

    }
}
