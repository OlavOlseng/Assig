package utils;

import java.util.ArrayList;

/**
 * Created by Olav on 29/01/2015.
 */
public class Ticker implements Runnable{

    Thread loop;
    private int fps;
    private int currentFPS = 0;
    private int frameCount = 0;
    double lastCounterUpdate;
    private ArrayList<Tickable> tickables;

    public Ticker(int fps){
        this.tickables = new ArrayList<Tickable>();
        this.fps = fps;
    }

    public void init(){
        loop = new Thread(this);
        loop.setPriority(Thread.MAX_PRIORITY);
        loop.start();
    }

    @Override
    public void run() {
        System.out.println("Starting Loop...");
        double dt;
        double nowTime;
        double fraction = 1000.0/(double)fps;
        double lastUpdate = System.currentTimeMillis();

        while(true){
            nowTime = System.currentTimeMillis();
            dt = nowTime - lastUpdate;
            while(dt < fraction){
                try{
                    Thread.sleep((long) (fraction - dt));

                } catch (Exception e){
                    e.printStackTrace();
                }
                dt = System.currentTimeMillis() - lastUpdate;
            }
            lastUpdate = System.currentTimeMillis();
            updateFPS(dt);
            onTick(dt);
        }
    }

    public void setFpsCap(int fps) {
        this.fps = fps;
    }

    protected void updateFPS(double dt) {
        frameCount++;
        lastCounterUpdate += dt;
        if(lastCounterUpdate > 1000.0)
        {
            lastCounterUpdate -= 1000.0;
            currentFPS = frameCount;
            frameCount = 0;
        }
    }

    protected int getFPS() {
        return currentFPS;
    }

    public void addTickable(Tickable t) {
        this.tickables.add(t);
    }

    public void removeTickable(Tickable t) {
        tickables.remove(t);
    }

    public void onTick(double dt) {
        for (Tickable t : tickables) {
            t.onTick(dt);
        }
    }
}
