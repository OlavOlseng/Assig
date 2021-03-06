package olseng.ea.flatland;

import olseng.ea.FitnessEvaluator;

import java.util.ArrayList;

/**
 * Created by Olav on 07.04.2015.
 */
public class FlatlandEvaluator implements FitnessEvaluator<ANN>{

    private static final int MAX_MOVES = 60;

    private final double poisonChance;
    private final double foodChance;

    public int levelCount;
    private double foodWorth;
    private double poisonWorth;

    public ArrayList<Level> levels;

    public FlatlandEvaluator(int levels, double foodChance, double poisonChance, double foodWorth, double poisonWorth) {
        this.levelCount = levels;
        this.foodChance = foodChance;
        this.poisonChance = poisonChance;
        this.foodWorth = foodWorth;
        this.poisonWorth = poisonWorth;
        generateLevels();
    }

    public void generateLevels() {
        this.levels = new ArrayList<Level>(levelCount);
        for (int i = 0; i < levelCount; i++ ) {
            Level l = new Level(10,10);
            l.initialize(foodChance, poisonChance);
            levels.add(l);
        }
    }

    @Override
    public double evaluate(ANN phenotype) {
        double utilSum = 0;
        for (int l = 0; l < levelCount; l++) {
            Level level = levels.get(l).copy();
            for (int i = 0; i < MAX_MOVES; i++) {
                int move = getMove(level, phenotype);
                level.movePlayer(move);
            }
            double levelUtility = level.consumedFood * foodWorth + (level.poisonCount - level.consumedPoison) * poisonWorth;
            levelUtility /= level.foodCount * foodWorth + level.poisonCount * poisonWorth;
            utilSum += levelUtility;
        }
        return utilSum / (double)levelCount;
    }

    public static int getMove(Level level, ANN phenotype) {
        phenotype.setInputs(level.getSensorData());
        phenotype.propagate();
        double[] output = phenotype.getOutput();
        int move = -1;
        double max = 0;
        for (int i = 0; i < output.length; i++) {
            if (output[i] > phenotype.outputThreshold && output[i] > max) {
                move = i;
                max = output[i];
            }
        }

        return move;
    }
}
