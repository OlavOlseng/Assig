package GridPathfinder;

import framework.AStarState;

/**
 * Created by Olav on 01.09.2014.
 */
public class GridState extends AStarState<int[][]> {

    int width, height, x, y;

    public GridState(int[][] stateData, int width, int height, int x, int y) {
        super(stateData);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public GridState(int [][] stateData, int width, int height, int x, int y, double g) {
        this(stateData, width, height, x, y);
        setG(g);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
