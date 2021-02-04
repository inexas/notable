/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */
package org.inexas.notable.util;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUTest {

	@Test
	void basic() {
		final List<Double> doubles = new ArrayList<>();
		assertArrayEquals(new double[0], ArrayU.doubleArray(doubles));
		doubles.add(0.0);
		doubles.add(1.1);
		assertArrayEquals(new double[]{0.0, 1.1}, ArrayU.doubleArray(doubles));
	}
}