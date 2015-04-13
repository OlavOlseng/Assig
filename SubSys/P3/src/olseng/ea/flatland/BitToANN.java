package olseng.ea.flatland;

import olseng.ea.DevelopmentalMethod;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;

import java.util.ArrayList;

/**
 * Created by Olav on 07.04.2015.
 */
public class BitToANN implements DevelopmentalMethod<BinaryGenome, ANN> {

    private int[] layerSizes;
    private double weightSpan = 3.0;

    public BitToANN(int[] layers) {
        this.layerSizes = layers;
    }

    @Override
    public ANN getPhenotype(BinaryGenome g) {
        ArrayList<Double> list = new ArrayList<Double>();
        double threshold = 0;
        for (int i = 0; i < g.geneCount; i++) {
            int val = 0;
            int maxVal = (int)Math.pow(2, g.geneSize);
            long[] gene = g.getGene(i).toLongArray();
            if(gene.length > 0) {
                val += (int)gene[0];
            }
            if (maxVal != 0) {
                val %= maxVal;
            }
            if (i == 1) {
                threshold = (double)val / maxVal;
            }
            list.add((double)(val - maxVal/2) / maxVal * weightSpan);
        }
        ANN n =  new ANN(g);
        n.outputThreshold = threshold;
        n.initializeLayers(layerSizes);
        int counter = 0;
        for(int i = 1; i < n.nodes.size(); i++) {
            double[][] layer  = new double[n.nodes.get(i).length][n.nodes.get(i - 1).length];

            for (int j = 0; j < n.nodes.get(i).length; j++) {
                for (int k = 0; k < n.nodes.get(i - 1).length; k++) {
                    layer[j][k] = list.get(counter);
                    counter++;
                }
            }
            n.weights.add(layer);
        }
        return n;
    }
}
