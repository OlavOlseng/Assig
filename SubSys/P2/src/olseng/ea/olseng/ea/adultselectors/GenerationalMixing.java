package olseng.ea.olseng.ea.adultselectors;

import olseng.ea.Phenotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Employs Elitism to retain the best adults in the former generation.
 * Created by Olav on 08.03.2015.
 */
public class GenerationalMixing extends AdultSelector {

    int adultsToRetain;

    public GenerationalMixing(int populationSize, int childrenToCreate, int adultsToRetain) {
        super(populationSize, childrenToCreate);
        this.adultsToRetain = adultsToRetain;
    }

    @Override
    public List<Phenotype> getSelection(List<Phenotype> adults, List<Phenotype> youngAdults) {
        Collections.sort(youngAdults);
        ArrayList<Phenotype> newPool = new ArrayList<Phenotype>(populationSize);
        newPool.addAll(adults.subList(0,Math.min(adultsToRetain, adults.size())));
        newPool.addAll(youngAdults.subList(0,populationSize - newPool.size()));
        return newPool;
    }
}
