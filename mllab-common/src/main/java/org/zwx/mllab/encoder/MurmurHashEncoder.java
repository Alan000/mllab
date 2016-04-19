package org.zwx.mllab.encoder;

import org.zwx.mllab.hash.MurmurHash;
import org.zwx.mllab.util.ByteUtil;

import com.google.common.base.Charsets;

/**
 * MurmurHash 编码
 * 
 * @author Alan
 *
 */
public class MurmurHashEncoder extends AbstractEncoder<Integer> {

	public MurmurHashEncoder() {

	}

	/**
	 * 
	 * @param dimension
	 *            维度
	 */
	public MurmurHashEncoder(int dimension) {
		this.setDimension(dimension);
	}

	@Override
	public int encode(String term, Integer seed) {
		int code = MurmurHash.hash(term.getBytes(Charsets.UTF_8), seed);
		int remain = code % getDimension();
		return remain < 0 ? getDimension() + remain : remain;
	}

	@Override
	public int encode(byte[] term, Integer seed) {
		int code = MurmurHash.hash(term, seed);
		int remain = code % getDimension();
		return remain < 0 ? getDimension() + remain : remain;
	}

	@Override
	public int encode(Object term, Integer seed) {
		int code = MurmurHash.hash(ByteUtil.objectToByteStream(term), seed);
		int remain = code % getDimension();
		return remain < 0 ? getDimension() + remain : remain;
	}

}
