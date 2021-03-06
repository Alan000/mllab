package org.zwx.mllab.core.regression;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.zwx.mllab.core.Model;
import org.zwx.mllab.core.Policy;
import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.lang.TypeMatchException;

public abstract class RegressionPolicy implements Policy<NamedVector<Double>> {

	@Override
	public void run(final TrainSets<NamedVector<Double>> ts, Model model) throws Exception {
		if (model instanceof RegressionModel) {
			run(ts, (RegressionModel) model);
		} else {
			throw new TypeMatchException();
		}
	}

	public abstract void run(final TrainSets<NamedVector<Double>> ts, RegressionModel model) throws Exception;
	
	public static Vector initializeVector(int size) {
		return new SequentialAccessSparseVector(size);
	}

}
