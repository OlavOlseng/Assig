package framework;

import java.util.ArrayList;

public class SA<T extends SADataStruct> {
	
	SAObjectiveFunction<T> f; 
	SAStateGenerator<T> generator;
	
	public boolean satisfied; //Tells if the algorithm terminated on Ftarget
	public int iterations; //Counts iterations 
	
	public SA(SAObjectiveFunction<T> f, SAStateGenerator<T> generator) {
		this.f = f;
		this.generator = generator;
	}
	

	/***
	 * 
	 * @param P - initial state
	 * @param Ftarget - target F
	 * @param Tmax - starting T
	 * @param dT - change in T per iteration
	 * @param n - number of neighbours generated per expansion
	 * @param iterations - number of iterations to be run, before force-terminating
	 * @return Solution state, the satisfied boolean will be set to true is search succeeded.
	 */
	public SADataStruct run(T P, double Ftarget, double Tmax, double dT, int n) {
		satisfied = false;
		double Tcurrent = Tmax;
		iterations = 0;
		
		//Loop begins here, I use the supplied algorithm form the paper
		while(Tcurrent > 0) {
			double Fcurrent = f.calc(P);
			if(Fcurrent >= Ftarget) {
				satisfied = true;
				break;
			}
			
			//Neighbour generation
			ArrayList<T> neighbours = new ArrayList<T>(); 
			neighbours = generator.getNeighbours(P, n);
			
			double Fmax = f.calc(neighbours.get(0));
			T Pmax = neighbours.get(0);
			
			//Find best neighbour
			for(int i = 0; i < neighbours.size(); i++) {
				double Ftemp = f.calc(neighbours.get(i)); 
				if(Ftemp > Fmax) {
					Fmax = Ftemp;
					Pmax = neighbours.get(i);
				}
			}
				
			double delta = Fmax - Fcurrent;
			double q = (delta)/Fcurrent;
			double temp = Math.pow(Math.E, -q / Tcurrent);
			double p = Math.min(1.0d, temp);
			double x = Math.random();
			
			iterations++;
			Tcurrent -= dT/(double)(iterations * 1.25); // Dividing by iterations will make a curve that converges to zero
			
//			System.out.println("Tcurrent: " + Tcurrent);
//			System.out.println("Fcurrent: " + Fcurrent);
//			System.out.println("Fmax: "+Fmax);
//			System.out.println("P :" + p);
//			System.out.println("X: " + x + " P: " + p);
			
			
			///If best new solution was better, discard it with probability p, else, pick a random one. 
			if(x > p) {
				P = Pmax;
			}
			else {
				int i = (int)Math.random()*neighbours.size();
				P = neighbours.get(i);
			}
		}
		return P;
	}
}
