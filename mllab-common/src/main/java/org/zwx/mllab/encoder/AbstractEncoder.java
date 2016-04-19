package org.zwx.mllab.encoder;

/**
 * 编码抽象类
 * 
 * @author Alan
 *
 */
public abstract class AbstractEncoder<T> implements Encoder<T> {

	private int dimension;

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}	

}
