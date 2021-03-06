package boids;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Olav on 29/01/2015.
 */
public class Bird extends Boid {

    public static float SCALE_COHESION = 100.f;
    public static float SCALE_ALIGNMENT = 0.5000f;
    public static float SCALE_SEPARATION = 10.f;
    public static float SCALE_EVASION = 5000.0f;
    public static float SCALE_FLEE = 1000000.0f;

    public static final float SCALE_COHESION_MAX = 500.0f;
    public static final float SCALE_ALIGNMENT_MAX = 3.0f;
    public static final float SCALE_SEPARATION_MAX = 80.0f;
    public static final float SCALE_EVASION_MAX = 10000.0f;
    public static final float SCALE_FLEE_MAX = 10000000.0f;

    public List<Boid> obstacles;
    public List<Boid> predators;

    public Bird(float x, float y) {
        super(x, y, Type.BIRD);
        maxSpeed = 40.0f;
        maxAcceleration = 500.0f;
    }

    @Override
    public void doPreTickCalculations() {
        ddx = 0;
        ddy = 0;
        this.obstacles = new ArrayList<Boid>();
        this.predators = new ArrayList<Boid>();
        calculateCohesion(neighbours);
        calculateAlignment(neighbours);
        calculateSeparation(neighbours);
        calculateObstacleEvasion(obstacles);
        calculatePredatorFlee(predators);
    }



    private void calculateCohesion(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            if(b.getType() != Type.BIRD) {
                if (b.getType() == Type.OBSTACLE){
                    this.obstacles.add(b);
                }
                else if (b.getType() == Type.PREDATOR)
                {
                    this.predators.add(b);
                }
                continue;
            }
            float x = b.x - this.x;
            float y = b.y - this.y;
            cX += x;
            cY += y;
        }
        float len = Utils.vecLength(cX, cY);
        if (len != 0) {
            this.ddx += cX / len * SCALE_COHESION;
            this.ddy += cY / len * SCALE_COHESION;
        }

    }

    private void calculateAlignment(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            cX += b.dx;
            cY += b.dy;
        }
        float len = Utils.vecLength(cX, cY);
        if (len != 0) {
            this.ddx += cX * SCALE_ALIGNMENT;
            this.ddy += cY * SCALE_ALIGNMENT;
        }
    }

    private void calculateSeparation(List<Boid> neighbours) {
        float sX = 0;
        float sY = 0;

        for (Boid b : neighbours) {
            float cX = this.x - b.x;
            float cY = this.y - b.y;

            float len = Utils.vecLength(cX, cY);
            if (len < (this.radius + b.radius) * 1.2f) {
                len = 0.0001f;
            }
            sX += cX / len;
            sY += cY / len;
        }
        if (sX != 0.0) {
            this.ddx += sX * SCALE_SEPARATION;
        }
        if(sY != 0.0) {
            this.ddy += sY * SCALE_SEPARATION;
        }
    }

    private void calculateObstacleEvasion(List<Boid> obstacles) {
        for (Boid o : obstacles) {

            float sx = o.x - this.x;
            float sy = o.y - this.y;
            float sLen = Utils.vecLength(sx, sy);

            sx /= sLen;
            sy /= sLen;

            //Generate normals
            float v1x = sy;
            float v1y = -sx;
            float v2x = -sy;
            float v2y = sx;

            float vx = v1x;
            float vy = v1y;

            //Choose normal that aligns the best with direction
            if (Utils.vecDot(v1x, v1y, dx, dy) < Utils.vecDot(v2x, v2y, dx, dy)) {
                vx = v2x;
                vy = v2y;
            }

            this.ddx += vx / sLen * SCALE_EVASION;
            this.ddy += vy / sLen * SCALE_EVASION;

        }
    }

    private void calculatePredatorFlee(List<Boid> predators) {
        for (Boid p : predators) {
            float sx = this.x - p.x;
            float sy = this.y - p.y;
            float sLen = Utils.vecLength(sx, sy);

            sx /= sLen;
            sy /= sLen;

            this.ddx += sx / sLen * SCALE_FLEE;
            this.ddy += sy / sLen * SCALE_FLEE;
        }
    }
}
