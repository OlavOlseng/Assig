
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Olav on 12.05.2015.
 */
public class QLearner {

    public static double SCORE_FOOD = 1;
    public static double SCORE_POISON = -2;
    public static double SCORE_NOTHING = -0.5;

    private int width, height, actionCount;

    private Level map;
    public double learningRate = 0.5;
    public double discountRate = 0.99;

    public double wobble = 0.4;

    public int maxBackstack = 1;

    Random random = new Random(System.currentTimeMillis());

    HashMap<Long, HashMap<Integer, double[]>> stateMap;
    ArrayList<int[]> positionQueue;
    ArrayList<Long> eatenQueue;
    ArrayList<Integer> actionQueue;

    ArrayList<Double> rewardQueue;
    public boolean escape = false;

    public QLearner(int actionCount) {
        this.positionQueue = new ArrayList<int[]>();
        this.eatenQueue = new ArrayList<Long>();
        this.rewardQueue = new ArrayList<Double>();
        this.actionQueue = new ArrayList<Integer>();
        this.actionCount = actionCount;
    }

    public void setMap(Level map) {
        this.map = map;
        this.width = map.width;
        this.height = map.height;
        initialize();
    }

    public void initialize() {
        stateMap = new HashMap<Long,HashMap<Integer, double[]>>();
        clearBacktrace();
    }


    @Deprecated
    public void train(int iterations) {
        int K = 0;
        double temperature = wobble;

        while(K < iterations) {
            Level map = this.map.copy();
            long eaten = 0;
            int[] newPos = new int[2];
            newPos[0] = map.getPlayerX();
            newPos[1] = map.getPlayerY();

            while(!map.gameOver()) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int[] oldPos = new int[2];
                oldPos[0] = newPos[0];
                oldPos[1] = newPos[1];

                int action = selectAction(oldPos, eaten, temperature);
                int result = map.movePlayer(action);

                newPos[0] = map.getPlayerX();
                newPos[1] = map.getPlayerY();

                updateStateActionMap(newPos, oldPos, eaten, action, result);

                if (result > 0) {
                    eaten |= 1L << (result - 1);
                }
            }
            temperature = 0.05 + wobble * (double)((iterations - K) / iterations);
            clearBacktrace();
            K++;
        }
    }

    public void train(double progress) {
        escape = false;
        Level map = this.map.copy();
        long eaten = 0;
        int[] newPos = new int[2];
        newPos[0] = map.getPlayerX();
        newPos[1] = map.getPlayerY();
        double temperature = 0.05 + wobble * (1 - progress);

        while(!map.gameOver() && !escape) {
            int[] oldPos = new int[2];
            oldPos[0] = newPos[0];
            oldPos[1] = newPos[1];

            int action = selectAction(oldPos, eaten, temperature);
            int result = map.movePlayer(action);

            newPos[0] = map.getPlayerX();
            newPos[1] = map.getPlayerY();

            updateStateActionMap(newPos, oldPos, eaten, action, result);

            if (result > 0) {
                eaten |= 1L << (result - 1);
            }
        }
        escape = false;
        clearBacktrace();
    }

    private void addBacktrace(int[] oldPos, long eaten, int action, double reward) {
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

    private int selectAction(int[] pos, long eaten, double randomChance) {
        double rand = random.nextDouble();
        if (rand < randomChance) {
            return (int)(rand * (actionCount));
        }
        return getBestAction(pos, eaten, false);
    }

    public double[] getState(int[] position, long eaten) {

        if (!stateMap.containsKey(eaten)) {
            stateMap.put(eaten,new HashMap<Integer, double[]>());
        }
        HashMap<Integer, double[]> foodState = stateMap.get(eaten);
        int pos = (position[0] << 16);
        pos |= position[1];

        if (!foodState.containsKey(pos)) {
            double[] vals = new double[actionCount];
            foodState.put(pos, vals);
        }
        return foodState.get(pos);
    }

    public void popBacktrace() {
        rewardQueue.remove(0);
        positionQueue.remove(0);
        eatenQueue.remove(0);
        actionQueue.remove(0);
    }

    private void runBacktrace(int[] newPos, long newEaten) {
        for (int i = 0; i < actionQueue.size(); i++) {
            double resultScore = rewardQueue.get(rewardQueue.size() - 1 - i);
            int[] oldPos = positionQueue.get(positionQueue.size() - 1 - i);
            long eaten = eatenQueue.get(eatenQueue.size() - 1 - i);
            int action = actionQueue.get(actionQueue.size() - 1 - i);

            double scoreDiff = learningRate * (resultScore + discountRate * (getMaxValue(newPos, newEaten)) - getState(oldPos, eaten)[action]);
            getState(oldPos, eaten)[action] += scoreDiff;

            newPos = oldPos;
            newEaten = eaten;
        }
    }

    private void updateStateActionMap(int[] newPos, int[] oldPos, long eaten, int action, int result) {
        long newEaten = eaten;
        boolean clear = false;
        if (result > 0) {
            newEaten = map.foodEaten;
            clear = true;
        }
        double resultScore = getResultScore(result);

        addBacktrace(oldPos, eaten, action, resultScore);

        while (actionQueue.size() > maxBackstack && maxBackstack > 0) {
            popBacktrace();
        }

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

    public boolean isStateVisited(int[] position, long eaten) {
        if (!stateMap.containsKey(eaten)) {
            stateMap.put(eaten,new HashMap<Integer, double[]>());
        }
        HashMap<Integer, double[]> foodState = stateMap.get(eaten);
        int pos = (position[0] << 16);
        pos |= position[1];

        return foodState.containsKey(pos);
    }

    public int getBestAction(int[] pos, long eaten, boolean unique) {
        int action = 0;
        double maxValue = -Double.MAX_VALUE;


        double[] stateActionMap = getState(pos, eaten);
        boolean isUnique = true;
        for (int a = 0; a < stateActionMap.length; a++) {
            double actionScore = stateActionMap[a];
            if (actionScore > maxValue) {
                action = a;
                maxValue = actionScore;
                isUnique = true;
            }
            else if (actionScore == maxValue) {
                isUnique = false;
            }
        }
        action = !isUnique && unique ? -1 : action;
        return action;
    }

    private double getMaxValue(int[] pos, long eaten) {
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

    public int[][] getPolicy(long eaten) {
        int[][] policy = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pos = new int[]{x, y};
                if (isStateVisited(pos, eaten)) {
                    policy[y][x] = getBestAction(new int[]{x, y}, eaten, false);
                }
                else {
                    policy[y][x] = -1;
                }
            }
        }
        return policy;
    }

    public static void main(String[] args) {
        Level map = null;
        try {
            map = Level.fromFile("1-simple.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        QLearner l =  new QLearner(4);
        l.setMap(map);
        l.train(10);
        int[][] policy = l.getPolicy(0L);
        System.out.println();
        for (int[] row : policy) {
            System.out.println(Arrays.toString(row));
        }

        policy = l.getPolicy(1L);
        System.out.println();
        for (int[] row : policy) {
            System.out.println(Arrays.toString(row));
        }
        l.map.print();
    }
}

