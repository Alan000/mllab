package org.zwx.mllab.contest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PinyouCount {

	static int P = 0, N = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader("e:/WorkStation/pyworks/py_test/alan/data/train.yzx.txt"));

		String line = null;

		while ((line = read.readLine()) != null) {
			if(line.startsWith("0"))
				N++;
			else if(line.startsWith("1"))
				P++;
		}
		
		read.close();
		System.out.println("P:"+P);
		System.out.println("N:"+N);
	}
}
