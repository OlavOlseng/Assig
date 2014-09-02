package GridPathfinder;

import framework.AStar;
import framework.AStarNode;
import framework.Callback;

/**
 * Created by Olav on 01.09.2014.
 */
public class EuclidTest {
    public static void main(String args[]) {

        int[][] board = new int[10000][10000];


        GridState start = new GridState(board, board[0].length, board.length, 0, 0);
        GridState end = new GridState(board, board[0].length, board.length, 9999, 1000);

        AStar aStar = new AStar(new EuclidianGridHandler(), start, end, new Callback<AStar<GridState>>() {

            @Override
            public void callback(AStar<GridState> data) {
//                System.out.println(data.state);
//                System.out.println("G: " + data.state.getG() + "\tH: " + data.state.getH() + "\tF: " + data.state.getF());
//                for (AStarNode n : data.open) {
//                    System.out.println(n.state.toString() + "\t" + n.getF());
//
//                }
//                System.out.println();
            }

        });
        long startTime = System.currentTimeMillis();
        boolean b = aStar.run();
        System.out.println("Path found: " + b);
        System.out.println("Runtime: " + (System.currentTimeMillis() - startTime) + " ms");

        AStarNode current = aStar.lastExpanded;
        while (current != null) {
//            System.out.println(current.state);
            current = current.getParent();
        }
    }
}
