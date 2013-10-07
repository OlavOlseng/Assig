package checkers;

import java.util.ArrayList;

import framework.AsState;
import framework.AsStateGenerator;

public class CheckerGenerator extends AsStateGenerator{

	@Override
	public int generateHash(AsState state) {
		//generates a hashcode unique to the state
		return state.toString().hashCode();
	}

	//The heuristics function, see paper for more info.
	@Override
	public double calculateH(AsState state, AsState goalState) {
		double h = 0;
		double factor = 0.9;
		int[] board = (int[]) state.getStateData();

		for (int i = 0; i < board.length; i++) {
			if(board[i] == -1) {
				h += i;
			}
			else if (board[i] == 1) {
				h += (board.length - i);
			}
		}
		return (h*factor)/2 - goalState.getH();
	}

	//Generates all moves for a given state. The cost per move is implemented here, in arcCost. See paper for more info.
	@Override
	protected ArrayList<AsState<?>> generateChildren(AsState<?> state) {
		ArrayList<AsState<?>> children = new ArrayList<AsState<?>>();
		int[] board = (int[]) state.getStateData();
		
		double arcCost = 0.8;
		int temp;
		int[] tempBoard;

		for (int i = 0; i < board.length; i++) {
			if(board[i] == 0) {
				if(i - 2 >= 0) {
					if(board[i - 2] == 1){
						tempBoard = board.clone();
						temp = board[i - 2];
						tempBoard[i] = temp;
						tempBoard[i - 2] = 0;
						CheckerState tempState = new CheckerState(tempBoard);
						tempState.setG(state.getG() + arcCost);
						children.add(tempState);
					}
				}
				if(i - 1 >= 0) {
					if(board[i - 1] == 1){
						tempBoard = board.clone();
						temp = board[i - 1];
						tempBoard[i] = temp;
						tempBoard[i - 1] = 0;
						CheckerState tempState = new CheckerState(tempBoard);
						tempState.setG(state.getG() + arcCost);
						children.add(tempState);
					}
				}
				if(i + 1 < board.length) {
					if(board[i + 1] == -1) {
						tempBoard = board.clone();
						temp = board[i + 1];
						tempBoard[i] = temp;
						tempBoard[i + 1] = 0;
						CheckerState tempState = new CheckerState(tempBoard);
						tempState.setG(state.getG() + arcCost);
						children.add(tempState);
					}
				}
				if(i + 2 < board.length) {
					if(board[i + 2] == -1) {
						tempBoard = board.clone();
						temp = board[i + 2];
						tempBoard[i] = temp;
						tempBoard[i + 2] = 0;
						CheckerState tempState = new CheckerState(tempBoard);
						tempState.setG(state.getG() + arcCost);
						children.add(tempState);
					}
				}
				break;
			}
		}
		return children;
	}
}
