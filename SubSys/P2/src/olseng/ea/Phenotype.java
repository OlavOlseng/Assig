package olseng.ea;

import sun.plugin.dom.exception.InvalidStateException;

/**
 * Created by Olav on 03/03/2015.
 */
public abstract class Phenotype<G extends Genotype> implements Comparable<Phenotype>{

    public final G genotype;
    private double utility;
    private boolean utilSet = false;

    public Phenotype(G genotype) {
        this.genotype = genotype;
    }

    public G getGenotype() {
        return this.genotype;
    }

    public void setUtility(double util) {
        this.utility = util;
        this.utilSet = true;
    }

    /**
     * setUtility() MUST be called before calling this function
     * @return - utility value of the phenotype.
     * @throws - un.plugin.dom.exception.InvalidStateException
     */
    public double getUtilty() {
        return this.utility;
    }

    @Override
    public int compareTo(Phenotype other) {
        if (this.utility > other.utility) {
            return -1;
        }
        else if(this.utility < other.utility) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Util: " + utility + ", Of: " + genotype.toString();
    }

}
