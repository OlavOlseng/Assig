package olseng.ea;

/**
 * Created by Olav on 05.03.2015.
 */
public abstract class Genotype<T> {

    public final int geneSize;
    public final int geneCount;

    public Genotype(int geneCount, int geneSize) {
        this.geneCount = geneCount;
        this.geneSize = geneSize;
    }

    /**
     * Should return a deep copy of the genotype.
     * @return A deep copy of the genotype.
     */
    public abstract Genotype<T> dupe();
    public abstract T getGene(int index);
    public abstract void setGene(T gene, int index);
    public abstract void randomize();


}
