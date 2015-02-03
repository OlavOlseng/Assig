package GUI;

import boids.Boid;
import boids.BoidWorld;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Tickable;
import utils.Utils;

/**
 * Created by Olav on 30.01.2015.
 */
public class BoidRenderer extends Canvas implements Tickable{

    private BoidWorld world;

    public BoidRenderer(BoidWorld world, double size_canvas_width, double size_canvas_height) {
        super(size_canvas_width, size_canvas_height);
        this.world = world;
    }

    @Override
    public void onTick(double dt) {
        GraphicsContext g = this.getGraphicsContext2D();
        double xScale = this.getWidth() / BoidWorld.BOUNDS_WORLD_WIDTH;
        double yScale =  this.getHeight() / BoidWorld.BOUNDS_WORLD_HEIGHT;

        g.setFill(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setFill(Color.DEEPPINK);
        for (Boid b : world.getAllBoids()) {
            g.fillOval((b.x - b.radius) * xScale, (b.y - b.radius) * yScale, 2 * b.radius * xScale, 2 * b.radius * yScale);
            g.setFill(Color.GREEN);

            float len = Utils.vecLength(b.dx, b.dy);
            g.setLineWidth(2.0);
            g.strokeLine(b.x * xScale, b.y * yScale, (b.x + b.dx/len * b.radius) * xScale, (b.y + b.dy/len * b.radius) * yScale );
        }
    }
}
