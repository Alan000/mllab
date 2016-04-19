package org.zwx.mllab.core.regression;

import java.io.BufferedReader;
import java.io.FileReader;

import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.lang.TypeMatchException;
import org.zwx.mllab.vector.NamedVector;
import org.zwx.mllab.vector.Vector;
import org.zwx.mllab.vector.Vector.Element;

import Jama.Matrix;

public class LinearRegressionPolicy extends RegressionPolicy {

	@Override
	public void run(final TrainSets<NamedVector<Double>> ts, RegressionModel model) throws Exception {
		if (model instanceof LinearRegressionModel) {

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
			Matrix theta = (X.transpose().times(X)).inverse().times(X.transpose()).times(Y);
			model.getParams().setAll(theta.getColumnVector(0));

		} else {
			throw new TypeMatchException();
		}

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

		LinearRegressionPolicy lrp = new LinearRegressionPolicy();
		long time = System.currentTimeMillis();
		RegressionModel model = new LinearRegressionModel(initializeVector(ts.get(0).getValue().size()));
		lrp.run(ts, model);
		System.out.println(model);
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");
		for (NamedVector<Double> nv : ts) {
			System.out.println(model.evaluate(nv.getValue()) + ":" + nv.getName());
		}
	}

}
