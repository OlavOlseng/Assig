package com.olseng.QL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Olav on 12.05.2015.
 */
public class QLearner {

    public static float SCORE_FOOD = 10f;
    public static float SCORE_POISON = -50f;
    public static float SCORE_NOTHING = -2.701f;

    private int width, height, actionCount;
    private Random random = new Random(System.currentTimeMillis());
    private Level map;
    public float learningRate = 0.1f;
    public float discountRate = 0.89f;

    public float wobble = 0.5f;

    public int maxBackstack = 3;

    HashMap<Long, HashMap<Short, float[]>> stateMap;
    ArrayList<int[]> positionQueue;
    ArrayList<Long> eatenQueue;
    ArrayList<Byte> actionQueue;

    ArrayList<Float> rewardQueue;
    public boolean escape = false;

    public QLearner(int actionCount) {
        this.positionQueue = new ArrayList<int[]>();
        this.eatenQueue = new ArrayList<Long>();
        this.rewardQueue = new ArrayList<Float>();
        this.actionQueue = new ArrayList<Byte>();
        this.actionCount = actionCount;
    }

    public void setMap(Level map) {
        this.map = map;
        this.width = map.width;
        this.height = map.height;
        initialize();
    }

    public void initialize() {
        stateMap = new HashMap<Long,HashMap<Short, float[]>>();
        clearBacktrace();
    }


    @Deprecated
    public void train(int iterations) {
        int K = 0;
        float temperature = wobble;

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

                byte action = selectAction(oldPos, eaten, temperature);
                int result = map.movePlayer(action);

                newPos[0] = map.getPlayerX();
                newPos[1] = map.getPlayerY();

                updateStateActionMap(newPos, oldPos, eaten, action, result);

                if (result > 0) {
                    eaten |= 1L << (result - 1);
                }
            }
            temperature = 0.05f + wobble * (float)((iterations - K) / iterations);
            clearBacktrace();
            K++;
        }
    }

    public void train(float progress) {
        escape = false;
        Level map = this.map.copy();
        long eaten = 0;
        int[] newPos = new int[2];
        newPos[0] = map.getPlayerX();
        newPos[1] = map.getPlayerY();
        float temperature = 0.001f + wobble * (1f - (float)Math.pow(progress, 0.8));

        while(!map.gameOver() && !escape) {
            int[] oldPos = new int[2];
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

    private void addBacktrace(int[] oldPos, long eaten, byte action, float reward) {
        this.positionQueue.add(oldPos);
        this.eatenQueue.add(eaten);
        this.rewardQueue.add(reward);
        this.actionQueue.add(action);
    }

    private void clearBacktrace() {
        this.positionQueue = new ArrayList<int[]>();
        this.eatenQueue = new ArrayList<Long>();
        this.rewardQueue = new ArrayList<Float>();
        this.actionQueue = new ArrayList<Byte>();
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
        short pos = (short)(position[0] << 8);
        pos |= position[1];

        if (!foodState.containsKey(pos)) {
            float[] vals = new float[actionCount];
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
            float resultScore = rewardQueue.get(rewardQueue.size() - 1 - i);
            int[] oldPos = positionQueue.get(positionQueue.size() - 1 - i);
            long eaten = eatenQueue.get(eatenQueue.size() - 1 - i);
            int action = actionQueue.get(actionQueue.size() - 1 - i);
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

        while (actionQueue.size() > maxBackstack && maxBackstack > 0) {
            popBacktrace();
        }

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
        short pos = (short)(position[0] << 8);
        pos |= position[1];

        return foodState.containsKey(pos);
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
        byte[][] policy = new byte[height][width];
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
}

