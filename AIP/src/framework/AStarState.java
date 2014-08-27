package framework;

import java.util.List;

/**
 * Created by Olav on 27/08/2014.
 */
public abstract class AStarState<T> {
    protected T stateData;
    private double f, g, h;
    private int hash;

    public AStarState(T stateData) {
        this.stateData = stateData;
        this.hash = generateHash();
    }

    /**
     *This method should generate all valid children with their G values set.
     * @return
     */
    public abstract List<AStarState<T>> generateChildren();

    protected abstract int generateHash();

    public abstract double calculateH();

    public void calculateF() {
        this.f =  g + h;
    }

    public AStarState<T> setG(double g){
        this.g = g;
        calculateF();
        return this;
    }

    public double getG() {
        return this.g;
    }

    public double getH() {
        return this.h;
    }

    public double getF() {
        return f;
    }

    public int getHash() {
        return hash;
    }
}
