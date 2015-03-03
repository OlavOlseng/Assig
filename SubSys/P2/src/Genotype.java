import java.util.BitSet;
import java.util.function.IntConsumer;

/**
 * Only supports fixed size genes atm.
 * Created by Olav on 03.03.2015.
 */
public class Genotype {
    BitSet genotype;
    private int geneCount;
    private int geneSize;

    public Genotype(int geneCount, int geneSize) {
        this.geneCount = geneCount;
        this.geneSize = geneSize;
        this.genotype = new BitSet();
        genotype.set(0, geneCount * geneSize);
    }

    public BitSet getGene(int gene) {
        int max =  gene * geneSize + geneSize;
        if (max > genotype.length()) {
            throw new IndexOutOfBoundsException();
        }
        return genotype.get(gene * geneSize, max);
    }



    public static void main(String[] args) {
        Genotype g = new Genotype(1, 20);
        BitSet set = g.getGene(0);
        System.out.println(set.toLongArray());
        set.stream().limit(20).forEach(System.out::println);
    }
}
