package framework;

//Interface needed for smooth implementation of specific problems
public interface SAObjectiveFunction<T extends SADataStruct> {
	public double calc(T state);
}
