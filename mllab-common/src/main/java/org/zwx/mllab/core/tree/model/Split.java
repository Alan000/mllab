package org.zwx.mllab.core.tree.model;

import java.util.Set;

import org.apache.mahout.math.Vector;

public class Split {

	int feature;
	double threshold;
	FeatureType featureType;
	Set<Double> categories;

	public boolean match(double feature) {
		switch (featureType) {
		case Continuous:
			return feature<=threshold;
		case Categorical:
			return categories.contains(feature);
		default:
			return false;
		}
	}
	
	public boolean match(Vector features) {
		return match(features.get(feature));
	}
	
}
