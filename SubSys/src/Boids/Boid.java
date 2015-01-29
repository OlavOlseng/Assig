package Boids;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import utils.utils;

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

    public static final float RADIUS_BIRD = 15.0f;
    public static final float RADIUS_OBSTACLE = 15.0f;
    public static final float RADIUS_PREDATOR = 15.0f;
    public static final float RADIUS_DEFAULT = 15.0f;

    public static final float VISION_RADIUS_BIRD = 15.0f;
    public static final float VISION_RADIUS_OBSTACLE = 15.0f;
    public static final float VISION_RADIUS_PREDATOR = 15.0f;
    public static final float VISION_RADIUS_DEFAULT = 15.0f;

    protected List<Boid> neighbours = null;

    private float visionRadius;
    private float radius;

    private Type type;

    //MOVE THIS FUNCTION OUT TO UPDATE LOOP MAYBE?
    public void calculateNeighbours(List<Boid> allBoids) {
        this.neighbours = new ArrayList<Boid>();
        for (int i = 0; i < allBoids.size() - 1; i++) {
            for (int j = i+1; j <= allBoids.size(); j++) {
                Boid b1 = allBoids.get(i);
                Boid b2 = allBoids.get(j);

                float dist = utils.vecLength(b1.x - b2.x, b1.y - b2.y) - b1.radius - b2.radius;
                if (dist <= b1.visionRadius) {
                    b1.neighbours.add(b2);
                }
                if (dist <= b2.visionRadius) {
                    b2.neighbours.add(b1);
                }
            }
        }
    }


    public Boid(float x, float y, Type t) {
        super();
        this.type = t;
        init();
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

    public abstract void doPreTickCalculations();

    @Override
    public void tick(float dt) {
        this.neighbours = null;
        super.tick(dt);
    }

    public static void main(String[] args) {
        System.out.println(Type.BIRD);
        System.out.println(Type.OBSTACLE);
        System.out.println(Type.PREDATOR);
    }
}
