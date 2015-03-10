package olseng.ea;

/**
 * Created by Olav on 03/03/2015.
 */
public interface FitnessEvaluator<P extends Phenotype> {
    /**
     * This function should evaluate a phenotype and return a clamped double.
     * @param phenotype
     * @return Double in range [0,1]
     */
    public double evaluate(P phenotype);
}
