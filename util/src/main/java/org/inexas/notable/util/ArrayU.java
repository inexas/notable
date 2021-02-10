package org.inexas.notable.util;

import java.util.*;

public class ArrayU {
	static double[] doubleArray(final List<Double> doubles) {
		final double[] result;

		final int size = doubles.size();
		result = new double[size];
		for(int i = 0; i < size; i++) {
			result[i] = doubles.get(i);
		}

		return result;
	}

	public static double[] parseDoubles(final String commaSeparatedString) {
		final double[] result;

		if(commaSeparatedString == null || commaSeparatedString.trim().length() == 0) {
			result = new double[0];
		} else {
			final String[] parts = commaSeparatedString.split(",");
			result = new double[parts.length];
			for(int i = 0; i < parts.length; i++) {
				result[i] = Double.parseDouble(parts[i]);
			}
		}

		return result;
	}
}
