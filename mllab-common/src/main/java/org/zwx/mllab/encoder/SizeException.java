package org.zwx.mllab.encoder;

public class SizeException extends RuntimeException{

	private static final long serialVersionUID = 6800097528605233054L;

	public SizeException() {
        super();
    }

    public SizeException(int s1, int s2) {
        super("size not match " + s1 + ":" + s2);
    }
}
