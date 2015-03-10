package olseng.ea.olseng.ea.adultselectors;

import olseng.ea.Phenotype;

import java.util.List;

/**
 * Created by Olav on 07.03.2015.
 */
public class FullReplacement extends AdultSelector {

    public FullReplacement(int populationSize) {
        super(populationSize, populationSize);
    }

    @Override
    public List<Phenotype> getSelection(List<Phenotype> adults, List<Phenotype> youngAdults) {
        return youngAdults;
    }
}
