package olseng.ea.flatland;

import olseng.ea.Phenotype;
import olseng.ea.olseng.ea.bitvec.BinaryGenome;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class ANN extends Phenotype<BinaryGenome> {

    public ArrayList<double[][]> weights;
    public ArrayList<double[]> nodes;
    public double beta = 3.0;
    private int weightCount = 0;
    public double outputThreshold = 0;

    public ANN(BinaryGenome genotype) {
        super(genotype);
        this.weights = new ArrayList<double[][]>();
        this.nodes = new ArrayList<double[]>();
    }

    /**
     * initilises the layers. Should only be called once.
     */
    public void initializeLayers(int[] layerSizes) {
        for (int i = 0; i < layerSizes.length; i++) {
            nodes.add(new double[layerSizes[i]]);
            if (i > 0) {
                weightCount += layerSizes[i] * layerSizes[i - 1];
            }
        }
    }

    private double function(double input) {
        return 1 / (1 + Math.exp(-beta * input));
    }

    public void propagate() {
        for(int i = 1; i < nodes.size(); i++) {
            double[][] layer = weights.get(i - 1);
            for (int j = 0; j < layer.length; j++) {
                double sum = 0;
                for (int k = 0; k < layer[j].length; k++) {
                    double weight = layer[j][k];
                    sum += weight * nodes.get(i - 1)[k];
                }
                nodes.get(i)[j] = function(sum);
            }
        }
    }

    public void setInputs(double[] inputs) {
        if (inputs.length != nodes.get(0).length) {
            throw new InvalidStateException("Failed to match input to input nodes");
        }
        nodes.set(0, inputs);
    }

    public double[] getOutput() {
        return nodes.get(nodes.size() - 1);
    }

    public int getWeightCount() {
        return weightCount;
    }

//==============================TESTS===============================
    public static void main(String[] args) {
        ANN b = new ANN(new BinaryGenome(6,6));
        b.initializeLayers(new int[]{6,4,3});
        b.setInputs(new double[]{1,0,0,1,0,0});
        b.propagate();
        double[] out  = b.getOutput();
        System.out.println(Arrays.toString(out));
    }
}
