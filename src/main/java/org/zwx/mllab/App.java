package org.zwx.mllab;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++)
			System.out.println(Math.cos(i/10.0) + "\t" + "1:" + i/10.0);
	}
}
