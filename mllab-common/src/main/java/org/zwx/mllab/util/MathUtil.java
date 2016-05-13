package org.zwx.mllab.util;

public class MathUtil {

	public static <T extends Number> double sum(T[] ts) {
		double sum = 0.0;
		for (T t : ts)
			sum += t.doubleValue();
		return sum;
	}

}
