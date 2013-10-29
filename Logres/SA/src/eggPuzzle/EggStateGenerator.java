package eggPuzzle;

import java.util.ArrayList;

import framework.SAStateGenerator;

//#####################################################
//Super simple cheap state generation. 
//Just flips one random slot for every neighbour.
//Returns a list of n random neighbours.
//Every neighbour might even be identical.
//#####################################################


public class EggStateGenerator implements SAStateGenerator<EggStruct>{

	@Override
	public ArrayList<EggStruct> getNeighbours(EggStruct state, int n) {
		ArrayList<EggStruct> neighbours = new ArrayList<EggStruct>();
		for (int i = 0; i < n; i++) {
			EggStruct s = new EggStruct(state);
			s.flip((int)(Math.random()*s.field.length));
			neighbours.add(s);
		}
		return neighbours;
	}
}
