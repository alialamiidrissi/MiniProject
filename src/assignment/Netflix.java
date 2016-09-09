package assignment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Netflix {

	public static void main(String[] args) {
		//write data - in case you want to store your own matrices
		double [][] R  = {{1,2,3},{3,4,5},{5,6,7},{7,8,9}};
		Netflix.writeData(R, "test.m");

		//read data - 
		double [][] M;
//		M = Netflix.readData("test.m");
		M = Netflix.readData("utility_user5_movies10.m");		
		Netflix.printData(M);
	}
	
	public static void writeData(double[][] utilityMatrix, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(utilityMatrix);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double[][] readData(String filename) {
		double[][] M = null;
		try {
			FileInputStream fos = new FileInputStream(filename);
			ObjectInputStream oos = new ObjectInputStream(fos);
			M = (double[][]) oos.readObject();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return M;
	}

	public static void printData(double[][] M) {
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[i].length; j++) {
				System.out.print(M[i][j] + "\t");
			}
			System.out.println();
		}
	}
}
