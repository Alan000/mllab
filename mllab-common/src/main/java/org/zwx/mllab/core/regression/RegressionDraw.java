package org.zwx.mllab.core.regression;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.zwx.mllab.core.TrainSets;

public class RegressionDraw extends Applet {
	private static final long serialVersionUID = 3232989527976843652L;
	TrainSets<NamedVector<Double>> ts;

	private static final double zoom = 20.0;

	private static final int radius0 = 6;
	private static final int radius1 = 4;

	public void init() {
		resize(480, 280);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("regression.data")));
			ts = new TrainSets<NamedVector<Double>>();
			String line = null;
			int size = Integer.valueOf(in.readLine()) + 1;
			while ((line = in.readLine()) != null) {
				if (line.length() < 3)
					continue;
				String[] kv = line.split("\t");
				double name = Double.valueOf(kv[0]);
				Vector v = initializeVector(size);
				v.set(0, 1.0);
				String[] splits = kv[1].split(" ");
				for (String s : splits) {
					String[] iv = s.split(":");
					v.set(Integer.valueOf(iv[0]), Double.valueOf(iv[1]));
				}
				ts.add(new NamedVector<Double>(name, v));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected static Vector initializeVector(int size) {
		return new SequentialAccessSparseVector(size);
	}

	@Override
	public void paint(Graphics g) {
		g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g.setColor(new Color(255, 0, 0));
		for (NamedVector<Double> nv : ts) {
			drowPoint(g, nv, radius0);
		}

		LocalWeightLinearRegressionPolicy lwr = new LocalWeightLinearRegressionPolicy(1.0);
		RegressionModel model = new LocalWeightLinearRegressionModel();
		try {
			lwr.run(ts, model);
		} catch (Exception e) {
			e.printStackTrace();
		}

		g.setColor(new Color(0, 0, 255));
		for (int i = 0; i < this.getWidth(); i += 4) {
			Vector v = initializeVector(2);
			v.set(0, 1.0);
			v.set(1, i / zoom);
			double value = model.evaluate(v);
			if (value * radius1 > getHeight())
				break;
			drowPoint(g, new NamedVector<Double>(value, v), radius1);
		}

	}

	public void drowPoint(Graphics g, NamedVector<Double> nv, int radius) {
		int c_x = (int) (zoom * nv.getValue().get(1));
		int c_y = this.getHeight() / 2 - (int) (2 * zoom * nv.getName());
		int p_x = c_x - radius / 2;
		int p_y = c_y - radius / 2;
		g.drawRoundRect(p_x, p_y, radius, radius, radius, radius);
	}
}
