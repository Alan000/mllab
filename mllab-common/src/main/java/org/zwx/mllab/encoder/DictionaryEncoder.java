package org.zwx.mllab.encoder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * 字典编码
 * 
 * @author Alan
 *
 */
public class DictionaryEncoder extends AbstractEncoder<Integer> {
	private List<Object> dictionary;

	public DictionaryEncoder() {
		this.dictionary = new LinkedList<Object>();
	}

	public DictionaryEncoder(int dimension) {
		this.setDimension(dimension);
		this.dictionary = new LinkedList<Object>();
	}

	public DictionaryEncoder(Collection<Object> dictionary, int dimension) {
		if (dictionary.size() > dimension)
			throw new SizeException(dictionary.size(), dimension);
		this.dictionary = new LinkedList<Object>();
		this.dictionary.addAll(dictionary);
		this.setDimension(dimension);
	}

	@Override
	public int encode(String term, Integer defaultIndex) {
		if (defaultIndex >= this.getDimension())
			throw new SizeException(dictionary.size(), this.getDimension());
		int index = dictionary.indexOf(term);
		return index == -1 ? defaultIndex : index;
	}

	@Override
	public int encode(byte[] term, Integer defaultIndex) {
		if (defaultIndex >= this.getDimension())
			throw new SizeException(dictionary.size(), this.getDimension());
		int index = dictionary.indexOf(term);
		return index == -1 ? defaultIndex : index;
	}

	@Override
	public int encode(Object term, Integer defaultIndex) {
		if (defaultIndex >= this.getDimension())
			throw new SizeException(dictionary.size(), this.getDimension());
		int index = dictionary.indexOf(term);
		return index == -1 ? defaultIndex : index;
	}

	public List<Object> getDictionary() {
		return dictionary;
	}

	public void setDictionary(List<Object> dictionary) {
		assert (dictionary.size() <= this.getDimension());
		if (dictionary.size() > this.getDimension())
			throw new SizeException(dictionary.size(), this.getDimension());
		this.dictionary = dictionary;
	}
	
}
