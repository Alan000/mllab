package org.zwx.mllab.lang;

public class UnsupportedException extends RuntimeException{

	private static final long serialVersionUID = -1619124310761081213L;
	
	public UnsupportedException() {
        super();
    }

    public UnsupportedException(String msg) {
        super(msg);
    }

}
