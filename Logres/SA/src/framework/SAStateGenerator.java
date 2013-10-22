package framework;

import java.util.ArrayList;

public interface SAStateGenerator<T extends SADataStruct> {
	public ArrayList<T> getNeighbours(T state, int n);
}
