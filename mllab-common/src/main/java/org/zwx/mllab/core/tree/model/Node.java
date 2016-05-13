package org.zwx.mllab.core.tree.model;

import org.apache.mahout.math.Vector;

public class Node {

	private int id;
	private Predict predict;
	private double impurity;
	private boolean isLeaf;
	private Split split;
	private Node leftNode;
	private Node rightNode;
	private InformationGainStats stats;

	@Override
	public String toString() {
		return String.format("id = %s, isLeaf = %s, predict = %s, impurity = %s, split = %s, stats = %s", id, isLeaf,
				predict, impurity, split, stats);
	}

	public double predict(Vector features) {
		if (isLeaf)
			return predict.predict;
		else
			return split.match(features) ? leftNode.predict(features) : rightNode.predict(features);
	}

	public int matchLeafID(Vector features) {
		if (isLeaf)
			return id;
		else
			return split.match(features) ? leftNode.matchLeafID(features) : rightNode.matchLeafID(features);
	}

	public int numDescendants() {
		if (isLeaf)
			return 0;
		else
			return 2 + leftNode.numDescendants() + rightNode.numDescendants();
	}

	public int subtreeDepth() {
		if (isLeaf)
			return 0;
		else
			return 1 + Math.max(leftNode.numDescendants(), rightNode.numDescendants());
	}

}
