package org.zwx.mllab.core.regression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.vector.NamedVector;
import org.zwx.mllab.vector.Vector;

public class SGDRegressionPolicy extends RegressionPolicy {

	private int loop = 200;

	private double alpha = 0.7;

	private double threshold = 0.01;

	@Override
	public void run(TrainSets<NamedVector<Double>> ts, RegressionModel model) {
		if (model instanceof LogisticRegressionModel) {
			normalize(ts);
		}

		ts = disorder(ts);

		double preError = 0.0;
		double curError = 0.0;
		for (int i = 0; i < loop; i++) {
			preError = curError;
			curError = 0.0;
			for (NamedVector<Double> instance : ts) {
				double yi = instance.getName();
				Vector xi = instance.getValue();
				double mis = Math.abs(yi - model.evaluate(xi));
				if (instance instanceof NamedWeightVector) {
					mis *= ((NamedWeightVector<?>) instance).getWeight();
				}
				curError += mis;
				Vector adjust = model.getParamsGradient(instance).times(-alpha);
				if (instance instanceof NamedWeightVector)
					model.updateParams(
							model.getParams().plus(adjust.times(((NamedWeightVector<Double>) instance).getWeight())));
				else
					model.updateParams(model.getParams().plus(adjust));
			}
			double mis = Math.abs((curError - preError) / curError);
			System.out.println("Loop" + i + ":\t" + curError + "\t|\t" + preError + "\t|\t" + mis);
			if (mis < threshold) {
				System.out.println("loop:" + i);
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private TrainSets<NamedVector<Double>> disorder(TrainSets<NamedVector<Double>> ts) {
		TrainSets<NamedVector<Double>> result = new TrainSets<NamedVector<Double>>();
		Random random = new Random(System.currentTimeMillis());
		TrainSets<NamedVector<Double>> old = (TrainSets<NamedVector<Double>>) ts.clone();
		while (!old.isEmpty()) {
			int r = random.nextInt(old.size());
			result.add(old.remove(r));
		}
		return result;
	}

	private void normalize(TrainSets<NamedVector<Double>> ts) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (NamedVector<Double> nv : ts) {
			if (nv.getName() > max)
				max = nv.getName();
			if (nv.getName() < min)
				min = nv.getName();
		}

		double dis = max - min;

		for (NamedVector<Double> nv : ts) {
			nv.setName((nv.getName() - min) / dis);
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
		RegressionModel model = new LogisticRegressionModel(initializeVector(ts.get(0).getValue().size()));
		sgd.run(ts, model);
		System.out.println(model);
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");
		for (NamedVector<Double> nv : ts) {
			System.out.println(model.evaluate(nv.getValue()) + ":" + nv.getName());
		}

	}
}
