package boids;

import utils.Utils;

import javax.naming.NameNotFoundException;

/**
 * Created by Olav on 29/01/2015.
 */
public class Entity {

    public float x, y;
    public float dx, dy;
    public float ddx, ddy;
    protected float maxSpeed = 1000000000;
    protected float maxAcceleration = 1000000000;

    public Entity(float x, float y, float dx, float dy, float ddx, float ddy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.ddx = ddx;
        this.ddy = ddy;
    }

    public Entity(){
        this(0.f, 0.f, 0.f, 0.f, 0.f, 0.f);
    }

    /***
     * Updates velocities and position. If overridden by subclasses, it must be called by the overriding funtion.
     * @param dt - The change in time in seconds since the last tick was called.
     */
    public void tick(double dt) {
        float len = Utils.vecLength(ddx, ddy);
        if (len > this.maxAcceleration && len != 0) {
            ddx = (ddx / len) * maxAcceleration;
            ddy = (ddy / len) * maxAcceleration;
        }

        dx += ddx * (float)dt;
        dy += ddy * (float)dt;
        len = Utils.vecLength(dx, dy);

        if (len > this.maxSpeed & len != 0) {
            //System.out.println("BIGSPEED");
            dx = (dx / len) * maxSpeed;
            dy = (dy / len) * maxSpeed;
        }
        x += dx*(float)dt;
        y += dy*(float)dt;
    }

    /***
     * Sets the acceleration in x and y directions.
     * @param ddx
     * @param ddy
     */
    public void accelerate(float ddx, float ddy) {
        this.ddx = ddx;
        this.ddy = ddy;
    }

    public void wipeAcceleration() {
        accelerate(0.f, 0.f);
    }

    /***
     * Forces the given velocites. Should be called in conjunction with wipeAcceleration();
     * @param dx
     * @param dy
     */
    public void forceVelocity(float dx, float dy){
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Hard sets the given position.
     * @param x
     * @param y
     */
    public void forcePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
