package org.zwx.mllab.nlp;

public class Forward {

	public static double forward(HMM phmm, int T, int[] O) {
		int i, j;
		int t;
		double sum;
		double[][] alpha = new double[T][phmm.getN()];

		for (i = 0; i < phmm.getN(); i++) {
			alpha[0][i] = phmm.getPi()[i] * phmm.getB()[i][O[0]];
		}

		for (t = 0; t < T; t++) {
			for (j = 0; j < phmm.getN(); j++) {
				sum = 0.0;
				for (i = 0; i < phmm.getN(); i++)
					sum += alpha[t][i] * phmm.getA()[i][j];
				alpha[t][j] = sum * phmm.getB()[j][O[t]];
			}
		}
		double pprob = 0.0;
		for (i = 0; i < phmm.getN(); i++)
			pprob += alpha[T][i];
		return pprob;
	}

	public static void main(String[] args) {
		HMM phmm = new HMM();
		phmm.setM(4);
		phmm.setN(3);
		double[][] A = new double[][] { { 0.500, 0.375, 0.125 }, { 0.250, 0.125, 0.625 }, { 0.250, 0.375, 0.375 } };
		phmm.setA(A);
		double[][] B = new double[][] { { 0.60, 0.20, 0.15, 0.05 }, { 0.25, 0.25, 0.25, 0.25 }, { 0.05, 0.10, 0.35, 0.50 } };
		phmm.setB(B);
		phmm.setPi(new double[] { 0.63, 0.17, 0.20 });

		System.out.println(forward(phmm, 3, new int[] { 1, 3, 4 }));
	}
}
