package org.zwx.mllab.core.tree.model;

import java.io.Serializable;

import org.apache.mahout.math.Vector;

public class DecisionTreeModel implements Serializable {

	private static final long serialVersionUID = -1893052087035417983L;

	private Node topNode;
	private Algo algo;

	public double predict(Vector features) {
		return topNode.predict(features);
	}

	public int numNodes() {
		return 1 + topNode.numDescendants();
	}

	public int depth() {
		return topNode.subtreeDepth();
	}
}
