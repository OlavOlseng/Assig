package olseng.ea.olseng.ea.bitvec;

import olseng.ea.DevelopmentalMethod;

import java.util.ArrayList;

/**
 * Created by Olav on 03/03/2015.
 */
public class BitToIntVec implements DevelopmentalMethod<BinaryGenome,IntVec> {



    public static void main(String[] args) {
        BinaryGenome g = new BinaryGenome(1,8);
        g.randomize();
        DevelopmentalMethod dm = new BitToIntVec();
        System.out.println(dm.getPhenotype(g));
    }

    @Override
    public IntVec getPhenotype(BinaryGenome g) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < g.geneCount; i++) {
            int val = 0;
            long[] gene = g.getGene(i).toLongArray();
            if(gene.length > 0) {
                val += (int)gene[0];
            }
            list.add(val);
        }
        return new IntVec(g, list);
    }
}


