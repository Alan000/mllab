package org.zwx.mllab.common.vector;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.zwx.mllab.lang.SizeException;


/**
 * 向量抽象类，依赖第三方的实现类最好不要混合使用，混合使用待测试,设计上是可以的
 * 
 * @author Alan
 *
 */
public abstract class AbstractVector implements Vector {

	private static final long serialVersionUID = -592046925697608601L;

	private int size;
	protected transient double lengthSquared = -1.0;

	public AbstractVector() {

	}

	public AbstractVector(int size) {
		this.size = size;
	}

	@Override
	public Iterable<Element> all() {
		return new Iterable<Element>() {
			@Override
			public Iterator<Element> iterator() {
				return AbstractVector.this.iterator();
			}
		};
	}

	@Override
	public Iterable<Element> nonZeroes() {
		return new Iterable<Element>() {
			@Override
			public Iterator<Element> iterator() {
				return iterateNonZero();
			}
		};
	}

	protected abstract Iterator<Element> iterator();

	protected abstract Iterator<Element> iterateNonZero();

	@Override
	public Vector divide(double x) {
		if (x == 1.0) {
			return (Vector) clone();
		}
		Vector result = (Vector) clone();
		for (Element element : result.nonZeroes()) {
			element.set(element.get() / x);
		}
		return result;
	}

	@Override
	public double get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("get out of index " + size);
		}
		return getQuick(index);
	}

	@Override
	public Element getElement(int index) {
		return new LocalElement(index);
	}

	@Override
	public void set(int index, double value) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("set out of index " + size);
		}
		setQuick(index, value);
	}

	@Override
	public void setAll(int[] indexs, double[] values) {
		if (indexs.length != values.length)
			throw new SizeException(indexs.length, values.length);

		if (indexs.length < 0 || indexs.length >= size) {
			throw new IndexOutOfBoundsException("set out of index " + size);
		}
		
		for (int i = 0; i < indexs.length; i++) {
			setQuick(indexs[i], values[i]);
		}
	}

	@Override
	public void setAll(double[] values) {
		if (values.length > size) {
			throw new IndexOutOfBoundsException("set out of index " + values.length);
		}
		for (int i = 0; i < values.length; i++) {
			setQuick(i, values[i]);
		}
	}

	@Override
	public Vector plus(Vector v) {
		if (this.size() != v.size())
			throw new SizeException(this.size(), v.size());

		Vector result = this.clone();
		for (Element e : v.nonZeroes()) {
			result.set(e.index(), result.get(e.index()) + e.get());
		}
		return result;
	}

	@Override
	public Vector minus(Vector v) {
		if (this.size() != v.size())
			throw new SizeException(this.size(), v.size());

		Vector result = this.clone();
		for (Element e : v.nonZeroes()) {
			result.set(e.index(), result.get(e.index()) - e.get());
		}
		return result;
	}

	@Override
	public double dot(Vector v) {

		if (this.size() != v.size())
			throw new SizeException(this.size(), v.size());

		double result = 0.0;
		if (this.isSequentialAccess() && v.isSequentialAccess()) {
			Iterator<Element> left;
			Iterator<Element> right;

			if (this.isDense())
				left = this.iterator();
			else
				left = this.iterateNonZero();
			if (v.isDense())
				right = v.all().iterator();
			else
				right = v.nonZeroes().iterator();
			if (left.hasNext() && right.hasNext()) {
				/*
				 * Element l = null; Element r = null; while (left.hasNext() ||
				 * right.hasNext()) { if (l == null && left.hasNext()) l =
				 * left.next(); if (r == null && right.hasNext()) r =
				 * right.next(); if (l != null && r != null) { if (l.index() ==
				 * r.index()) { result += l.get() * r.get(); l = null; r = null;
				 * } else if (l.index() < r.index()) l = null; else if
				 * (l.index() > r.index()) r = null; } }
				 */
				Element l = left.next();
				Element r = right.next();
				while (true) {
					if (l.index() == r.index()) {
						result += l.get() * r.get();
						if (left.hasNext())
							l = left.next();
						else
							break;
						if (right.hasNext())
							r = right.next();
						else
							break;
					} else if (l.index() < r.index()) {
						if (left.hasNext())
							l = left.next();
						else
							break;
					} else if (l.index() > r.index()) {
						if (right.hasNext())
							r = right.next();
						else
							break;
					}
				}

			}
		} else {
			for (Element e : v.nonZeroes()) {
				result += get(e.index()) * e.get();
			}
		}
		return result;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public int getNumNonZeroElements() {
		int count = 0;
		Iterator<Element> it = iterateNonZero();
		while (it.hasNext()) {
			if (it.next().get() != 0.0) {
				count++;
			}
		}
		return count;
	}

	@Override
	public double length() {
		if (lengthSquared < 0.0) {
			lengthSquared = dotSelf();
		}
		return Math.sqrt(lengthSquared);
	}

	@Override
	public double lengthSquare() {
		if (lengthSquared < 0.0) {
			lengthSquared = dotSelf();
		}
		return lengthSquared;
	}

	protected double dotSelf() {
		return dot(this);
	}

	@Override
	public void invalidateCachedLength() {
		lengthSquared = -1;
	}

	protected final class LocalElement implements Element {
		int index;

		LocalElement(int index) {
			this.index = index;
		}

		@Override
		public double get() {
			return getQuick(index);
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(double value) {
			setQuick(index, value);
		}
	}

	@Override
	public Vector clone() {
		try {
			AbstractVector r = (AbstractVector) super.clone();
			r.size = size;
			r.lengthSquared = lengthSquared;
			return r;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Can't happen");
		}
	}

	public String toString() {
		if (isDense()) {
			double[] values = new double[size()];
			for (Element e : all()) {
				values[e.index()] = e.get();
			}
			return Arrays.toString(values);
		} else {
			Map<Integer, Double> kv = new TreeMap<Integer, Double>();
			for (Element e : nonZeroes()) {
				kv.put(e.index(), e.get());
			}
			return kv.toString();
		}
	}
}
