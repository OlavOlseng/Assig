//Generic state generator, also calculates heuristics. This needs to be specific for each problem instance.
package framework;

import java.util.ArrayList;

public abstract class AsStateGenerator {
	
	/***
	 * This function must generate a unique hashcode for the given input
	 * @param state
	 * @return
	 */
	public abstract int generateHash(AsState<?> state);
	
	/***
	 * This function should calculate the heuristic of the function.
	 * @param state
	 * @return
	 */
	public abstract double calculateH(AsState<?> state, AsState<?> goalState);
	
	
	/*** This function generates all children states, with the G value set.
	 * Hashes for the states will be generated and set automatically
	 * @param state
	 * @return list of states that follow from state
	 */
	protected abstract ArrayList<AsState<?>> generateChildren(AsState<?> state);
	
	//This generates all children for a state, and returns them in a list.
	public ArrayList<AsState<?>> getChildren(AsState<?> state) {
		ArrayList<AsState<?>> res = generateChildren(state);
		
		for(AsState<?> currentState : res) {
			currentState.setHash(generateHash(currentState));
		}
		return res;
	}
}
