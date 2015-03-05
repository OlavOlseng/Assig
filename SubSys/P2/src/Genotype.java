import java.util.BitSet;

/**
 * Only supports fixed size genes atm.
 * Created by Olav on 03.03.2015.
 */
public class Genotype {
    BitSet genotype;
    public final int geneCount;
    public final int geneSize;

    /**
     * @param geneCount
     * @param geneSize
     * @param bitSet
     */
    public Genotype(int geneCount, int geneSize, BitSet bitSet) {
        this.geneCount = geneCount;
        this.geneSize = geneSize;
        this.genotype = bitSet;
    }

    public Genotype(int geneCount, int geneSize) {
        this(geneCount, geneSize, new BitSet());
    }

    public BitSet getGene(int geneIndex) {
        if (geneIndex >= geneCount) {
            throw new IndexOutOfBoundsException();
        }
        int max =  geneIndex * geneSize + geneSize;
        return genotype.get(geneIndex * geneSize, max);
    }

    public void setGene(BitSet set, int geneIndex) {
        if (geneIndex >= geneCount) {
            throw new IndexOutOfBoundsException();
        }
        int index = geneIndex * geneSize;
        for (int i = 0; i < geneSize; i++) {
            genotype.set(index + i, set.get(i));
        }
    }

    public Genotype randomize() {
        for (int i = 0; i < geneCount * geneSize; i++) {
            genotype.set(i, Math.random() < 0.5);
        }
        return this;
    }

    //=========================================================
    public static void main(String[] args) {
        Genotype g = new Genotype(2, 2);
        BitSet set = g.getGene(0);
        System.out.println(set.length());
        for (int i = 0; i<g.geneCount * g.geneSize; i++) {
            System.out.println(g.genotype.get(i));
        }
        System.out.println("LOL\n");
        g.setGene((new Genotype(1, 2)).randomize().getGene(0), 0);
        for (int i = 0; i<g.geneCount * g.geneSize; i++) {
            System.out.println(g.genotype.get(i));
        }
    }
}
