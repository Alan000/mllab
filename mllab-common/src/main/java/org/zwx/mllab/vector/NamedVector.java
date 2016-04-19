package org.zwx.mllab.vector;


public class NamedVector<K> {

	private K name;
	private Vector value;
	
	public NamedVector() {
		super();
	}

	public NamedVector(K name, Vector vector){
		this.name = name;
		this.value = vector;
	}

	public K getName() {
		return this.name;
	}

	public void setName(K name) {
		this.name = name;
	}	
	
	public Vector getValue() {
		return value;
	}

	public void setValue(Vector value) {
		this.value = value;
	}

	@Override
	public NamedVector<K> clone() {
		NamedVector<K> newVector = new NamedVector<K>();
		newVector.name = name;
		newVector.value = value.clone();
		return newVector;
	}

}
