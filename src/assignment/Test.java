package assignment;

import java.util.ArrayList;
import java.util.Random;

public class Test {

	static double[][] M = { { 11.0, 0.0, 9.0, 8.0, 7 }, { 18.0, 0.0, 18.0, 18.0, 18 },
		{ 29.0, 28.0, 27.0, 0.0, 25 }, { 6.0, 6.0, 0.0, 6.0, 6 }, { 17.0, 16.0, 15.0, 14.0, 0 } };
	
	
	
	static double[][] U = { { 2.0, 2.0 }, { 2.0, 2.0 },{ 2.0, 2.0 },{ 2.0, 2.0 },{ 2.0, 2.0 }};
	
	
	
	static double[][] V = { { 1.0, 1.0, 1.0, 1.0, 1.7 },{ 3.33, 1.0, 1.0, 1.0, 1.0 } };

	public static void main(String[] args) {
		System.out.println(matrixToString(optimizeU(M, U, V)));

	}
	
	public static double updateUElem(double[][] M, double[][] U, double[][] V,
			int r, int s) {
		double Urs = 0;
		double quotient = 0;
		for (int j = 0; j < M[0].length; j++) {
			if (M[r][j] != 0) {
				double sigma = 0;
				quotient += Math.pow(V[s][j], 2);
				for (int k = 0; k < U[0].length; k++) {
					if (k != s)
						sigma += (U[r][k] * V[k][j]);
				}

				Urs += (V[s][j] * (M[r][j] - sigma));
			}
		}
		if (quotient != 0)
			return (Urs / quotient);
		else
			return U[r][s];
	}
	
	public static double[][] optimizeU(double[][] M, double[][] U, double[][] V) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < (U[0].length * U.length); i++)
			indexes.add(new Integer(i));
		Random random = new Random();
		while (indexes.size() > 0) {
			int i = random.nextInt(indexes.size());
			int r = indexes.get(i).intValue();
			int l = (int) Math.floor((double) r / (double) U[0].length);
			int c = r % U[0].length;
			U[l][c] = updateUElem(M, U, V, l, c);
			indexes.remove(i);
		}
		return U;
	}
	
	public static String matrixToString(double[][] A) {
		String s = "{\n";
		for (int i = 0; i < A.length; i++) {
			s += "{";
			for (int j = 0; j < A[i].length; j++) {
				if (j == 0)
					s += A[i][j];
				else
					s += ", " + A[i][j];
			}
			s += "},\n";
		}
		s += "}";
		return s;
	}
}
