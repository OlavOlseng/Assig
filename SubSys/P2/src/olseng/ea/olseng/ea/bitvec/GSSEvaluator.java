package olseng.ea.olseng.ea.bitvec;

import olseng.ea.FitnessEvaluator;
import olseng.ea.olseng.ea.parentselection.FitnessProportionate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Olav on 11.03.2015.
 */
public class GSSEvaluator implements FitnessEvaluator<IntVec> {


    @Override
    public double evaluate(IntVec phenotype) {
        List<Integer> data = phenotype.data;

        double util = 0;
        double maxUtil = 0;

        for (int d = 1; d < data.size() - 1; d++) {
            for (int i = 0; i + d < data.size() - 2; i++) {
                int currentI = data.get(i);
                for (int j = i + d; j < data.size() - d; j++) {
                    if(data.get(j) == currentI) {
                        if(data.get(i + d) == data.get(j + d)) {
                            maxUtil++;
                            continue;
                        }
                    }
                    util++;
                    maxUtil++;
                }
            }
        }
        return util / maxUtil;
    }

    public static void main(String[] args) {
        List<Integer> data = new ArrayList<Integer>(Arrays.asList(0, 1, 1, 0, 0));

        double util = 0;
        double maxUtil = 0;

        for (int d = 1; d < data.size(); d++) {
            for (int i = 0; i + d < data.size() - 1; i++) {
                int currentI = data.get(i);
                for (int j = i + 1; j + d < data.size(); j++) {
                    if(data.get(j) == currentI) {
                        if(data.get(i + d) == data.get(j + d)) {
                            maxUtil++;
                            continue;
                        }
                    }
                    util++;
                    maxUtil++;
                }
            }
        }
        System.out.println(util/maxUtil);
        System.out.println(32 - Integer.numberOfLeadingZeros(8));

    }
}
