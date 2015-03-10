package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Phenotype;

/**
 * Created by Olav on 08.03.2015.
 */
public class SigmaScaling extends GlobalSelector {

    private double variance = 0;
    private double sd;

    @Override
    public void additionalPrecalcs() {
        for (Phenotype p : pool) {
            variance += Math.pow(p.getUtilty() - avgUtility, 2);
        }
        variance /= pool.size();
        sd = Math.sqrt(variance);
    }

    @Override
    public double scalingFunction(double originalUtility) {
        if(sd == 0) {
            return 1.0;
        }
        return 1 + ((originalUtility - avgUtility) / ( 2 * sd));
    }

}
