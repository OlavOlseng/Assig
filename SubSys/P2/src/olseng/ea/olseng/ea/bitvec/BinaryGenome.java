package olseng.ea.olseng.ea.bitvec;

import olseng.ea.Genotype;

import java.util.BitSet;

/**
 * Only supports fixed size genes atm.
 * Created by Olav on 03.03.2015.
 */
public class BinaryGenome extends Genotype<BitSet> {

    BitSet genotype;

    public BinaryGenome(int geneCount, int geneSize) {
        this(geneCount, geneSize, new BitSet());
    }

    public BinaryGenome(int geneCount, int geneSize, BitSet bitset) {
        super(geneCount, geneSize);
        this.genotype = bitset;
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

    /**
     * Randomizes the entire genotype
     */
    public void randomize() {
        for (int i = 0; i < geneCount * geneSize; i++) {
            genotype.set(i, Math.random() < 0.5);
        }
    }

    /**
     * @return The amount of bits in the genome.
     */
    public int getSize() {
        return geneSize * geneCount;
    }

    @Override
    public Genotype<BitSet> dupe() {
        return new BinaryGenome(geneCount, geneSize, (BitSet)genotype.clone());
    }

    //============================TEST=================================
    public static void main(String[] args) {
        BinaryGenome g = new BinaryGenome(2, 2);
        BitSet set = g.getGene(0);
        System.out.println(set.length());
        for (int i = 0; i < g.geneCount * g.geneSize; i++) {
            System.out.println(g.genotype.get(i));
        }
        System.out.println("LOL\n");
        BinaryGenome bsg = new BinaryGenome(1, 2);
        bsg.randomize();
        g.setGene(bsg.getGene(0), 0);
        for (int i = 0; i<g.geneCount * g.geneSize; i++) {
            System.out.println(g.genotype.get(i));
        }
    }
}
