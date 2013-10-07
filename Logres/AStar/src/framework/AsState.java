package framework;

//Just a simple class for saving states. Uses generics to make casting of stateData easier.
public class AsState<E> {
	
	private double F, G, H;
	private int hash;
	protected E stateData;
	
	public AsState(E stateData) {
		G = 0;
		this.stateData = stateData;
	}
	
	public void setG(double val) {
		this.G = val;
		calculateF();
	}
	
	public void setH(double val) {
		this.H = val;
		calculateF();
	}

	public void setHash(int hash) {
		this.hash = hash;
	}
	
	public double getG() {
		return this.G;
	}
	
	public double getF() {
		return this.F;
	}
	
	public double getH() {
		return this.H;
	}
	
	public int getHash() {
		return this.hash;
	}
	
	private void calculateF() {
		this.F = G + H;
	}
	
	public E getStateData() {
		return this.stateData;
	}
}
