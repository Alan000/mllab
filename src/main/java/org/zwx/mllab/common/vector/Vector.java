package org.zwx.mllab.common.vector;

import java.io.Serializable;

public interface Vector extends Serializable, Cloneable {
	
	static final double DEFAULT_VALUE = 0.0;

	void set(int index, double v);

	void setQuick(int index, double v);

	void setAll(int[] indexs, double[] values);
	
	void setAll(double[] values);

	Vector plus(Vector v);

	Vector minus(Vector v);

	Vector divide(double d);

	double dot(Vector v);

	double length();

	double lengthSquare();

	int size();
	
	void setSize(int size);

	double get(int index);

	Element getElement(int index);

	double getQuick(int index);

	int getNumNonZeroElements();

	boolean isDense();

	boolean isSequentialAccess();

	public interface Element {

		/** @return the value of this vector element. */
		double get();

		/** @return the index of this vector element. */
		int index();

		/**	@param value set the current element to value. */
		void set(double value);
	}

	Iterable<Element> all();

	Iterable<Element> nonZeroes();

	Vector clone();

	void invalidateCachedLength();
}
