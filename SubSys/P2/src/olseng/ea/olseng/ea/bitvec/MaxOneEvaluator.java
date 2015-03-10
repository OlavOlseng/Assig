package olseng.ea.olseng.ea.bitvec;

import olseng.ea.FitnessEvaluator;

/**
 * Created by Olav on 05.03.2015.
 */
public class MaxOneEvaluator implements FitnessEvaluator<IntVec> {

    @Override
    public double evaluate(IntVec phenotype) {
        long[] longs = phenotype.getGenotype().genotype.toLongArray();
        if(longs.length < 1) {
            return 0;
        }
        return (double)Long.bitCount(longs[0]) / (phenotype.getGenotype().geneSize * phenotype.getGenotype().geneCount);
    }
}
