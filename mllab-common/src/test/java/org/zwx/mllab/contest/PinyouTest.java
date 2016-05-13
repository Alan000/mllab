package org.zwx.mllab.contest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.core.regression.LogisticRegressionModel;
import org.zwx.mllab.core.regression.NamedVector;
import org.zwx.mllab.core.regression.RegressionModel;
import org.zwx.mllab.core.regression.SGDRegressionPolicy;
import org.zwx.mllab.util.Tuple2;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class PinyouTest {

	private static final int dimension = 600000;

	private static TrainSets<NamedVector<Double>> loadInstances(String path) throws IOException {
		TrainSets<NamedVector<Double>> result = new TrainSets<NamedVector<Double>>();
		BufferedReader read = new BufferedReader(new FileReader(path));
		String line = read.readLine();
		while ((line = read.readLine()) != null) {
			String[] values = line.split(" ");
			Vector vector = new SequentialAccessSparseVector(dimension);
			double target = Double.valueOf(values[0]);
			for (int i = 2; i < values.length; i++) {
				String[] split = values[i].split(":");
				int index = Integer.valueOf(split[0]);
				vector.set(index, 1.0);
			}
			result.add(new NamedVector<Double>(target, vector));
			if(result.size()%1000 == 0)
				System.out.println(result.size());
		}
		read.close();
		System.out.println("finish load instance");
		return result;
	}

	public static void main(String[] args) throws IOException {

		TrainSets<NamedVector<Double>> trainSet = loadInstances("D:/data/ipinyou/train.yzx.txt");

		SGDRegressionPolicy sgd = new SGDRegressionPolicy();
		long time = System.currentTimeMillis();
		RegressionModel model = new LogisticRegressionModel(
				SGDRegressionPolicy.initializeVector(trainSet.get(0).getValue().size()));
		sgd.run(trainSet, model);
		System.out.println(model);
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");

		Multiset<Tuple2<Double, Double>> set = HashMultiset.create();
		List<Tuple2<Double, Double>> preList = new ArrayList<Tuple2<Double, Double>>();
		for (NamedVector<Double> nv : trainSet) {
			double e = model.evaluate(nv.getValue());
			double cl = e >= 0.5 ? 1.0 : 0.0;
			set.add(new Tuple2<Double, Double>(nv.getName(), cl));
			preList.add(new Tuple2<Double, Double>(e, nv.getName()));
		}
		System.out.println(set);
	}

}
