package Boids;

/**
 * Created by Olav on 29/01/2015.
 */
public class Entity {

    float x, y;
    float dx, dy;
    float ddx, ddy;

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
    public void tick(float dt) {
        dx += ddx * dt;
        dy += ddy * dt;
        x += dx;
        y += dy;
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
