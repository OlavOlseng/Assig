package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Genotype;
import olseng.ea.Phenotype;

import java.util.List;

/**
 * Created by Olav on 08.03.2015.
 */
public interface ParentSelector {

    /**
     *Returns a list from the pool the module was initialized with.
     * @param sexual If true, returns two individuals. Else it returns one.
     * @return The parent(s) chosen to mate/mutate.
     * @throws sun.plugin.dom.exception.InvalidStateException
     */
    public List<Genotype> getParents(boolean sexual);

    /**
     *Sets up the selector to take calls from the getParents() function.
     * @param adultPool
     */
    public void initialize(List<Phenotype> adultPool);

    public double getAvgUtility();
}
