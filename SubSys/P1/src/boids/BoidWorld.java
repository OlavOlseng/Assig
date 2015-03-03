package boids;

import utils.Tickable;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 29/01/2015.
 */
public class BoidWorld implements Tickable{

    public static final float BOUNDS_WORLD_WIDTH = 250.0f;
    public static final float BOUNDS_WORLD_HEIGHT = 250.0f;
    public static int NUMBER_BIRDS = 300;

    private ArrayList<Boid> boids;

    public BoidWorld() {
        this.boids = new ArrayList<Boid>();
    }

    public void addBoid(Boid.Type type) {
        System.out.println("Adding boid...");
        float y = (float)Math.random() * BOUNDS_WORLD_HEIGHT;
        float x = (float)Math.random() * BOUNDS_WORLD_WIDTH;
        Boid b = null;

        if (type == Boid.Type.BIRD) {
            System.out.println("Bird selected...");
            b = new Bird(x, y);
        }
        else if (type == Boid.Type.OBSTACLE) {
            System.out.println("Obstacle selected...");
            b = new Obstacle(x, y);
        }
        else if (type == Boid.Type.PREDATOR) {
            System.out.println("Predator selected...");
            b = new Predator(x, y);
        }

        if (b != null) {
            boids.add(b);
            System.out.println("Added boid successfully");
        }
    }

    @Override
    public void onTick(double dt) {
        collisionCheck(boids);
        for (Boid b : boids) {
            b.doPreTickCalculations();
        }
        for (Boid b : boids) {
            b.tick(dt);
        }
    }

    public List<Boid> getAllBoids() {
        return this.boids;
    }

    /**
     * Removes all boids from the world that matches the type parameter.
     * @param type
     */
    public void wipe(Boid.Type type) {
        ArrayList<Boid> toWipe = new ArrayList<Boid>();
        for (Boid b : this.boids) {
            if (b.getType() == type) {
                toWipe.add(b);
            }
        }
        for (Boid b : toWipe) {
            this.boids.remove(b);
        }
    }


    /**
     * This method performs collision checks, which in this case is a vision check.
     * @param allBoids
     */
    public void collisionCheck(List<Boid> allBoids) {
        for (int i = 0; i < allBoids.size() - 1; i++) {
            for (int j = i+1; j < allBoids.size(); j++) {
                Boid b1 = allBoids.get(i);
                Boid b2 = allBoids.get(j);

                float dist = Utils.vecLength(Math.abs(b1.x - b2.x), Math.abs(b1.y - b2.y));
                if (dist <= b1.visionRadius) {
                    b1.neighbours.add(b2);
                }
                if (dist <= b2.visionRadius) {
                    b2.neighbours.add(b1);
                }
            }
        }
    }
}
