package org.inexas.notable.util;

import java.util.*;

public class ArrayU {
	public static double[] doubleArray(final List<Double> doubles) {
		final double[] returnValue;

		final int size = doubles.size();
		returnValue = new double[size];
		for(int i = 0; i < size; i++) {
			returnValue[i] = doubles.get(i);
		}

		return returnValue;
	}
}
