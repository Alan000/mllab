package org.zwx.mllab.common.vector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 随机存储的稀疏向量
 * 
 * @author Alan
 *
 */
public class RandomAccessSparseVector extends AbstractVector {

	private static final long serialVersionUID = -7863915156606962079L;

	private LinkedList<Integer> indexs;
	private LinkedList<Double> values;

	public RandomAccessSparseVector() {
		this(0);
	}

	public RandomAccessSparseVector(int cardinality) {
		super(cardinality);
		indexs = new LinkedList<Integer>();
		values = new LinkedList<Double>();
	}

	@Override
	public void setQuick(int index, double v) {
		invalidateCachedLength();
		if (v != DEFAULT_VALUE) {
			if (!indexs.contains(index)) {
				indexs.add(index);
				values.add(v);
			} else {
				int pos = indexs.indexOf(index);
				values.set(pos, v);
			}
		} else if (indexs.contains(index)) {
			int pos = indexs.indexOf(index);
			indexs.remove(pos);
			values.remove(pos);
		}
	}

	@Override
	public double getQuick(int index) {
		int pos = indexs.indexOf(index);
		if (pos == -1)
			return DEFAULT_VALUE;
		Double v = values.get(pos);
		return v;
	}

	@Override
	public boolean isDense() {
		return false;
	}

	@Override
	public boolean isSequentialAccess() {
		return false;
	}

	@Override
	protected Iterator<Element> iterator() {
		return new AllIterator();
	}

	@Override
	protected Iterator<Element> iterateNonZero() {
		return new NonDefaultIterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector clone() {
		LinkedList<Integer> newIndexs = (LinkedList<Integer>) indexs.clone();
		LinkedList<Double> newValues = (LinkedList<Double>) values.clone();
		RandomAccessSparseVector newVector = new RandomAccessSparseVector(this.size());
		newVector.indexs = newIndexs;
		newVector.values = newValues;
		return newVector;
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
	}

	private final class NonDefaultIterator implements Iterator<Element> {

		private final NoZeroRandomAccessElement element = new NoZeroRandomAccessElement();

		private NonDefaultIterator() {
			element.pos = -1;
			element.index = -1;
		}

		@Override
		public boolean hasNext() {
			if (element.pos < (indexs.size() - 1))
				return true;
			else
				return false;
		}

		@Override
		public Element next() {
			element.advanceNext();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private final class AllIterator implements Iterator<Element> {
		private final RandomAccessElement element = new RandomAccessElement();

		private AllIterator() {
			element.index = -1;
			element.pos = -1;
		}

		@Override
		public boolean hasNext() {
			if (element.index < (RandomAccessSparseVector.this.size() - 1) || indexs.indexOf(element.index) < (indexs.size() - 1))
				return true;
			else
				return false;
		}

		@Override
		public Element next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			element.index++;
			element.pos = indexs.indexOf(element.index);
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final class NoZeroRandomAccessElement implements Element {
		int index;
		int pos;

		@Override
		public double get() {
			return values.get(pos);
		}

		@Override
		public int index() {
			return index;
		}

		public void advanceNext() {
			pos++;
			index = indexs.get(pos);
		}

		@Override
		public void set(double value) {
			invalidateCachedLength();
			if (value == DEFAULT_VALUE) {
				indexs.remove(pos);
				values.remove(pos);
				pos--;
			} else {
				values.set(pos, value);
			}
		}
	}

	private final class RandomAccessElement implements Element {
		int index;
		int pos;

		@Override
		public double get() {
			if (pos == -1)
				return DEFAULT_VALUE;
			else
				return values.get(pos);
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(double value) {
			invalidateCachedLength();
			if (value == DEFAULT_VALUE && pos != -1) {
				indexs.remove(pos);
				values.remove(pos);
			} else {
				indexs.add(index);
				values.add(value);
				pos = indexs.size() - 1;
			}
		}
	}

}
