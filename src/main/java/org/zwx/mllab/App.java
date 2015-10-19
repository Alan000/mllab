package org.zwx.mllab;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++)
			System.out.println(Math.log(i)/Math.log(0.5) + "\t" + "1:" + i);
	}
}
