package org.zwx.mllab.contest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PinyouSample {

	static int P = 8829;
	static int N = 12187629;
	static double sample_rate = P / (1.0 * N);

	public static void main(String[] args) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader("F://training.txt"));
		BufferedWriter write = new BufferedWriter(new FileWriter("F://sample.txt"));

		String line = null;
		Random random = new Random();
		while ((line = read.readLine()) != null) {
			if (line.startsWith("0")) {
				double r = random.nextDouble();
				if (r <= sample_rate) {
					write.write(line);
					write.newLine();
				}
			} else if (line.startsWith("1")) {
				write.write(line);
				write.newLine();
			}
		}

		read.close();
		write.close();
	}

}
