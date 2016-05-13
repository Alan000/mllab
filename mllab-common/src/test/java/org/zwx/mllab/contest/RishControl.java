package org.zwx.mllab.contest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.core.regression.LogisticRegressionModel;
import org.zwx.mllab.core.regression.NamedVector;
import org.zwx.mllab.core.regression.NamedWeightVector;
import org.zwx.mllab.core.regression.RegressionModel;
import org.zwx.mllab.core.regression.SGDRegressionPolicy;
import org.zwx.mllab.encoder.Encoder;
import org.zwx.mllab.encoder.MurmurHashEncoder;
import org.zwx.mllab.util.Tuple2;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class RishControl {

	static final int dimension = 20000;
	static final Encoder<Integer> encoder = new MurmurHashEncoder(dimension);

	private static TrainSets<NamedVector<Double>> loadInstances(String path) throws IOException {

		BufferedReader read = new BufferedReader(new FileReader(path));
		String line = read.readLine();
		String[] cols = line.split(",");

		int targetIndex = -1;
		for (int i = 0; i < cols.length; i++) {
			if ("Target".equalsIgnoreCase(cols[i]))
				targetIndex = i;
		}

		if (-1 == targetIndex) {
			System.err.println("no target field found, exit!");
			read.close();
			System.exit(-1);
		}

		TrainSets<NamedVector<Double>> result = new TrainSets<NamedVector<Double>>();
		while ((line = read.readLine()) != null) {
			String[] values = line.split(",");
			Vector vector = new SequentialAccessSparseVector(dimension);
			double target = -1;
			for (int i = 1; i < cols.length; i++) {
				if (i != targetIndex) {
					String v = cols[i] + ":" + values[i];
					vector.set(encoder.encode(v, dimension), 1);
				} else {
					target = Double.valueOf(values[i]);
				}
			}
			result.add(new NamedVector<Double>(target, vector));

		}
		read.close();
		System.out.println("finish load instance");
		return result;
	}

	private static Tuple2<TrainSets<NamedVector<Double>>, TrainSets<NamedVector<Double>>> split(
			TrainSets<NamedVector<Double>> allSets) {
		TrainSets<NamedVector<Double>> train = new TrainSets<NamedVector<Double>>();
		TrainSets<NamedVector<Double>> test = new TrainSets<NamedVector<Double>>();
		Random random = new Random();
		for (NamedVector<Double> nv : allSets) {
			int r = random.nextInt(10);
			if (r >= 1)
				train.add(nv);
			else
				test.add(nv);
		}

		return new Tuple2<TrainSets<NamedVector<Double>>, TrainSets<NamedVector<Double>>>(train, test);

	}

	private static TrainSets<NamedVector<Double>> sampleAndWeight(TrainSets<NamedVector<Double>> trainSet) {
		double N = 0;
		for (NamedVector<Double> nv : trainSet) {
			if (nv.getName() == 0.0)
				N++;
		}
		double SAMPLE_COR = 1.0;
		double SAMPLE_RATE = SAMPLE_COR * (trainSet.size() - N) / N;
		double PW = 1.0;
		double NW = 1.0 / SAMPLE_RATE;

		TrainSets<NamedVector<Double>> weight = new TrainSets<NamedVector<Double>>();
		for (NamedVector<Double> nv : trainSet) {
			NamedWeightVector<Double> nwv = new NamedWeightVector<Double>(nv.getName(), nv.getValue());
			if (nv.getName() == 1.0)
				nwv.setWeight(PW);
			else
				nwv.setWeight(NW);
			weight.add(nwv);
		}

		TrainSets<NamedVector<Double>> sample = new TrainSets<NamedVector<Double>>();

		Random random = new Random();
		for (NamedVector<Double> nv : weight) {
			if (nv.getName() == 1.0)
				sample.add(nv);
			else {
				if (random.nextDouble() <= SAMPLE_RATE)
					sample.add(nv);
			}
		}

		System.out.println("sample:" + sample.size());
		return sample;
	}

	private static List<Tuple2<Double, Double>> roc(List<Tuple2<Double, Double>> preList) {
		List<Tuple2<Double, Double>> result = new ArrayList<Tuple2<Double, Double>>();
		double P = 0, N = 0;
		for (Tuple2<Double, Double> tuple : preList) {
			if (tuple.get_2() == 1.0)
				P++;
			else
				N++;
		}

		for (int i = 0; i < preList.size(); i++) {
			double threshold = preList.get(i).get_1();
			int FP = 0, TP = 0;
			for (Tuple2<Double, Double> tuple : preList) {
				if (tuple.get_1() >= threshold) {
					if (tuple.get_2() == 1.0)
						TP++;
					else
						FP++;
				}
			}
			result.add(new Tuple2<Double, Double>(FP / N, TP / P));
		}
		return result;

	}

	public static void main(String[] args) throws IOException {
		String dataSource = "E:\\data\\contest\\kesci\\魔镜风控\\PPD-First-Round-Data-Updated\\PPD-First-Round-Data-Update\\Training Set\\PPD_Training_Master_GBK_3_1_Training_Set.csv";
		TrainSets<NamedVector<Double>> allSets = loadInstances(dataSource);
		Tuple2<TrainSets<NamedVector<Double>>, TrainSets<NamedVector<Double>>> splits = split(allSets);
		//TrainSets<NamedVector<Double>> trainSet = sampleAndWeight(splits.get_1());
		TrainSets<NamedVector<Double>> trainSet = splits.get_1();
		TrainSets<NamedVector<Double>> testSet = splits.get_2();

		SGDRegressionPolicy sgd = new SGDRegressionPolicy();
		long time = System.currentTimeMillis();
		RegressionModel model = new LogisticRegressionModel(
				SGDRegressionPolicy.initializeVector(trainSet.get(0).getValue().size()));
		sgd.run(trainSet, model);
		System.out.println(model);
		System.out.println("Time:" + (System.currentTimeMillis() - time) + "ms");

		Multiset<Tuple2<Double, Double>> set = HashMultiset.create();
		List<Tuple2<Double, Double>> preList = new ArrayList<Tuple2<Double, Double>>();
		for (NamedVector<Double> nv : testSet) {
			double e = model.evaluate(nv.getValue());
			double cl = e > 0.5 ? 1.0 : 0.0;
			set.add(new Tuple2<Double, Double>(nv.getName(), cl));
			preList.add(new Tuple2<Double, Double>(e, nv.getName()));
		}
		System.out.println(set);

		
		System.out.println(preList);
	}

}
