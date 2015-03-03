package boids;

/**
 * Created by Olav on 03.02.2015.
 */
public class Obstacle extends Boid{


    public Obstacle(float x, float y) {
        super(x, y, Type.OBSTACLE);
        this.maxAcceleration = 0f;
        this.maxSpeed = 0f;
    }

    @Override
    public void doPreTickCalculations() {
    }

}
