package GridPathfinder.gui;

import GridPathfinder.GridState;
import framework.AStar;
import framework.AStarNode;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Olav on 02.09.2014.
 */
public class AStarCanvas extends Canvas{
    int h, w;
    int rectangleSide = 0;
    private AStar<GridState> astar;
    private boolean shouldClear = true;

    public AStarCanvas(AStar<GridState> astar) {
        this.astar = astar;
        this.setBackground(Color.WHITE);
    }

    public void calculateRectangleSize() {
        double maxBoxes = Math.max(astar.goalNode.state.width, astar.goalNode.state.height);
        double minSidePixels = Math.min(w, h);
        if (maxBoxes <= 0 || minSidePixels == 0) {
            return;
        }
        this.rectangleSide =  (int)(minSidePixels / maxBoxes);
    }

    public void setClearingMode(boolean shouldClear) {
        this.shouldClear = shouldClear;
    }

    public void resize() {
        w = getSize().width;
        h = getSize().height;
        calculateRectangleSize();
        render();
    }

    @Override
    public void paint(Graphics g) {

    }

    public void render() {
        //Fill in start and goal nodes
        BufferStrategy  bs = getBufferStrategy();

        if(bs == null) {
            createBufferStrategy(2);
            requestFocus();
            bs = getBufferStrategy();
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.white);
        if(shouldClear == true) {
            g.clearRect(0,0,getWidth(),getHeight());
        }
        g.setColor(Color.green);
        GridState s = astar.goalNode.state;
        g.fillRect(s.x * rectangleSide, s.y * rectangleSide, rectangleSide, rectangleSide);

        g.setColor(Color.BLUE);
        s = astar.initialNode.state;
        g.fillRect(s.x * rectangleSide, s.y * rectangleSide, rectangleSide, rectangleSide);

        //Fill in walls
        for (int y = 0; y < s.getStateData().length; y++) {
            for (int x = 0; x < s.getStateData()[0].length; x++) {
                if (s.getStateData()[y][x] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * rectangleSide, y * rectangleSide, rectangleSide, rectangleSide);
                }
                else if (s.getStateData()[y][x] == 2) {
                    g.setColor(Color.lightGray);
                    g.fillRect(x * rectangleSide, y * rectangleSide, rectangleSide, rectangleSide);
                }
                else if (s.getStateData()[y][x] == 0){
                    //Draw boxes
//                    g.setColor(Color.BLACK);
//                    g.drawRect(x * rectangleSide, y * rectangleSide, rectangleSide, rectangleSide);
                }
            }
        }

        g.setColor(Color.red);
        AStarNode<GridState> node = astar.currentNode;
        while(node != null) {
            s = node.state;
            g.fillRect(s.x * rectangleSide, s.y * rectangleSide, rectangleSide, rectangleSide);
            node = node.getParent();
        }
        g.dispose();
        bs.show();
    }
}
