package com.olseng.QL;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Olav on 12.05.2015.
 */
public class QLearner {

    public static float SCORE_FOOD = 10f;
    public static float SCORE_POISON = -50f;
    public static float SCORE_NOTHING = -1.801f;

    private int width, height, actionCount;
    private Random random = new Random(System.currentTimeMillis());
    private Level map;
    public float learningRate = 0.1f;
    public float discountRate = 0.89f;

    public float wobble = 0.6f;

    short shortPos;

    private int maxBackstack = 3;

    HashMap<Long, HashMap<Short, float[]>> stateMap;
    int[][] positionQueue;
    long[] eatenQueue;
    byte[] actionQueue;
    float[] rewardQueue;
    public boolean escape = false;

    byte[][] policy;
    private long lastRequestedPolicy;
    private int stepCounter = 0;

    public QLearner(int actionCount) {
        this.actionCount = actionCount;
        setMaxBackstack(maxBackstack);
    }

    public void setMap(Level map) {
        this.map = map;
        this.width = map.width;
        this.height = map.height;
        initialize();
    }

    public void initialize() {
        stateMap = new HashMap<Long,HashMap<Short, float[]>>();
        policy = null;
        clearBacktrace();
    }


    @Deprecated
    public void train(int iterations) {
        int K = 0;

        while(K < iterations) {
            train(K, iterations);
            K++;
        }
    }

    public void train(int currentIteration, int totalIterations) {
        escape = false;
        Level map = this.map.copy();
        long eaten = 0;

        float progress = (float)currentIteration / totalIterations;

        int[] newPos = new int[2];
        int[] oldPos = new int[2];

        newPos[0] = map.getPlayerX();
        newPos[1] = map.getPlayerY();
        float temperature = 0.001f + wobble * (1f - (float)Math.pow(progress, 0.5));

        while(!map.gameOver() && !escape) {
            oldPos[0] = newPos[0];
            oldPos[1] = newPos[1];

            byte action = selectAction(oldPos, eaten, temperature);
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

    public void setMaxBackstack(int maxBackstack) {
        this.maxBackstack = maxBackstack;
        this.positionQueue = new int[maxBackstack][2];
        this.eatenQueue = new long[maxBackstack];
        this.rewardQueue = new float[maxBackstack];
        this.actionQueue = new byte[maxBackstack];
    }

    private void addBacktrace(int[] oldPos, long eaten, byte action, float reward) {
        int index = stepCounter % maxBackstack;
        this.positionQueue[index][0] = oldPos[0];
        this.positionQueue[index][1] = oldPos[1];
        this.eatenQueue[index] = eaten;
        this.rewardQueue[index] = reward;
        this.actionQueue[index] = action;
        stepCounter++;
    }

    private void clearBacktrace() {
        stepCounter = 0;
    }

    private byte selectAction(int[] pos, long eaten, float randomChance) {
        double rand = random.nextDouble();
        if (rand < randomChance) {
            /*
            float[] indices = new float[actionCount];
            float[] utilities = getState(pos, eaten);
            float[] actions = new float[actionCount];
            float min = actions[0];
            for (int i = 0; i < indices.length; i++) {
                if(actions[i] < min) {
                    min = actions[i];
                }
            }

            float sum = -min;
            for (int i = 0; i < indices.length; i++) {
                float utility = utilities[i];
                sum += utility;
                indices[i] = sum;
            }

            rand *= sum;
            int action = 0;
            for (int i = 0; i < indices.length; i++) {
                if (indices[i] > rand) {
                    action = i;
                }
            }
            return action;
            */
            return (byte)(random.nextInt(actionCount));
        }
        return getBestAction(pos, eaten, false);
    }



    public float[] getState(int[] position, long eaten) {

        if (!stateMap.containsKey(eaten)) {
            stateMap.put(eaten,new HashMap<Short, float[]>());
        }
        HashMap<Short, float[]> foodState = stateMap.get(eaten);
        shortPos = (short)(position[0] << 8);
        shortPos |= position[1];

        if (!foodState.containsKey(shortPos)) {
            float[] vals = new float[actionCount];
            foodState.put(shortPos, vals);
        }
        return foodState.get(shortPos);
    }

    private void runBacktrace(int[] newPos, long newEaten) {
        for (int i = 0; i < actionQueue.length; i++) {
            int index = (stepCounter - 1 - i) % maxBackstack;
            if(index < 0) {
                break;
            }
            float resultScore = rewardQueue[index];
            int[] oldPos = positionQueue[index];
            long eaten = eatenQueue[index];
            int action = actionQueue[index];
            float scoreDiff = learningRate * (resultScore + discountRate * (getMaxValue(newPos, newEaten)) - getState(oldPos, eaten)[action]);

            getState(oldPos, eaten)[action] += scoreDiff;

            newPos = oldPos;
            newEaten = eaten;
        }
    }

    private void updateStateActionMap(int[] newPos, int[] oldPos, long eaten, byte action, int result) {
        long newEaten = eaten;
        boolean clear = false;
        if (result > 0) {
            newEaten = map.foodEaten;
        }
        float resultScore = getResultScore(result);

        addBacktrace(oldPos, eaten, action, resultScore);

        runBacktrace(newPos, newEaten);
        if (clear) {
            clearBacktrace();
        }
    }

    private float getResultScore(int result) {
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
            stateMap.put(eaten,new HashMap<Short, float[]>());
        }
        HashMap<Short, float[]> foodState = stateMap.get(eaten);
        shortPos = (short)(position[0] << 8);
        shortPos |= position[1];

        return foodState.containsKey(shortPos);
    }

    public byte getBestAction(int[] pos, long eaten, boolean unique) {
        int action = 0;

        float[] stateActionMap = getState(pos, eaten);
        float maxValue = stateActionMap[0];
        boolean isUnique = true;
        for (int a = 1; a < stateActionMap.length; a++) {
            float actionScore = stateActionMap[a];
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
        return (byte)action;
        //return actions.get((int)(random.nextInt(actions.size())));
    }

    private float getMaxValue(int[] pos, long eaten) {
        float[] stateActionMap = getState(pos, eaten);
        float maxVal = stateActionMap[0];

        for (int a = 1; a < stateActionMap.length; a++) {

            float actionScore = stateActionMap[a];
            if (actionScore > maxVal) {
                maxVal = actionScore;
            }
        }
        return maxVal;
    }

    public byte[][] getPolicy(long eaten) {
        if (policy != null && lastRequestedPolicy == eaten) {
            return policy;
        }
        if (policy == null) {
            policy = new byte[height][width];
        }
        lastRequestedPolicy = eaten;
        int[] pos = new int[2];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos[0] = x;
                pos[1] = y;
                if (isStateVisited(pos, eaten)) {
                    policy[y][x] = getBestAction(pos, eaten, false);
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
        byte[][] policy = l.getPolicy(0L);
        System.out.println();
        for (byte[] row : policy) {
            System.out.println(Arrays.toString(row));
        }

        policy = l.getPolicy(1L);
        System.out.println();
        for (byte[] row : policy) {
            System.out.println(Arrays.toString(row));
        }
        l.map.print();
    }

    public int getMaxBackstack() {
        return maxBackstack;
    }
}

