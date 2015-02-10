package boids;

import utils.Utils;

import java.util.List;

/**
 * Created by Olav on 09.02.2015.
 */
public class Predator extends Boid {

    public static float SCALE_COHESION = 100.f;
    public static float SCALE_SEPARATION = 0.1f;
    public static float SCALE_EVASION = 4000.0f;



    public Predator(float x, float y) {
        super(x, y, Type.PREDATOR);
        maxSpeed = 40.0f;
        maxAcceleration = 150.0f;
    }

    @Override
    public void doPreTickCalculations() {
        ddx = 0;
        ddy = 0;
        calculateCohesion(neighbours);
        calculateSeparation(neighbours);
    }

    private void calculateCohesion(List<Boid> neighbours) {
        float cX = 0;
        float cY = 0;

        for (Boid b : neighbours) {
            if(b.getType() != Type.BIRD) {
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

    private void calculateSeparation(List<Boid> neighbours) {
        float sX = 0;
        float sY = 0;

        for (Boid b : neighbours) {
            float cX = this.x - b.x;
            float cY = this.y - b.y;

            float len = Utils.vecLength(cX, cY);
            if (len < (this.radius + b.radius) * 1.1f) {
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
}
