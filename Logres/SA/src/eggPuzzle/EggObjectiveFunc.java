package eggPuzzle;

import framework.SAObjectiveFunction;

public class EggObjectiveFunc implements SAObjectiveFunction<EggStruct>{
	
	private double weight = 1.0;
	private double penalty = 0.5;
	
	@Override
	public double calc(EggStruct state) {
		double val = 0;
		int width = state.width;
		int height = state.height;
		int K = state.K;
		int diagonals = (height + width - 3)*2;
		val += checkRows(state, width, height, K)*weight;
		val += checkColumns(state, width, height, K)*weight;
		val += checkDiagonals(state, width, height, K)*weight;
		val /= (width + height + diagonals);
		return val;
	}

	private double checkRows(EggStruct state, int width, int height, int K) {
		double val = 0;
		for (int y = 0; y < height; y++) {
			int rowSum = 0;
			for (int x = 0; x < width; x++) {
				rowSum += state.get(x, y);
			}
			if(rowSum <= K) {
				val += (double)rowSum/K;
			}
			else{
//				val += (double)(K - penalty*rowSum)/K;
				return 0;
			}
		}
		return val;
	}

	private double checkColumns(EggStruct state, int width, int height, int K) {
		double val = 0;
		for (int x = 0; x < width; x++) {
			int colSum = 0;
			for (int y = 0; y < width; y++) {
				colSum += state.get(x, y);
			}
			if(colSum <= K) {
				val += (double)colSum/K;
			}
			else{
//				val += (double)(K - penalty*colSum)/K;
				return 0;
			}
		}
		return val;
	}

	private double checkDiagonals(EggStruct state, int width, int height, int K) {
		double val = 0;
		
		//main diagonals
		double diagSum1 = 0;
		double diagSum2 = 0;
		for (int i = 0; i < width && i < height; i++){
			diagSum1 += state.get(i, i);
			diagSum2 += state.get(i, height - 1 - i);
		}
		if(diagSum1 <= K) {
			val += (double)diagSum1/K;
		}
		else{
//			val += (double)(K - penalty*diagSum1)/K;
			return 0;
		}
		if(diagSum2 <= K) {
			val += (double)diagSum2/K;
		}
		else{
//			val += (double)(K - penalty*diagSum2)/K;
			return 0;
		}
		
		//Check all other diagonals
		for (int i = 1; i < width - 1; i++) {
			diagSum1 = 0;
			diagSum2 = 0;
			double diagSum3 = 0;
			double diagSum4 = 0;

			//TODO : Optimize
			for (int j = 0; j < width; j++) {
				diagSum1 += state.get(i + j, j);
				diagSum2 += state.get(i - j, j);
				diagSum3 += state.get(i + j, height - 1 - j);
				diagSum4 += state.get(i - j, height - 1 - j);
			}
			
			if(diagSum1 <= K) {
				val += (double)diagSum1/K;
			}
			else{
//				val += (double)(K - penalty*diagSum1)/K;
				return 0;
			}
			
			if(diagSum2 <= K) {
				val += (double)diagSum2/K;
			}
			
			else{
//				val += (double)(K - penalty*diagSum2)/K;
				return 0;
			}
			
			if(diagSum3 <= K) {
				val += (double)diagSum3/K;
			}
			
			else{
//				val += (double)(K - penalty*diagSum3)/K;
				return 0;
			}
			
			if(diagSum4 <= K) {
				val += (double)diagSum4/K;
			}
			else{
//				val += (double)(K - penalty*diagSum4)/K;
				return 0;
			}
		}
		return val;
	}

//	public static void main(String[] args) {
//		EggObjectiveFunc f = new EggObjectiveFunc();
//		EggStruct s = new EggStruct(5, 5, 2);
//		s.put(1, 0, 0);
//		s.put(1, 2, 0);
//		s.put(1, 1, 1);
//		s.put(1, 4, 1);
//		s.put(1, 1, 2);
//		s.put(1, 3, 2);
//		s.put(1, 0, 3);
//		s.put(1, 4, 3);
//		s.put(1, 2, 4);
//		s.put(1, 3, 4);
//		System.out.println(s);
//		System.out.println(f.calc(s));
//	}
}
