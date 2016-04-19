package org.zwx.mllab.encoder;

/**
 * 编码
 * @author Alan
 *
 */
public interface Encoder<T> {
	
	/**
	 * encode object
	 * @param term
	 * @param param parameter( default, seed or others)
	 * @return
	 */
	public int encode(Object term, T param);
	
	/**
	 * encode string
	 * @param term
	 * @param param parameter( default, seed or others)
	 * @return
	 */
	public int encode(String term, T param);
	
	/**
	 * encode byte[]
	 * @param term
	 * @param param parameter( default, seed or others)
	 * @return
	 */
	public int encode(byte[] term, T param);
	

	int getDimension();

	void setDimension(int dimension);
}
