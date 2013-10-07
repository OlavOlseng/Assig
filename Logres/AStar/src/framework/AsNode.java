package framework;

import java.util.ArrayList;
import java.util.HashMap;

public class AsNode implements Comparable<AsNode>{
	
	protected ArrayList<AsNode> children;
	protected HashMap<AsNode, Double> childCost;
	protected AsNode parent;
	protected AsState<?> state;
	public boolean OPEN = false;
	
	public AsNode(AsState<?> state) {
		children = new ArrayList<AsNode>();
		childCost = new HashMap<AsNode, Double>();
		parent = null;
		this.state = state;
	}
	
	public double getF() {
		return state.getF();
	}

	//Sets arguement to be parent, and calculates new G.	
	public void setParent(AsNode parent) {
		this.state.setG(parent.getState().getG() + parent.childCost.get(this));
		this.parent = parent;
	}

	
	public AsNode getParent() {
		return this.parent;
	}
	
	//Adds children to both child list, and its distances to the distance hashmap.
	public void addChild(AsNode child, double cost) {
		if(!children.contains(child)) {
			children.add(child);
			childCost.put(child, cost);
		}
	}
	
	// returns distance to the child
	public double getArcCost(AsNode child) {
		return this.state.getG() + childCost.get(child);
	}
	
	public int getHash() {
		return this.state.getHash();
	}
	
	public AsState getState() {
		return this.state;
	}
	//Simple function comparing the states of two nodes
	public boolean equals(AsNode other) {
		return this.state.getHash() == other.state.getHash();
	}

	public ArrayList<AsNode> getChildren() {
		return this.children;
	}
	
	//This allows the collections class to sort collections of AsNodes
	@Override
	public int compareTo(AsNode other) {
		if(this.state.getF() < other.state.getF()) return -1;
		if(this.state.getF() > other.state.getF()) return 1;
		return 0;
	}
}
