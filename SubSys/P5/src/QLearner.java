
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Olav on 12.05.2015.
 */
public class QLearner {

    private int[][] policy;

    private int width, height, foodCount, actionCount;
    private Level map;

    public double learningRate = 0.1;
    public double discountRate = 0.9;

    public double coolingRate = 0.001;
    private double initialTemperature = 0.99;

    HashMap<String, double[][][]> stateMap;

    ArrayList<int[]> positionQueue;
    ArrayList<int[]> eatenQueue;
    ArrayList<Integer> actionQueue;
    ArrayList<Double> rewardQueue;

    public static double SCORE_FOOD = 100;
    public static double SCORE_POISON = -100;
    public static double SCORE_NOTHING = -0.01;

    public QLearner(int actionCount, Level map) {
        this.positionQueue = new ArrayList<int[]>();
        this.eatenQueue = new ArrayList<int[]>();
        this.rewardQueue = new ArrayList<Double>();
        this.actionQueue = new ArrayList<Integer>();

        this.width = map.width;
        this.height = map.height;
        this.actionCount = actionCount;
        this.foodCount = map.foodCount;
        setMap(map);
    }

    private void setMap(Level map) {
        this.map = map;
        initialize();
    }

    public void initialize() {
        stateMap = new HashMap<String, double[][][]>();
        policy = new int[height][width];
    }

    public void train(int iterations) {
        int K = 0;
        double temperature = initialTemperature;

        while(K < iterations) {
            Level map = this.map.copy();
            int[] eaten = new int[foodCount];
            int[] newPos = new int[2];
            newPos[0] = map.getPlayerX();
            newPos[1] = map.getPlayerY();

            while(!map.gameOver()) {
                int[] oldPos = new int[2];
                oldPos[0] = newPos[0];
                oldPos[1] = newPos[1];

                int action = selectAction(oldPos, eaten, 0.9);
                int result = map.movePlayer(action);

                newPos[0] = map.getPlayerX();
                newPos[1] = map.getPlayerY();

                updateStateActionMap(newPos, oldPos, eaten, action, result);

                if (result > 0) {
                    eaten[result - 1] = 1;
                }
            }
            temperature = initialTemperature * Math.pow(1 - coolingRate, K);
            clearBacktrace();
            K++;
        }
    }

    private void addBacktrace(int[] oldPos, int[] eaten, int action, double reward) {
        this.positionQueue.add(oldPos);
        this.eatenQueue.add(eaten);
        this.rewardQueue.add(reward);
        this.actionQueue.add(action);
    }

    private void clearBacktrace() {
        this.positionQueue.clear();
        this.eatenQueue.clear();
        this.rewardQueue.clear();
        this.actionQueue.clear();
    }



    private int selectAction(int[] pos, int[] eaten, double randomChance) {
        double rand = Math.random();
        if (rand < randomChance) {
            return (int)(rand * (actionCount));
        }
        return getBestAction(pos, eaten);
    }

    public double[] getState(int[] position, int[] eaten) {
        String eat = Arrays.toString(eaten);

        if (!stateMap.containsKey(eat)) {
            stateMap.put(eat, new double[height][width][actionCount]);
        }
        return stateMap.get(eat)[position[1]][position[0]];
    }

    private void runBacktrace(int[] newPos, int[] newEaten) {
        for (int i = 0; i < actionQueue.size(); i++) {
            double resultScore = rewardQueue.get(rewardQueue.size() - 1 - i);
            int[] oldPos = positionQueue.get(positionQueue.size() - 1 - i);
            int[] eaten = eatenQueue.get(eatenQueue.size() - 1 - i);
            int action = actionQueue.get(actionQueue.size() - 1 - i);

            double scoreDiff = learningRate * (resultScore + discountRate * (getMaxValue(newPos, newEaten)) - getState(oldPos, eaten)[action]);
            getState(oldPos, eaten)[action] += scoreDiff;

            newPos = oldPos;
            newEaten = eaten;
        }
    }

    private void updateStateActionMap(int[] newPos, int[] oldPos, int[] eaten, int action, int result) {
        int[] newEaten = eaten;
        boolean clear = false;
        if (result > 0) {
            newEaten = Arrays.copyOf(eaten, eaten.length);
            newEaten[result - 1] = 1;
        }
        double resultScore = getResultScore(result);

        addBacktrace(oldPos, Arrays.copyOf(eaten, eaten.length), action, resultScore);
        runBacktrace(newPos, newEaten);
        if (clear) {
            clearBacktrace();
        }
    }



    private double getResultScore(int result) {
        switch (result) {
            case Level.TILE_EMPTY:
                return SCORE_NOTHING;

            case Level.TILE_POISON:
                return SCORE_POISON;

            case Level.TILE_FINISHED:
                return SCORE_FOOD;
            default:
                return SCORE_FOOD;
        }
    }

    private int getBestAction(int[] pos, int[] eaten) {
        int action = 0;
        double maxValue = -Double.MAX_VALUE;
        double[] stateActionMap = getState(pos, eaten);
        for (int a = 0; a < stateActionMap.length; a++) {
            double actionScore = stateActionMap[a];
            if (actionScore > maxValue) {
                action = a;
                maxValue = actionScore;
            }
        }
        return action;
    }

    private double getMaxValue(int[] pos, int[] eaten) {
        double maxVal = -Double.MAX_VALUE;
        double[] stateActionMap = getState(pos, eaten);

        for (int a = 0; a < stateActionMap.length; a++) {

            double actionScore = stateActionMap[a];
            if (actionScore > maxVal) {
                maxVal = actionScore;
            }
        }
        return maxVal;
    }

    public int[][] getPolicy(int[] eaten) {
        int[][] policy = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                policy[y][x] = getBestAction(new int[]{x, y}, eaten);
            }
        }
        return policy;
    }

    public static void main(String[] args) {
        Level map = null;
        try {
            map = Level.fromFile("Levels/1-simple.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        QLearner l =  new QLearner(4, map);
        l.train(10);
        int[][] policy = l.getPolicy(new int[]{0});
        System.out.println();
        for (int[] row : policy) {
            System.out.println(Arrays.toString(row));
        }

        policy = l.getPolicy(new int[]{1});
        System.out.println();
        for (int[] row : policy) {
            System.out.println(Arrays.toString(row));
        }
        l.map.print();
    }
}

