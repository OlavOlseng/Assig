package eggPuzzle;

import framework.SA;

public class EggTest {

	public static boolean debug = false;
	//###############################################
	//These variables run with the debug function.
	public static double Ftarget = 0.73;
	public static double Tmax = 0.15;
	public static int n = 500;
	public static double dT = 0.02;
	public static int size = 6;
	public static int K = 2;
	//################################################
	
	public static void debug() {
		EggObjectiveFunc func = new EggObjectiveFunc();
		EggStateGenerator sg = new EggStateGenerator();
		SA<EggStruct> sa = new SA<EggStruct>(func, sg);
		
		EggStruct state = new EggStruct(size, size, K);
		state = (EggStruct) sa.run(state, Ftarget, Tmax, dT, n);
		System.out.println(state);
		System.out.println(sa.satisfied);
		System.out.println(func.calc(state));
	}
	
	public static void run() {
		EggObjectiveFunc func = new EggObjectiveFunc();
		EggStateGenerator sg = new EggStateGenerator();
		SA<EggStruct> sa = new SA<EggStruct>(func, sg);
		
		EggStruct state = new EggStruct(5, 5, 2);
		state = (EggStruct) sa.run(state, 0.76, 0.15, 0.02, 50);
		System.out.println("Iterations: " + sa.iterations);
		System.out.println("Finished: " + sa.satisfied);
		System.out.println("Final F: " + func.calc(state));
		System.out.println(state);
		
		state = new EggStruct(6, 6, 2);
		state = (EggStruct) sa.run(state, 0.76, 0.15, 0.02, 80);
		System.out.println("Iterations: " + sa.iterations);
		System.out.println("Finished: " + sa.satisfied);
		System.out.println("Final F: " + func.calc(state));
		System.out.println(state);
		
		state = new EggStruct(8, 8, 1);
		state = (EggStruct) sa.run(state, 0.70, 0.15, 0.02, 500);
		System.out.println("Iterations: " + sa.iterations);
		System.out.println("Finished: " + sa.satisfied);
		System.out.println("Final F: " + func.calc(state));
		System.out.println(state);
		
		state = new EggStruct(10, 10, 3);
		state = (EggStruct) sa.run(state, 0.72, 0.15, 0.02, 200);
		System.out.println("Iterations: " + sa.iterations);
		System.out.println("Finished: " + sa.satisfied);
		System.out.println("Final F: " + func.calc(state));
		System.out.println(state);
	}
	
	public static void main(String[] args) {
		if (debug) {
			debug();
		}
		else {
			run();
		}
	}
}
