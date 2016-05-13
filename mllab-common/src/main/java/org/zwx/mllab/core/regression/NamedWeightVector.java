package org.zwx.mllab.core.regression;

import org.apache.mahout.math.Vector;

public class NamedWeightVector<K> extends NamedVector<K>{
	
	public NamedWeightVector() {
		super();
	}

	public NamedWeightVector(K name, Vector vector){
		super(name, vector);
	}
	
	private double weight;

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}	

}
