package org.zwx.mllab.core.regression;

import org.zwx.mllab.common.vector.NamedVector;
import org.zwx.mllab.common.vector.Vector;
import org.zwx.mllab.lang.SizeException;

public class LogisticRegressionModel extends RegressionModel {

	@Override
	public double evaluate(Vector inputV) {
		if (params.size() != inputV.size())
			throw new SizeException();
		else
			return 1.0 / (1.0 + Math.exp(-params.dot(inputV)));
	}

	@Override
	Vector getParamsGradient(NamedVector<Double> inputNV) {
		return inputNV.getValue().divide(1.0 / (evaluate(inputNV.getValue()) - inputNV.getName()));
	}

	@Override
	double getParamGradient(NamedVector<Double> inputNV, int paramIndex) {
		return inputNV.getValue().get(paramIndex) / (1.0 / (evaluate(inputNV.getValue()) - inputNV.getName()));
	}

}
