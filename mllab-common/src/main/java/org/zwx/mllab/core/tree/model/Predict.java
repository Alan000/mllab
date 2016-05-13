package org.zwx.mllab.core.tree.model;

import java.util.Arrays;

class Predict {

	double predict;
	double prop; // for classification only

	@Override
	public String toString() {
		return String.format("%s (prob = %s)", predict, prop);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Predict) {
			Predict obj = (Predict) o;
			return predict == obj.predict && prop == obj.prop;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[] { predict, prop });
	}

}
