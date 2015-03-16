package olseng.ea.olseng.ea.bitvec;

import olseng.ea.FitnessEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Olav on 10.03.2015.
 */
public class LSSEvaluator implements FitnessEvaluator<IntVec> {

    @Override
    public double evaluate(IntVec phenotype) {
        List<Integer> data = phenotype.data;

        double util = 0;
        double maxUtil = (data.size() - 1) * (data.size() - 2) / 2;

        for (int i = 0; i < data.size() - 2; i++) {
            int currentNum = data.get(i);
                for (int j = i + 1; j < data.size() - 1; j++) {
                    if(data.get(j) == currentNum) {
                        if (data.get(i + 1) == data.get(j + 1)) {
                            continue;
                        }
                    }
                    util++;
                }
            }
        return util / maxUtil;
    }

    public static void main(String[] args) {
        List<Integer> data = new ArrayList<Integer>(Arrays.asList(0,1,1,0,0));
        double util = 0;

        for (int i = 0; i < data.size() - 2; i++) {
            int currentNum = data.get(i);
            for (int j = i + 1; j < data.size() - 1; j++) {
                if(data.get(j) == currentNum) {
                    if (data.get(i + 1) == data.get(j + 1)) {
                        continue;
                    }
                }
                util++;
            }
        }
        System.out.println(util);
    }
}
