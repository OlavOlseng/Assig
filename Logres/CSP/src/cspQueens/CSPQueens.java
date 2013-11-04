package cspQueens;

import java.util.ArrayList;

public class CSPQueens {
	ArrayList<Integer> globalDomain;
	ArrayList<Variable> variables;
	ArrayList<int []> constraints;

	/***
	 * Returns one of the variables with the smallest domain bigger than 1.
	 * @return
	 */
	private Variable selectUnassignedVariable() {
		int min = 1000000;
		Variable mostPressured = null;
		for (Variable v: variables) {
			if (v.domain.size() > 1 && v.domain.size() <= min) {
				if (v.domain.size() == min) {
					if (Math.random() < 0.5) {
						continue;
					}
				}
				min = v.domain.size();
				mostPressured = v;
			}
		}
		return mostPressured;
	}
	
	/***
	 * Checks if all domains are of size 1, and returns true if that is the case.
	 * @return
	 */
	private boolean finished() {
		int count = 0;
		for (Variable v : variables) {
			if (v.domain.size() !=  1) {
				count++;
			}
		}
		return count == 0;
	}
	
	/***
	 * Sets up a K-queen puzzle.
	 * @param K
	 */
	private void reset(int K) {
		variables = new ArrayList<Variable>();
		globalDomain = new ArrayList<Integer>();
	
		//init domain
		for (int i = 0; i < K; i++) {
			globalDomain.add(i);
		}
		
		//init variables
		for (int i = 0; i < K; i++) {
			Variable var = new Variable(globalDomain);
			variables.add(var);
		}
	}
	
	/***
	 * Generates the constraints for a queen on row Q for a K-queen puzzle
	 * @param K
	 * @param q
	 * @return
	 */
	private ArrayList<int[]> genConstraints(int K, int q) {
		ArrayList<int[]> constraints = new ArrayList<int[]>();
		//Constriants are represented like this: [qi, qj]

		for (int i  = 0; i < K; i++) {
				if (i == q) continue;
				int[] a = {i, q};
				constraints.add(a);
		}
		return constraints;
	}

	/***
	 * Customised version of AC-3
	 * @param arcs, all arcs to be evaluated
	 * @param vars, all the variables
	 */
	public boolean AC3(ArrayList<int[]> arcs, ArrayList<Variable> vars) {

		//Check arc consistency
		while(arcs.size() > 0) {
			int[] arc = arcs.remove(0);
			Variable var1 = vars.get(arc[0]);
			Variable var2 = vars.get(arc[1]);
			int distance = Math.abs(arc[0] - arc[1]);

			//This is the revise algorithm
			boolean revised = false;
			ArrayList<Integer> toBeRemoved = new ArrayList<Integer>(); 
			for (int i = 0; i < var1.domain.size(); i++) {
				Integer val = var1.domain.get(i);
				int removed = 0;
				if (var2.domain.contains(val)) {
					removed++;
				}
				if (var2.domain.contains(val - distance)) { 
					removed++;
				}
				if (var2.domain.contains(val + distance)) {
					removed++;
				}
				if (var2.domain.size() - removed < 1) {
					toBeRemoved.add(val);
					revised = true;
				}
			}
			//If any domains were shrunk, we enter here
			if (revised) {
				for (int val : toBeRemoved) {
					if (var1.domain.contains(val)) {
						var1.moveToConflictSet(val);
					}
				}
				if (var1.domain.size() <= 0) {
					return false;
				}
				else {
					for (int i = 0; i < vars.size(); i++) {
						if (i == arc[0] || i == arc[1]) continue;
						int[] a = {i, arc[0]};
						arcs.add(a);
					}
				}
			}
		}
		return true;
	}

	private boolean KQueens(int K) {
		if(finished()) {
			return true;
		}
		Variable var;
		var = selectUnassignedVariable();
		for (int i = 0; i < var.domain.size(); i++){
			//Add a new "Node" in the search tree
			for (Variable v : variables) {
				v.conflicts.add(new ArrayList<Integer>());
			}
			int value = var.domain.get(i);
			var.assign(value);
//			printBoard();
			constraints = genConstraints(K, variables.indexOf(var));
			boolean success = AC3(constraints, variables);
			if(success) {
				return KQueens(K);
			}
			var.unAssign();
			for (Variable v : variables) {
				v.backTrack();
			}
		}
		return false;
	}

	public boolean run(int K){
		//Reset variables
		reset(K);
		//Main loop
		return KQueens(K);
	}
	
	/***
	 * Prints the board out nicely.
	 */
	public void printBoard() {
		for (int y = 0; y < variables.size(); y++) {
			String s = "";
			Variable var = variables.get(y);
			for (int x = 0; x < variables.size(); x++) {
				if(var.domain.size() == 1 && var.domain.get(0) == x) {
					s += "[Q]";
				}
				else {
					s += "[ ]";
				}
			}
			if(var.domain.size() == 1)
				s += String.format(" Col = %d", var.domain.get(0));
		System.out.println(s);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		CSPQueens q = new CSPQueens();
		int runs = 10;
		int K = 64;
		long sum = 0;
		for (int i = 0; i < runs; i++) {
			long timer = System.currentTimeMillis();
			boolean victory = q.run(K);
			long runtime = System.currentTimeMillis() - timer;
//			q.printBoard();
			System.out.println("Runtime: " + runtime);
			System.out.println("Success: " + victory);
			sum += runtime;
		}
		System.out.println(String.format("Average runtime for %d runs: %d ms", runs, sum/runs));
	}
}
