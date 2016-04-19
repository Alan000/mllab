package org.zwx.mllab.util;

public class Tuple2<L, R> {
	private L _1;
	private R _2;

	public Tuple2(L _1, R _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public L get_1() {
		return _1;
	}

	public void set_1(L _1) {
		this._1 = _1;
	}

	public R get_2() {
		return _2;
	}

	public void set_2(R _2) {
		this._2 = _2;
	}

	@Override
	public int hashCode() {
		return _1.hashCode() + _2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tuple2) {
			Tuple2<?, ?> target = (Tuple2<?, ?>) obj;
			return target._1.equals(_1) && target._2.equals(_2);
		} else
			return false;
	}

	@Override
	public String toString() {
		return _1.toString() + "&" + _2.toString();
	}

}
