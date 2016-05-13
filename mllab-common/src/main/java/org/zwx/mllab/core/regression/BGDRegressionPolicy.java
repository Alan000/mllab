package org.zwx.mllab.core.regression;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.mahout.math.Vector;
import org.zwx.mllab.core.Model;
import org.zwx.mllab.core.TrainSets;

public class BGDRegressionPolicy extends RegressionPolicy {
	private double alpha = 0.1;

	private double threshold = 0.001;

	@Override
	public void run(TrainSets<NamedVector<Double>> ts, RegressionModel model) {

		double preError = 0.0;
		double curError = 0.0;

		do {
			preError = curError;
			curError = 0.0;

			Vector adjust = initializeVector(ts.get(0).getValue().size());
			for (NamedVector<Double> instance : ts) {
				double yi = instance.getName();
				Vector xi = instance.getValue();
				double error = yi - model.evaluate(xi);
				curError += error;
				adjust = adjust.plus(model.getParamsGradient(instance));
			}

			model.updateParams(model.getParams().minus(adjust.divide(1.0 / alpha)));
			System.out.println(Math.abs((curError - preError) / curError));

		} while (Math.abs((curError - preError) / curError) > threshold);

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

		BGDRegressionPolicy bgd = new BGDRegressionPolicy();
		Model model = new LinearRegressionModel(initializeVector(ts.get(0).getValue().size()));
		bgd.run(ts, model);
		System.out.println(model);
	}
}
