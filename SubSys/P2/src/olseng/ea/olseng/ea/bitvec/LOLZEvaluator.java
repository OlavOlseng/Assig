package olseng.ea.olseng.ea.bitvec;

import olseng.ea.FitnessEvaluator;

/**
 * Created by Olav on 10.03.2015.
 */
public class LOLZEvaluator implements FitnessEvaluator<IntVec> {

    private int threshold;

    public LOLZEvaluator(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public double evaluate(IntVec phenotype) {


        int initialVal = phenotype.data.get(0);
        int checkTo = phenotype.data.size();
        if(initialVal == 0) {
            checkTo = threshold;
        }

        double hits = 0;

        for (int i = 1; i < checkTo; i++) {
            if(phenotype.data.get(i) == initialVal){
                hits++;
            }
            else {
                break;
            }
        }
        return hits /(double)phenotype.data.size();
    }
}
