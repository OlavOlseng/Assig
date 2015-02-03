package boids;

import utils.Tickable;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 29/01/2015.
 */
public class BoidWorld implements Tickable{

    public static final float BOUNDS_WORLD_WIDTH = 100.0f;
    public static final float BOUNDS_WORLD_HEIGHT = 100.0f;

    private ArrayList<Boid> boids;

    public BoidWorld() {
        this.boids = new ArrayList<Boid>();
    }

    public void addBoid(Boid.Type type) {
        float y = (float)Math.random() * BOUNDS_WORLD_HEIGHT;
        float x = (float)Math.random() * BOUNDS_WORLD_WIDTH;
        Boid b = null;

        if (type == Boid.Type.BIRD) {
            b = new Bird(x, y);
        }

        if (b != null) {
            boids.add(b);
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
