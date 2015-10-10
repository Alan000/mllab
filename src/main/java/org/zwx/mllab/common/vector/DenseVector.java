package org.zwx.mllab.common.vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 数据密集型顺序向量<br/>
 * 适用于数据密集型
 * @author Alan
 *
 */
public class DenseVector extends AbstractVector {

	private static final long serialVersionUID = 784376637914809226L;

	private double[] values;

	public DenseVector() {
		this(0);
	}

	public DenseVector(int size) {
		super(size);
		values = new double[size];
	}

	public DenseVector(int size, double[] values) {
		super(size);
		this.values = values;
	}

	@Override
	public void setQuick(int index, double v) {
		invalidateCachedLength();
		values[index] = v;
	}

	@Override
	public double getQuick(int index) {
		return values[index];
	}
	
	@Override
	public void setAll(double[] values) {
		if (values.length > size()) {
			throw new IndexOutOfBoundsException("set out of index " + values.length);
		}
		invalidateCachedLength();
		System.arraycopy(values, 0, this.values, 0, values.length);
	}

	@Override
	protected Iterator<Element> iterator() {
		return new AllIterator();
	}

	@Override
	protected Iterator<Element> iterateNonZero() {
		return new NonDefaultIterator();
	}

	@Override
	public Vector clone() {
		double[] newValues = new double[this.values.length];
		System.arraycopy(this.values, 0, newValues, 0, this.values.length);
		return new DenseVector(this.size(), newValues);
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
		values = new double[size];
	}

	private final class NonDefaultIterator implements Iterator<Element> {
		private final DenseElement element = new DenseElement();
		private int index = -1;
		private int lookAheadIndex = -1;

		@Override
		public boolean hasNext() {
			if (lookAheadIndex == index) { // User calls hasNext() after a
											// next()
				lookAhead();
			} // else user called hasNext() repeatedly.
			return lookAheadIndex < size();
		}

		private void lookAhead() {
			lookAheadIndex++;
			while (lookAheadIndex < size() && values[lookAheadIndex] == 0.0) {
				lookAheadIndex++;
			}
		}

		@Override
		public Element next() {
			if (lookAheadIndex == index) { // If user called next() without
											// checking hasNext().
				lookAhead();
			}
			if (!(lookAheadIndex > index))
				throw new IllegalArgumentException();
			index = lookAheadIndex;

			if (index >= size()) { // If the end is reached.
				throw new NoSuchElementException();
			}

			element.index = index;
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final class AllIterator implements Iterator<Element> {
		private final DenseElement element = new DenseElement();

		private AllIterator() {
			element.index = -1;
		}

		@Override
		public boolean hasNext() {
			return element.index + 1 < size();
		}

		@Override
		public Element next() {
			if (element.index + 1 >= size()) { // If the end is reached.
				throw new NoSuchElementException();
			}
			element.index++;
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final class DenseElement implements Element {
		int index;

		@Override
		public double get() {
			return values[index];
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(double value) {
			invalidateCachedLength();
			values[index] = value;
		}
	}

	@Override
	public boolean isDense() {
		return true;
	}

	@Override
	public boolean isSequentialAccess() {
		return true;
	}

}
