package cspQueens;

import java.util.ArrayList;

public class Variable implements Comparable<Variable>{
	public int value;
	public ArrayList<Integer> domain;
	public ArrayList<ArrayList<Integer>> conflicts;
	
	public Variable(ArrayList<Integer> globalDomain) {
		value = -1;
		domain = new ArrayList<Integer>(globalDomain);
		conflicts = new ArrayList<ArrayList<Integer>>();
	}
	
	/***
	 * This function takes assigns value to the variable, and removes all other values from the domain and into to top element of the conflict set.
	 * @param value
	 */
	public void assign(int value) {
		this.value = value;
		
		for(int i = 0; i < domain.size(); i++) {
			if (domain.get(i) == value) {continue;}
			conflicts.get(0).add(domain.get(i));
		}
		
		for (int val : conflicts.get(0)) {
			domain.remove((Integer)val);
		}
	}
	
	public void unAssign() {
		this.value = -1;
	}
	
	public void backTrack() {
		for(int val : conflicts.remove(0)) {
			domain.add(val);
		}
	}
	
	public void moveToConflictSet(int value) {
		domain.remove((Integer)value);
		conflicts.get(0).add(value);
	}
	
	@Override
	public int compareTo(Variable o) {
		// TODO Auto-generated method stub
		return this.domain.size() - o.domain.size();
	}
}
