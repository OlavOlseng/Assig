package olseng.ea.ctrnn;

import olseng.ea.Phenotype;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class CTRNN extends Phenotype<BinaryGenome> {

    public final int[] layerSizes;
    public ArrayList<double[][]> feedForwardWeights;
    public ArrayList<double[][]> internalLayerWeights;
    public double[][] nodes;
    public double[][] outputs;
    public double[][] gains;
    public double[][] biasWeights;
    public double[][] timeConstants;

    private int[] weightCounts;

    public CTRNN(int[] layerSizes, BinaryGenome genotype) {
        super(genotype);
        this.layerSizes = layerSizes;
        this.feedForwardWeights = new ArrayList<double[][]>();
        this.internalLayerWeights = new ArrayList<double[][]>();
        flush();
    }

    /**
     * @return An array with the same topology as the specified layers. Layer -> Node.
     */
    public double[][] buildTopology() {
        double[][] nodes = new double[layerSizes.length][];
        for (int i = 0; i < layerSizes.length; i++) {
            nodes[i] = new double[layerSizes[i]];
        }
        return nodes;
    }

    public double[][] buildInternalTopology() {
        double[][] temp = new double[layerSizes.length - 1][];
        for (int i = 1; i < layerSizes.length; i++) {
            temp[i - 1] = new double[layerSizes[i]];
        }
        return temp;
    }

    public int[] getWeightCounts() {
        if (this.weightCounts == null) {
            weightCounts = new int[3];
            for (int i = 1; i < layerSizes.length; i++) {
                weightCounts[0] += layerSizes[i] * layerSizes[i - 1]; //Feed forward weights
                weightCounts[1] += layerSizes[i] * layerSizes[i]; //Layer internal weights
                weightCounts[2] += layerSizes[i] * 3; //Bias gains and time const weights
            }
        }
        return this.weightCounts;
    }

    private double sigmoid(double input, double gain) {
        return 1 / (1 + Math.exp(-gain * input));
    }

    public void propagate() {
        for(int i = 1; i < nodes.length; i++) {

            for(int node = 0; node < nodes[i].length; node++) {
                double sum = 0;
                double[][] prevLayer = feedForwardWeights.get(i - 1);
                double[][] selfLayer = internalLayerWeights.get(i - 1);

                for (int j = 0; j < nodes[i - 1].length; j++) {
                    sum += prevLayer[j][node] * outputs[i - 1][j]; //Node j in prev layer to current node.
                }

                for (int j = 0; j < nodes[i].length; j++) {
                    sum += selfLayer[j][node] * outputs[i][j]; //Node j in same layer, to current node.
                }
                nodes[i][node] += 1 / this.timeConstants[i - 1][node] * (-nodes[i][node] + sum + biasWeights[i - 1][node]);
                outputs[i][node] = sigmoid(nodes[i][node], this.gains[i - 1][node]);
            }
        }
    }

    public void flush() {
        this.nodes = buildTopology();
        this.outputs = buildTopology();
    }

    public void setInputs(double[] inputs) {
        if (inputs.length != nodes[0].length) {
            throw new InvalidStateException("Failed to match input to input nodes");
        }
        nodes[0] = inputs;
        outputs[0] = inputs;
    }

    public double[] getOutput() {
        return outputs[nodes.length - 1];
    }


//==============================TESTS===============================
    public static void main(String[] args) {
        CTRNN n = new CTRNN(new int[]{5, 2, 2}, new BinaryGenome(6,6));
        n.setInputs(new double[]{1, 1, 0, 0, 0});

        n.gains = n.buildInternalTopology();
        n.biasWeights = n.buildInternalTopology();
        n.timeConstants = n.buildInternalTopology();

        for(int i = 1; i < n.nodes.length; i++) {
            double[][] layer  = new double[n.nodes[i - 1].length][n.nodes[i].length];
            double[][] intLayer = new double[n.layerSizes[i]][n.layerSizes[i]];

            for (int j = 0; j < n.nodes[i - 1].length; j++) {

                for (int k = 0; k < n.nodes[i].length; k++) {
                    layer[j][k] = Math.random();
                }
            }

            for(int j = 0; j < n.nodes[i].length; j++) {
                for (int k = 0; k < n.nodes[i].length; k++) {
                    intLayer[j][k] = Math.random();
                }
                n.gains[i - 1][j] = Math.random() * 5;
                n.biasWeights[i - 1][j] = Math.random();
            }

            n.feedForwardWeights.add(layer);
            n.internalLayerWeights.add(intLayer);
        }
        n.propagate();
        double[] out  = n.getOutput();
        System.out.println(Arrays.toString(out));
        System.out.println(Arrays.toString(n.getWeightCounts()));
    }
}
