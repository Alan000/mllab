package org.zwx.mllab.core.regression;

import org.zwx.mllab.common.vector.NamedVector;
import org.zwx.mllab.common.vector.Vector;
import org.zwx.mllab.lang.UnsupportedException;

public class LocalWeightLinearRegressionModel extends RegressionModel{

	@Override
	public double evaluate(Vector inputV) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	Vector getParamsGradient(NamedVector<Double> inputNV) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	double getParamGradient(NamedVector<Double> inputNV, int paramIndex) {
		// TODO Auto-generated method stub
		return 0;
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
