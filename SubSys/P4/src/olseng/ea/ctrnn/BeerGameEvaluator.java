package olseng.ea.ctrnn;

import olseng.ea.FitnessEvaluator;

/**
 * Created by Olav on 13.04.2015.
 */
public class BeerGameEvaluator implements FitnessEvaluator<CTRNN> {

    public static final int MODE_STANDARD = 0;
    public static final int MODE_PULL = 1;
    public static final int MODE_NO_WRAP = 2;

    private double captureSmall;
    private double captureBig;
    private double avoidSmall;
    private double avoidBig;

    private int timesteps = 600;

    public static int MODE = 0;

    public BeerGameEvaluator(double captureSmall, double avoidBig, double avoidSmall, double captureBig) {
        this.captureSmall = captureSmall;
        this.avoidBig = avoidBig;
        this.avoidSmall = avoidSmall;
        this.captureBig = captureBig;
    }

    @Override
    public double evaluate(CTRNN phenotype) {
        int gamesPlayed = 0;
        double[] results = new double[4];
        int stepsLeft = timesteps;
        BeerGame bg = new BeerGame(30, 15);
        if (MODE == MODE_NO_WRAP) {
            bg.wrapping = false;
        }
        bg.newDrop();
        phenotype.flush();

        while (stepsLeft > 0) {
            if (bg.done) {
                addResult(bg, results);
                gamesPlayed++;
                bg.newDrop();
                //phenotype.flush();
            }
            move(bg, phenotype);
            stepsLeft--;
        }
        double util = 0;
        if (MODE != MODE_PULL) {
            double util1 = results[0] * captureSmall;
            double util2 = results[2] * avoidBig;
            util1 /= results[1];
            util2 /= results[3];
            util += (util1 + util2);
        }
        else {
            util += results[0] * captureSmall;
            util -= (results[1] - results[0]) * avoidSmall;
            util += results[2] * avoidBig;
            util -= (results[3] - results[2]) * captureBig;
        }
        return util;
    }

    /**
     * Has the agent make a move given the Beer game.
     * @param bg
     * @param agent
     */
    public static void move(BeerGame bg, CTRNN agent) {
        agent.setInputs(bg.getSensorReadings());
        agent.propagate();
        double[] outputs = agent.getOutput();
        double moveD = (-outputs[0] + outputs[1]) * 4.0;
        if (MODE == MODE_PULL) {
            if (moveD < agent.pullThreshold) {
                bg.pull();
                return;
            }
        }
        int move = (int)Math.round(moveD);
        bg.step(move);
    }

    public static void addResult(BeerGame bg, double[] current) {
        int[] results = bg.getResult();
        if (results[1] == 1) {
            current[1]++;
            if(results[0] == 1) {
                current[0]++;
            }
        }
        else{
            current[3]++;
            if(results[0] == 0) {
                current[2]++;
            }
        }
    }
}
