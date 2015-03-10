package olseng.ea.olseng.ea.adultselectors;

import olseng.ea.Phenotype;

import java.util.Collections;
import java.util.List;

/**
 * Created by Olav on 08.03.2015.
 */
public class OverProduction extends AdultSelector {

    /**
     * @param childrenToCreate Decides how many children will be added to the next generation.
     */
    public OverProduction(int populationSize, int childrenToCreate) {
        super(populationSize, childrenToCreate);
    }

    @Override
    public List<Phenotype> getSelection(List<Phenotype> adults, List<Phenotype> youngAdults) {
        Collections.sort(youngAdults);
        return youngAdults.subList(0,populationSize);
    }
}
