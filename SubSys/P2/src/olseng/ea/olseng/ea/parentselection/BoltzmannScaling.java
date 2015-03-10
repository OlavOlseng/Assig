package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Phenotype;

/**
 * Created by Olav on 08.03.2015.
 */
public class BoltzmannScaling extends GlobalSelector {

    private final double initialT;
    private double currentT = 0;
    private double avgFitnessExp;

    public BoltzmannScaling(double initialT) {
        this.initialT = initialT;
        this.currentT = initialT;

    }

    @Override
    public void additionalPrecalcs() {
        if(currentT > 1) {
            currentT--;
        }
        double T = currentT / initialT;
        avgFitnessExp = 0;

        for (Phenotype p : pool) {
            avgFitnessExp += Math.pow(Math.E,p.getUtilty()/ T);
        }
        avgFitnessExp /= pool.size();
    }

    @Override
    public double scalingFunction(double originalUtility) {
        return Math.pow(Math.E, originalUtility/ (currentT/ initialT)) / avgFitnessExp;
    }
}
