package framework;

public interface SAObjectiveFunction<T extends SADataStruct> {
	public double calc(T state);
}
