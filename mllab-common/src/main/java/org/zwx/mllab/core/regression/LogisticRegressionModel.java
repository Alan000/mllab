package org.zwx.mllab.core.regression;

import org.apache.mahout.math.Vector;
import org.zwx.mllab.lang.SizeException;

public class LogisticRegressionModel extends RegressionModel {
	
	public LogisticRegressionModel(Vector params) {
		this.params = params;
	}

	@Override
	public double evaluate(Vector inputV) {
		if (params.size() != inputV.size())
			throw new SizeException();
		else
			return 1.0 / (1.0 + Math.exp(-params.dot(inputV)));
	}

	@Override
	Vector getParamsGradient(NamedVector<Double> inputNV) {
		return inputNV.getValue().times(evaluate(inputNV.getValue()) - inputNV.getName());
	}

	@Override
	double getParamGradient(NamedVector<Double> inputNV, int paramIndex) {
		return inputNV.getValue().get(paramIndex) * (evaluate(inputNV.getValue()) - inputNV.getName());
	}

}
