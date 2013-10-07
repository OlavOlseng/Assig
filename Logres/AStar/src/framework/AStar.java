package framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AStar {

	private ArrayList<AsNode> OPEN;
	private HashMap<Integer, AsNode> states;
	private AsStateGenerator generator;
	public AsNode goalNode;
	public int expandedNodes = 0;

	public AStar(AsStateGenerator generator, AsState initialState, AsState goalState) {

		//	Initialize helper classes.
		this.generator = generator;
		OPEN = new ArrayList<AsNode>();
		states = new HashMap<Integer, AsNode>();
		
		//	Ensure goalstate is properly generated.
		goalState.setHash(generator.generateHash(goalState));
		goalState.setH(this.generator.calculateH(goalState, goalState));
		this.goalNode = new AsNode(goalState);
		
		//	Handle and prepare root node
		initialState.setHash(generator.generateHash(initialState));
		initialState.setG(0);
		initialState.setH(generator.calculateH(initialState, goalState));
		AsNode root = new AsNode(initialState);
		states.put(root.getHash(), root);
		OPEN.add(root);
	}
	
	/***
	 * This function runs the A* algorithm, for the entered parameters.
	 * @return boolean, sucess state of the algorithm.
	 */
	public boolean run() {
		boolean success = false;
		AsNode currentNode;
		//This is the agenda loop
		do {
			currentNode = OPEN.remove(0);
			if (currentNode.equals(goalNode)){
				goalNode = currentNode;
				success = true; 
				break;
			}
			
			ArrayList<AsState<?>> children = generator.getChildren(currentNode.getState());
			currentNode.OPEN = false;
			expandedNodes++;
			for (AsState<?> child : children) {
				
				//Check if child state already exists, and set/treat it accordingly.
				AsNode childNode;
				if(states.containsKey(child.getHash())) {
					childNode = states.get(child.getHash());
				}
				else {
					childNode = new AsNode(child);
				}
				
				//Calculate distance from parent to generated child state, and add it to it's children
				currentNode.addChild(childNode, child.getG() - currentNode.getState().getG());
				
				//If a new node was found, evaluate and add it to OPEN using binary insertion. This ensures the OPEN is sorted in O(log(n)) time. 
				if(!states.containsKey(child.getHash())) {
					attachAndEval(currentNode, childNode);
					states.put(childNode.getHash(), childNode);
					OPEN.add(binarySearch(OPEN, 0, OPEN.size(), childNode.getF()), childNode);
				}
				//If node was already made, check to see if it has a new optimal path to it, and propagate if it's children were already handled.
				else if (childNode.getState().getG() > currentNode.getState().getG() + currentNode.getArcCost(childNode)) {
					System.out.println("Updated node");
					attachAndEval(currentNode, childNode);
					if (!childNode.OPEN) {
						System.out.println("Propagatin");
						propagate(childNode);
					}
					else if(childNode.OPEN) {
						System.out.println("Reputting child");
						OPEN.remove(childNode);
						OPEN.add(binarySearch(OPEN, 0, OPEN.size(), childNode.getF()), childNode);
					}
				}
			}
		} while(OPEN.size() > 0);
		System.out.println(success);
		return success;
	}
	
	private void attachAndEval(AsNode parent, AsNode child) {
		child.setParent(parent);
		child.getState().setH(generator.calculateH(child.getState(), goalNode.getState()));
	}
	
	private void propagate(AsNode parent) {
		double G = parent.state.getG();
		for (AsNode child : parent.getChildren()) {
			double toChild = parent.childCost.get(child);
			double childG = child.state.getG();
			
			if (childG > G + toChild) {
				child.setParent(parent);
				child.state.setG(G + toChild);
				if(child.OPEN) {
					OPEN.remove(child);
					OPEN.add(binarySearch(OPEN, 0, OPEN.size(), child.getF()), child);
				}
				propagate(child);
			}
		}
	}
	
	/***
	 * You must run the run() function before calling this this function, else it returns and empty goal state.
	 * @return Goal node.
	 */
	public AsNode getSolution() {
		return goalNode;
	}
	
	
	//This function is used for binary search in an ArrayList.
	private static int binarySearch(ArrayList<AsNode> a, int start, int end, double val){
		   
	    if(start == end){
	    	return start;
	    }
	    int middle = (start + end)/2;
	    if(val <= a.get(middle).getF()){
	        return binarySearch(a, start, middle, val);
	    }
	    else{
	        return binarySearch(a, middle + 1, end, val); 
	    }
	}
}

