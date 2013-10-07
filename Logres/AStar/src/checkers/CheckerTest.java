//This class only generates the initial board and goalstate, runs the algorithm and prints the solution, if it is found.

package checkers;

import java.util.ArrayList;
import framework.*;

public class CheckerTest {
	int[] start;
	int[] goal;
	CheckerGenerator generator;
	AsState goalState;
	AsState startState;
	AStar astar;
	//Change this variable to try different puzzle sizes
	public static final int PIECES_PR_SIDE = 12;
	
	
	public CheckerTest(int piecesPerSide) {
		start = generateBoard(piecesPerSide, 1, -1);	
		goal = generateBoard(piecesPerSide, -1, 1);
		goalState = new CheckerState(goal);
		startState = new CheckerState(start);
		
		generator = new CheckerGenerator();
		astar = new AStar(generator, startState, goalState);
	}
	
	private int[] generateBoard(int piecesPerSide, int p1, int p2) {
		int[] board = new int[piecesPerSide * 2 + 1];
		for (int i = 0; i < board.length; i++) {
			if(i < ((board.length-1)/2)) {
				board[i] = p1;
			}
			else if(i == (board.length - 1) / 2) {
				board[i] = 0;
			}
			else {
				board[i] = p2;
			}
		}
		return board;
	}
	
	public void run() {
		if (astar.run()) {
			AsNode node = astar.goalNode;
			int steps = 0;
			while(node.getParent() != null) {
				steps++;
				System.out.println(node.getState() + String.format(" f: %f, g: %f,h: %f", node.getState().getF(), node.getState().getG(), node.getState().getH())); 
				node = node.getParent();
			}
			System.out.println(node.getState() + String.format(" f: %f, g: %f,h: %f", node.getState().getF(), node.getState().getG(), node.getState().getH()));
			System.out.println("Result steps: " + steps);
			System.out.println("Expanded nodes: " + astar.expandedNodes);
			node = astar.goalNode;
			System.out.println("Goal State: " + node.getState() + String.format(" f: %f, g: %f,h: %f", node.getState().getF(), node.getState().getG(), node.getState().getH()));
		}
		else {System.out.println("FAILED TO FIND A SOLUTION");}; 
	}
	
	public static void main(String[] args) {
		CheckerTest t = new CheckerTest(CheckerTest.PIECES_PR_SIDE);
		long time = System.currentTimeMillis();
		t.run();
		long now = System.currentTimeMillis();
		System.out.println("Runtime: " + (now - time));
	}
}