package framework;

import java.util.ArrayList;

//Interface needed gor smooth problem specific implementation
public interface SAStateGenerator<T extends SADataStruct> {
	public ArrayList<T> getNeighbours(T state, int n);
}
