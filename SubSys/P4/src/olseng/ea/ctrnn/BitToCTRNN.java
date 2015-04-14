package olseng.ea.ctrnn;

import olseng.ea.DevelopmentalMethod;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class BitToCTRNN implements DevelopmentalMethod<BinaryGenome, CTRNN> {

    private int[] layerSizes;
    private double gainSpan = 4;
    private double gainMax = 5;
    private double weightSpan = 10;
    private double weightMax = 5;
    private double biasSpan = 10;
    private double biasMax = 0;
    private double timeConstSpan = 1;
    private double timeConstMax = 2;

    public static final int MODE_STANDARD = 0;
    public static final int MODE_PULL = 1;
    public static final int MODE_NO_WRAP = 2;

    public int mode = MODE_STANDARD;

    public BitToCTRNN(int[] layers) {
        this.layerSizes = layers;
    }

    @Override
    public CTRNN getPhenotype(BinaryGenome g) {
        ArrayList<Double> list = new ArrayList<Double>();
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
            list.add((double)val / maxVal);
        }
        CTRNN n =  new CTRNN(layerSizes, g);

        int geneCounter = 0;

        n.gains = n.buildInternalTopology();
        n.biasWeights = n.buildInternalTopology();
        n.timeConstants = n.buildInternalTopology();

        for(int i = 1; i < n.nodes.length; i++) {
            double[][] layer  = new double[n.nodes[i - 1].length][n.nodes[i].length];
            double[][] intLayer = new double[n.layerSizes[i]][n.layerSizes[i]];

            for (int j = 0; j < n.nodes[i - 1].length; j++) {

                for (int k = 0; k < n.nodes[i].length; k++) {
                    double val = list.get(geneCounter++);
                    val = weightMax - val * weightSpan;
                    layer[j][k] = val;
                }
            }

            for(int j = 0; j < n.nodes[i].length; j++) {
                for (int k = 0; k < n.nodes[i].length; k++) {
                    double val = list.get(geneCounter++);
                    val = weightMax - val * weightSpan;
                    intLayer[j][k] = val;
                }
                double val = list.get(geneCounter++);
                val = gainMax - val * gainSpan;
                n.gains[i - 1][j] = val++;
                val = list.get(geneCounter++);
                val = biasMax - val * biasSpan;
                n.biasWeights[i - 1][j] = val;
                val = list.get(geneCounter++);
                val = timeConstMax - val * timeConstSpan;
                n.timeConstants[i - 1][j] = val;
            }
            n.feedForwardWeights.add(layer);
            n.internalLayerWeights.add(intLayer);
        }
        if (mode == MODE_PULL) {
            n.pullThreshold = list.get(list.size() - 1) * 5;
        }
        return n;
    }

    //###########################TEST#################################

    public static void main(String[] args) {
        CTRNN n = new CTRNN(new int[]{4,2,2}, null);
        int weights = 0;
        for (int i : n.getWeightCounts()){
            weights += i;
        }
        BinaryGenome g = new BinaryGenome(weights, 8);
        g.randomize();
        CTRNN m = new BitToCTRNN(n.layerSizes).getPhenotype(g);
        System.out.println(m);
        m.propagate();
        m.setInputs(new double[]{1,0,0,1});
        System.out.println(Arrays.toString(m.getOutput()));
    }
}
