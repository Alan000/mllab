package org.zwx.mllab.core.regression;

import org.zwx.mllab.core.Model;
import org.zwx.mllab.core.Policy;
import org.zwx.mllab.core.TrainSets;
import org.zwx.mllab.lang.TypeMatchException;
import org.zwx.mllab.vector.DenseVector;
import org.zwx.mllab.vector.NamedVector;
import org.zwx.mllab.vector.Vector;

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
		return new DenseVector(size);
	}

}
