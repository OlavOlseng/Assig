package eggPuzzle;

import framework.SADataStruct;

//#############################################################
//Standard one-dimensional array implementation of a board.
//Uses helper methods for easy indexing.
//Flip method for easy flipping between one and zero.
//#############################################################

public class EggStruct implements SADataStruct{
	
	int width, height;
	int[] field;
	public final int K;
	
	/***
	 * @param M - Height of board
	 * @param N - width of board
	 * @param K - max number of 1's per row/column/diagonal
	 */
	public EggStruct(int M, int N, int K) {
		this.height = N;
		this.width = M;
		this.K = K;
		field = new int[M*N];
	}
	
	/***
	 * Clones the supplied board
	 * @param toBeCloned
	 */
	public EggStruct (EggStruct toBeCloned) {
		this.K = toBeCloned.K;
		clone(toBeCloned);
	}
	
	private void clone(EggStruct other) {
		this.field = other.field.clone();
		this.width = other.width;
		this.height = other.height;
	}
	
	public void put(int val, int x, int y) {
		field[width*y + x] = val;
	}
	
	public int get(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) return 0;
		return field[width*y + x];
	}
	
	/***
	 * Flips the given index on the board. No validity checks are performed.
	 * @param i
	 */
	public void flip(int i) {
		if (field[i] > 0) {
			field[i] = 0;
		}
		else {
			field[i] = 1;
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int y = 0; y < height; y++) {
			s += "[ ";
			for (int x = 0; x < width - 1; x++) {
				s += get(x, y) + ", ";
			}
			s+= get(width - 1, y) + "]\n";
		}
		return s;
	}
}
