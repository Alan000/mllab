package org.zwx.mllab.core.regression;

import org.zwx.mllab.lang.UnsupportedException;
import org.zwx.mllab.vector.NamedVector;
import org.zwx.mllab.vector.Vector;

public class LocalWeightLinearRegressionModel extends RegressionModel {

	LocalWeightLinearRegressionPolicy policy;

	@Override
	public double evaluate(Vector inputV) {
		return policy.getParams(inputV).dot(inputV);
	}

	@Override
	Vector getParamsGradient(NamedVector<Double> inputNV) {
		throw new UnsupportedException("get params gradient not support by no-params function");
	}

	@Override
	double getParamGradient(NamedVector<Double> inputNV, int paramIndex) {
		throw new UnsupportedException("get param gradient not support by no-params function");
	}

	@Override
	public void updateParams(Vector params) {
		throw new UnsupportedException("update params not support by no-params function");
	}

	@Override
	public Vector getParams() {
		throw new UnsupportedException("get params not support by no-params function");
	}

}
