package org.inexas.notable.util;

import java.util.*;

public class ArrayU {
	static double[] doubleArray(final List<Double> doubles) {
		final double[] returnValue;

		final int size = doubles.size();
		returnValue = new double[size];
		for(int i = 0; i < size; i++) {
			returnValue[i] = doubles.get(i);
		}

		return returnValue;
	}

	public static double[] parseDoubles(final String commaSeparatedString) {
		final double[] returnValue;

		if(commaSeparatedString == null || commaSeparatedString.trim().length() == 0) {
			returnValue = new double[0];
		} else {
			final String[] parts = commaSeparatedString.split(",");
			returnValue = new double[parts.length];
			for(int i = 0; i < parts.length; i++) {
				returnValue[i] = Double.parseDouble(parts[i]);
			}
		}

		return returnValue;
	}
}
