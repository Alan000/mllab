package org.zwx.mllab.core.regression;

import java.io.BufferedReader;
import java.io.FileReader;
import org.zwx.mllab.common.vector.NamedVector;
import org.zwx.mllab.common.vector.Vector;
import org.zwx.mllab.core.Model;
import org.zwx.mllab.core.TrainSets;

public class SGDRegressionPolicy extends RegressionPolicy {

	private int loop = 1000;

	private double alpha = 0.1;

	private double threshold = 0.001;

	@Override
	public void run(TrainSets<NamedVector<Double>> ts, RegressionModel model) {
		double preError = 0.0;
		double curError = 0.0;
		for (int i = 0; i < loop; i++) {
			preError = curError;
			curError = 0.0;
			for (NamedVector<Double> instance : ts) {
				double yi = instance.getName();
				Vector xi = instance.getValue();
				curError += yi - model.evaluate(xi);
				Vector adjust = model.getParamsGradient(instance).divide(-1.0 / alpha);
				model.updateParams(model.getParams().plus(adjust));
			}
			if (Math.abs((curError - preError) / curError) < threshold)
				break;
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

		SGDRegressionPolicy sgd = new SGDRegressionPolicy();
		long time = System.currentTimeMillis();
		Model model = new LinearRegressionModel(initializeVector(ts.get(0).getValue().size()));
		sgd.run(ts, model);
		System.out.println(model);
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");
	}
}
