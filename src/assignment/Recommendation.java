package assignment;

import java.util.ArrayList;
import java.util.Random;

public class Recommendation {


	static Random random = new Random();

	static double[][] M = { { 52, 0, 9, 8, 7 }, { 25, 0, 52, 88, 18 },
			{ 9, 28, 27, 0, 2 }, { 6, 45, 0, 6, 6 }, { 0, 0, 0, 0, 0 } };

	static double U[][];
	static double V[][];

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

	public static boolean isMatrix(double[][] A) {
		boolean isMatrix = true;
		if (A != null && A.length > 0 && A[0] != null) {
			int l1 = A[0].length;
			for (int i = 0; i < A.length; i++) {
				if (A[i] == null || A[i].length != l1)
					isMatrix = false;
			}
		} else
			isMatrix = false;
		return isMatrix;
	}

	public static double[][] multiplyMatrix(double[][] A, double[][] B) {
		if ((isMatrix(A) && isMatrix(A)) && (A[0].length == B.length)) {
			double[][] prod = new double[A.length][B[0].length];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < B[0].length; j++) {
					for (int k = 0; k < B.length; k++) {
						prod[i][j] += A[i][k] * B[k][j];
					}
				}
			}
			return prod;
		} else
			return null;
	}

	public static double[][] createMatrix(int n, int m, int k, int l) {
		int tmp = random.nextInt(5);
		double[][] matrix = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				Random random = new Random();
				matrix[i][j] = (l - k) * random.nextDouble() + k;
			}
		}
		return matrix;
	}

	public static double rmse(double[][] M, double[][] P) {
		double rmse = 0;
		int nbTerms = 0;
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] != 0) {
					rmse += Math.pow((M[i][j] - P[i][j]), 2);
					nbTerms++;
				}
			}
		}
		rmse = Math.sqrt(rmse / nbTerms);
		return rmse;
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

	public static double updateVElem(double[][] M, double[][] U, double[][] V,
			int r, int s) {
		double sigmaDenom = 0;
		double sigmaNom = 0;
		int n = M.length;
		for (int i = 0; i < n; i++) {
			if (M[i][s] != 0) {
				sigmaDenom += Math.pow(U[i][r], 2);
				double tmp = 0;
				for (int k = 0; k < V.length; k++) {
					if (k != r)
						tmp += U[i][k] * V[k][s];
				}
				sigmaNom += U[i][r] * (M[i][s] - tmp);
			}
		}

		if (sigmaDenom == 0)
			return V[r][s];
		else
			return sigmaNom / sigmaDenom;
	}

	public static double[][] optimizeU(double[][] M, double[][] U, double[][] V) {
		/*for(int i = 0 ;i < U.length; i++){
			for(int j = 0; j < U[0].length; j++){
				U[i][j] = updateUElem(M, U, V, i, j);
			}
		}
		*/
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

	public static double[][] optimizeV(double[][] M, double[][] U, double[][] V) {

		/*for(int i = 0 ;i < V.length; i++){
			for(int j = 0; j < V[0].length; j++){
				V[i][j] = updateVElem(M, U, V, i, j);
			}
		}
		*/
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < (V[0].length * V.length); i++)
			indexes.add(new Integer(i));
		Random rd = new Random();
		while (indexes.size() > 0) {
			int index = rd.nextInt(indexes.size());
			int ij = indexes.get(index).intValue();
			int i = (int) Math.floor((double) ij / (double) V[0].length);
			int j = ij % V[0].length;
			V[i][j] = updateVElem(M, U, V, i, j);
			indexes.remove(index);
		}
		
		return V;
	}

	public static double computeInitVal(double[][] M, int d) {
		double val = 0, sum = 0, numEl = 0;
		Random random = new Random();
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] != 0) {
					sum += M[i][j];
					numEl++;
				}
			}
		}
		if (numEl == 0)
			return 1;
		else {
			val = Math.sqrt(sum / numEl / d); //+ (d) * random.nextDouble();
			return val;
		}
	}

	public static void InitUV(double[][] M, int d) {
		U = new double[M.length][d];
		V = new double[d][M[0].length];
		double initVal = computeInitVal(M, d);
		for (int i = 0; i < U.length; i++) {
			for (int j = 0; j < U[0].length; j++) {
				U[i][j] = initVal;
			}
		}
		for (int i = 0; i < V.length; i++) {
			for (int j = 0; j < V[0].length; j++) {
				V[i][j] = initVal;
			}
		}

	}

	public static int[] recommend(double[][] M, int d) {
		int[] users = null;
		if (isMatrix(M)) {
			// Initialiser U et V
			InitUV(M, d);

			// Optimize UV
			double[][] P = multiplyMatrix(U, V);
			double rmse = rmse(M, P);
			double delta = 1;
			System.out.println("Debut ");
			while (delta > 10e-6) {
				U = optimizeU(M, U, V);
				V = optimizeV(M, U, V);
				P = multiplyMatrix(U, V);
				double temp = rmse(M, P);
				delta = rmse - temp;
				rmse = temp;
			}

			System.out.println("Rmse: " + rmse);
			// Return the list
			users = new int[M.length];
			for (int i = 0; i < P.length; i++) {
				int indRecommend = -1;
				double scorRecommend = 0;
				for (int j = 0; j < P[i].length; j++) {
					if (M[i][j] == 0 && P[i][j] > scorRecommend) {
						scorRecommend = P[i][j];
						indRecommend = j;
					}
				}
				users[i] = indRecommend;
			}

		}
		return users;
	}

	public static void main(String[] args) {
		double[][] M = Netflix
				.readData("src/assignment/utility_user300_movies500.m");
		long time = System.currentTimeMillis();
		int[] m = recommend(M, 20);
		System.out.println((System.currentTimeMillis()-time)/1000);
		for (int i = 0; i < m.length; i++) {
			System.out.println(i + ": " + m[i]);
		}
	}

}
