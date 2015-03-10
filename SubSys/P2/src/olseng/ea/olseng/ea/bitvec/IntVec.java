package olseng.ea.olseng.ea.bitvec;

import olseng.ea.Phenotype;

import java.util.List;

/**
 * Created by Olav on 05.03.2015.
 */
public class IntVec extends Phenotype<BinaryGenome> {

    List<Integer> data;

    public IntVec(BinaryGenome genotype, List<Integer> data) {
        super(genotype);
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString() + " -> " + this.getUtilty();
    }
}
