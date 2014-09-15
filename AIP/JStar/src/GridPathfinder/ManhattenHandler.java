package GridPathfinder;

import framework.AStarStateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 03/09/2014.
 */
public class ManhattenHandler extends AStarStateHandler<GridState> {
    private static final double COST_STRAIGHT = 1.0;

    @Override
    public int generateHash(GridState state) {
        String s = state.x + ":" + state.y;
        int h = s.hashCode();
        return h;
    }

    @Override
    public double calculateH(GridState state, GridState goalState) {
        double h = Math.abs(state.x - goalState.x) + Math.abs(state.y - goalState.y);
        return h;
    }

    @Override
    protected List<GridState> generateChildren(GridState state) {
        List<GridState> children = new ArrayList<GridState>();

        int[][] board = state.getStateData();
        state.getStateData()[state.y][state.x] = 2;

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
        return children;
    }
}
