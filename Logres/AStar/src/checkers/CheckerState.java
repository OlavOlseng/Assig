//Simple state class, to make casting easier and to generate a hash from the printout function
package checkers;

import framework.AsState;

public class CheckerState extends AsState<int[]>{

	public CheckerState(int[] stateData) {
		super(stateData);
	}

	@Override
	public String toString() {
		String s = new String();
		int[] board = this.stateData;
		s += "[";
		for (int i = 0; i < board.length; i++) {
			s += String.format("%d", board[i]);
		}
		s += "]";
		return s;
	}
}
