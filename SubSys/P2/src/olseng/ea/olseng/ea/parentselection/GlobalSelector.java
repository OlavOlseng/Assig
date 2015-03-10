package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Genotype;
import olseng.ea.Phenotype;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 07.03.2015.
 */
public abstract class GlobalSelector implements ParentSelector{

    private double utilitySum;
    protected double postScaleUtilitySum;
    protected double avgUtility;
    protected List<Phenotype> pool = null;
    private double[] indices;

    @Override
    public void initialize(List<Phenotype> adultPool) {
        utilitySum = 0;
        postScaleUtilitySum = 0;
        avgUtility = 0;
        this.pool = adultPool;
        this.indices = new double[pool.size()];

        //Calc averages
        for (int i = 0; i < pool.size(); i++) {
            Phenotype p = pool.get(i);
            utilitySum += p.getUtilty();
        }
        avgUtility = utilitySum / pool.size();
        //Plugging function for extra math
        additionalPrecalcs();

        for (int i = 0; i < indices.length; i++) {
            Phenotype p = pool.get(i);
            double utility = scalingFunction(p.getUtilty());
            postScaleUtilitySum += utility;
            indices[i] = postScaleUtilitySum;
        }
    }

    @Override
    public List<Genotype> getParents(boolean sexual) {
        if(this.pool == null) {
            throw new InvalidStateException("ParentSelector not properly initialized");
        }

        List<Genotype> parents = new ArrayList<Genotype>();
        int index = selectParent();
        parents.add(pool.get(index).genotype.dupe());

        if (sexual) {
            int index2 = selectParent();
            if (index2 == index) {
                if (index2 < pool.size() - 1) {
                    index2++;
                } else {
                    index2--;
                }
            }
            parents.add(pool.get(index2).genotype.dupe());
        }
        return parents;
    }


    private int getParentFromList(double value) {
        //This can be upgraded using binary search.
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] > value) {
                return i;
            }
        }
        return indices.length - 1;
    }

    private int selectParent() {
        double value = Math.random();
        value *= postScaleUtilitySum;
        return getParentFromList(value);
    }

    public double getAvgUtility() {
        return this.avgUtility;
    }

    /**
     * Function that is latched onto the end of the initialization function.
     * Override to do any additional calculations post initialization, pre selection.
      */
    public void additionalPrecalcs() {
    }

    /**
     * Should scale the utility of a phenotype. Called post additionalPreCalcs.
     * @param originalUtility Clamped double
     * @return [0,1)
     */
    public abstract double scalingFunction(double originalUtility);
}
