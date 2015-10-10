package org.zwx.mllab.core.regression;

import org.zwx.mllab.common.vector.NamedVector;
import org.zwx.mllab.common.vector.Vector;
import org.zwx.mllab.core.Model;

public abstract class RegressionModel implements Model {
	
	protected Vector params;
	
	public abstract double evaluate(Vector inputV);
	
	abstract Vector getParamsGradient(NamedVector<Double> inputNV);
	
	abstract double getParamGradient(NamedVector<Double> inputNV, int paramIndex);
	
	public void updateParams(Vector params) {
		this.params = params;
	}

	public Vector getParams() {
		return params;
	}
	
}
