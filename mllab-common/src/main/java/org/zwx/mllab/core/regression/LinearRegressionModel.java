package org.zwx.mllab.core.regression;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.zwx.mllab.lang.SizeException;

public class LinearRegressionModel extends RegressionModel {

	public LinearRegressionModel(Vector params) {
		this.params = params;
	}

	@Override
	public double evaluate(Vector inputV) {
		if (params.size() != inputV.size())
			throw new SizeException();
		else
			return params.dot(inputV);
	}

	public String toString() {
		return "LRM={" + params.toString() + "}";
	}

	@Override
	Vector getParamsGradient(NamedVector<Double> inputNV) {
		double eva = evaluate(inputNV.getValue());
		if (eva == inputNV.getName())
			return new SequentialAccessSparseVector(params.size());
		else
			return inputNV.getValue().times(eva - inputNV.getName());
	}

	@Override
	double getParamGradient(NamedVector<Double> inputNV, int paramIndex) {
		return inputNV.getValue().get(paramIndex) * (evaluate(inputNV.getValue()) - inputNV.getName());
	}

}
