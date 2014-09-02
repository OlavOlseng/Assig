package GridPathfinder;

import com.sun.javafx.geom.transform.GeneralTransform3D;
import framework.AStarStateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 01.09.2014.
 */
public class EuclidianGridHandler extends AStarStateHandler<GridState> {

    private static final double COST_STRAIGHT = 1.0;
    private static final double COST_DIAGONAL = Math.sqrt(2.0);


    @Override
    public int generateHash(GridState state) {
        String s = state.x + ":" + state.y;
        int h = s.hashCode();
        return s.hashCode();
    }

    /**
     * This heruritic is a simple euclidian distance.
     * @param state
     * @param goalState
     * @return
     */
    @Override
    public double calculateH(GridState state, GridState goalState) {
        double h = Math.sqrt(Math.pow(goalState.x - state.x, 2) + Math.pow(goalState.x - state.x, 2));
        return h;
    }


    /**
     * Generates all 8 possible valid states
     * @param state
     * @return
     */
    @Override
    protected List<GridState> generateChildren(GridState state) {
        List<GridState> children = new ArrayList<GridState>();

        int[][] board = state.getStateData();

        int x = state.x;
        int y = state.y;
        boolean l = false,
                r = false,
                u = false,
                d = false;

        //directional check:
        if(x - 1 >= 0) {
            if (board[y][x-1] == 0) {
                children.add(new GridState(board, state.width, state.height, x - 1, y, state.getG() + COST_STRAIGHT));
                l = true;
            }
        }
        if (y - 1 >= 0) {
            if (board[y-1][x] == 0) {
                children.add(new GridState(board, state.width, state.height, x, y-1, state.getG() + COST_STRAIGHT));
                d = true;
            }
        }
        if(x + 1 < state.width) {
            if (board[y][x + 1] == 0) {
                children.add(new GridState(board, state.width, state.height, x + 1, y, state.getG() + COST_STRAIGHT));
                r = true;
            }
        }
        if (y + 1 < state.height) {
            if (board[y + 1][x] == 0) {
                children.add(new GridState(board, state.width, state.height, x, y + 1, state.getG() + COST_STRAIGHT));
                u = true;
            }
        }

        //diagonal checks:
        if (l && d) {
            if (board[y-1][x-1] == 0) {
                children.add(new GridState(board, state.width, state.height, x-1, y-1, state.getG() + COST_DIAGONAL));
            }
        }
        if(l && u) {
            if(board[y+1][x-1] == 0) {
                children.add(new GridState(board, state.width, state.height, x-1, y+1, state.getG() + COST_DIAGONAL));
            }
        }
        if(r && d) {
            if(board[y-1][x+1] == 0) {
                children.add(new GridState(board, state.width, state.height, x+1, y-1, state.getG() + COST_DIAGONAL));
            }
        }
        if(r && u) {
            if(board[y+1][x+1] == 0) {
                children.add(new GridState(board, state.width, state.height, x+1, y+1, state.getG() + COST_DIAGONAL));
            }
        }
        return children;
    }
}
