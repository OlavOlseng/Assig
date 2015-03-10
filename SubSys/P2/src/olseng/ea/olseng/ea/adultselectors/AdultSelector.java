package olseng.ea.olseng.ea.adultselectors;

import olseng.ea.Phenotype;

import java.util.List;

/**
 * Created by Olav on 03/03/2015.
 */
public abstract class AdultSelector {

    public final int childrenToCreate;
    public final int populationSize;

    /**
     * @param childrenToCreate Decides how many children will be added to the next generation.
     */
    public AdultSelector(int populationSize, int childrenToCreate) {
        this.populationSize = populationSize;
        this.childrenToCreate = childrenToCreate;
    }

    public abstract List<Phenotype> getSelection(List<Phenotype> adults, List<Phenotype> youngAdults);
}
 