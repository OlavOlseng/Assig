package olseng.ea.flatland;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class ANN {

    public ArrayList<double[][]> weights;
    public ArrayList<double[]> nodes;
    public double beta = 1.0;

    public ANN() {
        this.weights = new ArrayList<double[][]>();
        this.nodes = new ArrayList<double[]>();
    }

    /**
     * initilises the layers and weights.
     */
    private void initializeLayers() {
        nodes.add(new double[6]);
        nodes.add(new double[4]);
        nodes.add(new double[3]);


        //weights are randomised here, needs to be moved into phenotype development and made not random.
        for(int i = 1; i < nodes.size(); i++) {
            double[][] layer  = new double[nodes.get(i).length][nodes.get(i - 1).length];

            for (int j = 0; j < nodes.get(i).length; j++) {
                for (int k = 0; k < nodes.get(i - 1).length; k++) {
                    layer[j][k] = Math.random() * 2 - 1;
                }
            }
            weights.add(layer);
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

    public static void main(String[] args) {
        ANN b = new ANN();
        b.initializeLayers();
        b.setInputs(new double[]{1,0,0,1,0,0});
        b.propagate();
        double[] out  = b.getOutput();
        System.out.println(Arrays.toString(out));
    }

}
