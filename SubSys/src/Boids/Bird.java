package Boids;

import java.util.List;

/**
 * Created by Olav on 29/01/2015.
 */
public class Bird extends Boid {

    public static float SCALE_COHESION = 1.0f;
    public static float SCALE_ALIGNMENT = 1.0f;
    public static float SCALE_SEPARATION = 1.0f;

    public Bird(float x, float y) {
        super(x, y, Type.BIRD);
    }

    @Override
    public void doPreTickCalculations() {
        ddx = 0;
        ddy = 0;
        calculateCohesion(neighbours);
        calculateAlignment(neighbours);
        calculateSeparation(neighbours);
    }

    private void calculateCohesion(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            cX += b.x - this.x;
            cY += b.y - this.y;
        }

        this.ddx += cX * SCALE_COHESION;
        this.ddy += cY * SCALE_COHESION;
    }

    private void calculateAlignment(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            cX += b.dx;
            cY += b.dy;
        }

        this.ddx += cX * SCALE_ALIGNMENT;
        this.ddy += cY * SCALE_ALIGNMENT;
    }

    private void calculateSeparation(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            cX += this.x - b.x;
            cY += this.y - b.y;
        }
        /*
        this.ddx += cX;
        this.ddy += cY;
        */
    }
}
