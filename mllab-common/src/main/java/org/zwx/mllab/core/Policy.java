package org.zwx.mllab.core;

public interface Policy<T> {
	
	public void run(final TrainSets<T> ts, Model model) throws Exception;
	
}
