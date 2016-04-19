package org.zwx.mllab.util;

public class Tuple3<L, C, R> {
	private L _1;
	private C _2;
	private R _3;

	public Tuple3(L _1, C _2, R _3) {
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
	}

	public L get_1() {
		return _1;
	}

	public void set_1(L _1) {
		this._1 = _1;
	}

	public C get_2() {
		return _2;
	}

	public void set_2(C _2) {
		this._2 = _2;
	}

	public R get_3() {
		return _3;
	}

	public void set_3(R _3) {
		this._3 = _3;
	}

}
