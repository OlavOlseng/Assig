package olseng.ea;

import java.util.List;

/**
 * Created by Olav on 03.03.2015.
 */
public abstract class GeneticOperators<G extends Genotype> {

    /**
     * Mutates a binary genome using the native geneMutationRate and the componentMutationRate.
     * @param genome The genome to mutate.
     * @param geneMutationRate Probability of mutating a gene.
     * @param componentMutationRate Probability of mutating a component within a chosen gene.
     * @return The mutated genome.
     */
    public abstract G mutate(G genome, double geneMutationRate, double componentMutationRate);

    /**
     * Does a crossover of the two supplied genomes, with probability equal the provided crossoverRate and returns a new genome.
     * @param genome1
     * @param genome2
     * @param crossoverRate Probability that a crossover will be made pr. gene.
     * @return The two new or copied genomes.
     */
    public abstract List<G> crossover(G genome1, G genome2, double crossoverRate);
}
