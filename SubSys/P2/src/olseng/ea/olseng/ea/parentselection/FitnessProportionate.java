package olseng.ea.olseng.ea.parentselection;

/**
 * Created by Olav on 08.03.2015.
 */
public class FitnessProportionate extends GlobalSelector {

    @Override
    public double scalingFunction(double originalUtility) {
        return originalUtility;
    }
}
