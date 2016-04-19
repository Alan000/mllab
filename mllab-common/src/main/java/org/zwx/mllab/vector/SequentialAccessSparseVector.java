package org.zwx.mllab.vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 顺序存储的稀疏向量
 * 
 * @author Alan
 *
 */
public class SequentialAccessSparseVector extends AbstractVector {

	private static final long serialVersionUID = 9122657676828541656L;

	private int[] indexs;
	private double[] values;

	private int pos;

	public SequentialAccessSparseVector() {
		this(0);
	}

	public SequentialAccessSparseVector(int cardinality) {
		this(cardinality, Math.min(100, cardinality / 1000 < 10 ? 10 : cardinality / 1000));
	}

	public SequentialAccessSparseVector(int cardinality, int capacity) {
		super(cardinality);
		indexs = new int[capacity];
		values = new double[capacity];
		this.pos = 0;
	}

	private SequentialAccessSparseVector(int cardinality, int[] indexs, double[] values, int pos) {
		super(cardinality);
		this.indexs = indexs;
		this.values = values;
		this.pos = pos;
	}

	@Override
	public void setQuick(int index, double value) {
		invalidateCachedLength();
		if (pos == 0 || index > indexs[pos - 1]) {
			if (value != DEFAULT_VALUE) {
				if (pos >= indexs.length) {
					growTo(Math.max((int) (1.2 * pos), pos + 1));
				}
				indexs[pos] = index;
				values[pos] = value;
				++pos;
			}
		} else {
			int offset = find(index);
			if (offset >= 0) {
				insertOrUpdateValueIfPresent(offset, value);
			} else {
				insertValueIfNotDefault(index, offset, value);
			}
		}

	}

	@Override
	public double getQuick(int index) {
		int offset = find(index);
		return offset >= 0 ? values[offset] : DEFAULT_VALUE;
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
		int[] newIndexs = new int[this.indexs.length];
		System.arraycopy(this.indexs, 0, newIndexs, 0, this.indexs.length);
		double[] newValues = new double[this.values.length];
		System.arraycopy(this.values, 0, newValues, 0, this.values.length);
		return new SequentialAccessSparseVector(this.size(), newIndexs, newValues, this.pos);
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
	}

	private void growTo(int newCapacity) {
		if (newCapacity > indexs.length) {
			int[] newIndexs = new int[newCapacity];
			double[] newValues = new double[newCapacity];
			System.arraycopy(indexs, 0, newIndexs, 0, indexs.length);
			System.arraycopy(values, 0, newValues, 0, values.length);
			indexs = newIndexs;
			values = newValues;
		}
	}

	private int find(int index) {
		int low = 0;
		int high = pos - 1;
		while (low <= high) {
			int mid = low + (high - low >>> 1);
			int midVal = indexs[mid];
			if (midVal < index) {
				low = mid + 1;
			} else if (midVal > index) {
				high = mid - 1;
			} else {
				return mid;
			}
		}
		return -(low + 1);
	}

	private void insertValueIfNotDefault(int index, int offset, double value) {
		if (value != DEFAULT_VALUE) {
			if (pos >= indexs.length) {
				growTo(Math.max((int) (1.2 * pos), pos + 1));
			}
			int at = -offset - 1;
			if (pos > at) {
				for (int i = pos - 1, j = pos; i >= at; i--, j--) {
					indexs[j] = indexs[i];
					values[j] = values[i];
				}
			}
			indexs[at] = index;
			values[at] = value;
			pos++;
		}
	}

	private void insertOrUpdateValueIfPresent(int offset, double newValue) {
		if (newValue == DEFAULT_VALUE) {
			for (int i = offset + 1, j = offset; i < pos; i++, j++) {
				indexs[j] = indexs[i];
				values[j] = values[i];
			}
			pos--;
		} else {
			values[offset] = newValue;
		}
	}

	private final class AllElement implements Element {
		private int index = -1;
		private int nextOffset;

		void advanceIndex() {
			index++;
			if (nextOffset < pos && index > indexs[nextOffset]) {
				nextOffset++;
			}
		}

		int getNextIndex() {
			return index + 1;
		}

		@Override
		public double get() {
			if (nextOffset < pos && index == indexs[nextOffset]) {
				return values[nextOffset];
			} else {
				return DEFAULT_VALUE;
			}
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(double value) {
			invalidateCachedLength();
			if (nextOffset < pos && index == indexs[nextOffset]) {
				setQuick(nextOffset, value);
			} else {
				// Yes, this works; the offset into indices of the new value's
				// index will still be nextOffset
				setQuick(index, value);
			}
		}
	}

	private final class AllIterator implements Iterator<Element> {
		private final AllElement element = new AllElement();

		@Override
		public boolean hasNext() {
			return element.getNextIndex() < SequentialAccessSparseVector.this.size();
		}

		@Override
		public Element next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			element.advanceIndex();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private final class NonDefaultElement implements Element {
		private int offset = -1;

		void advanceOffset() {
			offset++;
		}

		int getNextOffset() {
			return offset + 1;
		}

		@Override
		public double get() {
			return values[offset];
		}

		@Override
		public int index() {
			return indexs[offset];
		}

		@Override
		public void set(double value) {
			invalidateCachedLength();

			insertOrUpdateValueIfPresent(offset, value);
			if (value == DEFAULT_VALUE) {
				offset --;
			}
		}
	}

	private final class NonDefaultIterator implements Iterator<Element> {
		private final NonDefaultElement element = new NonDefaultElement();

		@Override
		public boolean hasNext() {
			return element.getNextOffset() < pos;
		}

		@Override
		public Element next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			element.advanceOffset();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean isDense() {
		return false;
	}

	@Override
	public boolean isSequentialAccess() {
		return true;
	}
}
