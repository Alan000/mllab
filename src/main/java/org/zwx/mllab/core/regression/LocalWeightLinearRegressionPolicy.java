package org.zwx.mllab.core.regression;

import java.io.BufferedReader;
import java.io.FileReader;

import org.zwx.mllab.common.vector.DenseVector;
import org.zwx.mllab.common.vector.NamedVector;
import org.zwx.mllab.common.vector.Vector;
import org.zwx.mllab.common.vector.Vector.Element;
import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.lang.TypeMatchException;

import Jama.Matrix;

public class LocalWeightLinearRegressionPolicy extends RegressionPolicy {

	private double t;

	TrainSets<NamedVector<Double>> ts;

	public LocalWeightLinearRegressionPolicy(double t) {
		this.t = t;
	}

	@Override
	public void run(TrainSets<NamedVector<Double>> ts, RegressionModel model) throws Exception {
		if (model instanceof LocalWeightLinearRegressionModel) {
			this.ts = ts;
			((LocalWeightLinearRegressionModel) model).policy = this;
		} else {
			throw new TypeMatchException();
		}

	}

	public Vector getParams(Vector x) {
		Matrix w = getW(x);

		Matrix X = new Matrix(ts.size(), ts.get(0).getValue().size());
		Matrix Y = new Matrix(ts.size(), 1);

		for (int row = 0; row < ts.size(); row++) {
			double yi = ts.get(row).getName();
			Vector xi = ts.get(row).getValue();
			Y.set(row, 0, yi);
			for (Element e : xi.nonZeroes()) {
				X.set(row, e.index(), e.get());
			}
		}
		Matrix theta = (X.transpose().times(w).times(X)).inverse().times(X.transpose()).times(w).times(Y);
		Vector params = new DenseVector(x.size());
		params.setAll(theta.getColumnVector(0));
		return params;
	}

	private Matrix getW(Vector x) {
		Matrix w = new Matrix(ts.size(), ts.size());
		for (int i = 0; i < ts.size(); i++) {
			double wi = Math.exp(-ts.get(i).getValue().minus(x).lengthSquare() / (2 * t * t));
			w.set(i, i, wi);
		}
		return w;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(args[0]));
		TrainSets<NamedVector<Double>> ts = new TrainSets<NamedVector<Double>>();
		String line = null;
		int size = Integer.valueOf(in.readLine()) + 1;
		while ((line = in.readLine()) != null) {
			if (line.length() < 3)
				continue;
			String[] kv = line.split("\t");
			double name = Double.valueOf(kv[0]);
			Vector v = initializeVector(size);
			v.set(0, 1.0);
			String[] splits = kv[1].split(" ");
			for (String s : splits) {
				String[] iv = s.split(":");
				v.set(Integer.valueOf(iv[0]), Double.valueOf(iv[1]));
			}
			ts.add(new NamedVector<Double>(name, v));
		}
		in.close();

		LocalWeightLinearRegressionPolicy lwr = new LocalWeightLinearRegressionPolicy(1.0);
		long time = System.currentTimeMillis();
		RegressionModel model = new LocalWeightLinearRegressionModel();
		lwr.run(ts, model);

		Vector test = initializeVector(size);
		test.set(0, 1.0);
		test.set(1, 5);
		System.out.println(model.evaluate(test));
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");
	}
}
