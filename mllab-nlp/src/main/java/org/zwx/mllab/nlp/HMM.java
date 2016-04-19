package org.zwx.mllab.nlp;

public class HMM {
	private int N;
	private int M;
	private double[][] A;
	private double[][] B;
	private double[] pi;

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public int getM() {
		return M;
	}

	public void setM(int m) {
		M = m;
	}

	public double[][] getA() {
		return A;
	}

	public void setA(double[][] a) {
		A = a;
	}

	public double[][] getB() {
		return B;
	}

	public void setB(double[][] b) {
		B = b;
	}

	public double[] getPi() {
		return pi;
	}

	public void setPi(double[] pi) {
		this.pi = pi;
	}

}
