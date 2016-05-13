package org.zwx.mllab.core.tree.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.math.Vector;
import org.zwx.mllab.util.MathUtil;

public abstract class TreeEnsembleModel implements Serializable {

	private static final long serialVersionUID = 8468295996239169412L;

	private Algo algo;
	private DecisionTreeModel[] trees;
	private Double[] treeWeights;
	private Double sumWeight;
	private EnsembleCombingStrategy combiningStrategy;

	public double sumWeight() {
		return MathUtil.sum(treeWeights);
	}

	public double predictBySumming(Vector features) {
		if (sumWeight == null) {
			sumWeight = 0.0;
			for (int i = 0; i < trees.length; i++) {
				sumWeight += trees[i].predict(features) * treeWeights[i];
			}
		}
		return sumWeight;
	}

	public double predictByAveraging(Vector features) {
		return predictBySumming(features) / sumWeight();
	}

	public double predictByVoting(Vector features) {
		Map<Double, Double> map = new HashMap<Double, Double>();
		for (int i = 0; i < trees.length; i++) {
			double p = trees[i].predict(features);
			map.put(p, map.containsKey(p) ? (map.get(p) + treeWeights[i]) : treeWeights[i]);
		}
		double result = Double.NaN;
		double max = Double.MIN_NORMAL;
		for (Map.Entry<Double, Double> entry : map.entrySet())
			if (entry.getValue() > max) {
				result = entry.getKey();
				max = entry.getValue();
			}
		return result;
	}

	public double predict(Vector features) {
		if (algo == Algo.Regression && combiningStrategy == EnsembleCombingStrategy.Sum)
			return predictBySumming(features);
		else if (algo == Algo.Regression && combiningStrategy == EnsembleCombingStrategy.Average)
			return predictByAveraging(features);
		else if (algo == Algo.Classification && combiningStrategy == EnsembleCombingStrategy.Sum)
			return predictBySumming(features) > 0.0 ? 1.0 : 0.0;
		else if (algo == Algo.Classification && combiningStrategy == EnsembleCombingStrategy.Vote)
			return predictByVoting(features);
		else
			throw new IllegalArgumentException(
					"TreeEnsembleModel given unsupported (algo, combiningStrategy) combination:(regression, sum),(regression, average),(classification, sum),(classification, vote)");
	}

}
