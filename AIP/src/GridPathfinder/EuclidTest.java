package GridPathfinder;

import framework.AStar;
import framework.AStarNode;
import framework.Callback;

/**
 * Created by Olav on 01.09.2014.
 */
public class EuclidTest {
    public static void main(String args[]) {

        int[][] board = {
                {0, 0, 1, 0},
                {1, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0}
        };

        GridState start = new GridState(board, board[0].length, board.length, 0, 0);
        GridState end = new GridState(board, board[0].length, board.length, 3, 0);

        AStar aStar = new AStar(new EuclidianGridHandler(), start, end, new Callback<AStarNode>() {
            @Override
            public void callback(AStarNode data) {
                System.out.println(data.state);
            }
        });
        boolean b = aStar.run();
        System.out.println(b);
    }
}
