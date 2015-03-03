package boids;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

/**
 * Created by Olav on 29/01/2015.
 */
public abstract class Boid extends Entity {

    public enum Type{
        BIRD(0),
        OBSTACLE(1),
        PREDATOR(2);

        private int val;

        Type(int val) {
            this.val = val;
        }
    };

    public static final float RADIUS_BIRD = 1.f;
    public static final float RADIUS_OBSTACLE = 2.50f;
    public static final float RADIUS_PREDATOR = 2.0f;
    public static final float RADIUS_DEFAULT = 15.0f;

    public static final float VISION_RADIUS_BIRD = 17.5f;
    public static final float VISION_RADIUS_OBSTACLE = 0.0f;
    public static final float VISION_RADIUS_PREDATOR = 35.0f;
    public static final float VISION_RADIUS_DEFAULT = 15.0f;

    protected List<Boid> neighbours = null;

    public float visionRadius;
    public float radius;

    private Type type;

    public Boid(float x, float y, Type t) {
        super();
        this.type = t;
        init();
        neighbours = new ArrayList<Boid>();
        this.forcePosition(x, y);
    }

    private void init() {
        switch (this.type.val) {
            case 0:
                this.radius = RADIUS_BIRD;
                this.visionRadius = VISION_RADIUS_BIRD;
            break;
            case 1:
                this.radius = RADIUS_OBSTACLE;
                this.visionRadius = VISION_RADIUS_OBSTACLE;
            break;
            case 2:
                this.radius = RADIUS_PREDATOR;
                this.visionRadius = VISION_RADIUS_PREDATOR;
            break;
            default:
                this.radius = RADIUS_DEFAULT;
                this.visionRadius = VISION_RADIUS_DEFAULT;
        }
    }

    public Type getType() {
        return this.type;
    }

    /**
     * This function should do all the calculations and apply correct impulses to the boid. This is the first thing called in every timestep (before the tick() function).
     */
    public abstract void doPreTickCalculations();

    @Override
    public void tick(double dt) {
        super.tick(dt);

       //handle world wrapping.
        this.x += BoidWorld.BOUNDS_WORLD_WIDTH * 100;
        this.y += BoidWorld.BOUNDS_WORLD_HEIGHT * 100;
        this.x %= BoidWorld.BOUNDS_WORLD_WIDTH;
        this.y %= BoidWorld.BOUNDS_WORLD_HEIGHT;

        this.neighbours = new ArrayList<Boid>();
    }
}
