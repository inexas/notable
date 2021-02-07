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

	@Test
	void doubleArray() {
		assertEquals(0, ArrayU.parseDoubles(null).length);
		assertEquals(0, ArrayU.parseDoubles("").length);
		assertEquals(0.0, ArrayU.parseDoubles("0")[0]);
		assertEquals(1.0, ArrayU.parseDoubles("1")[0]);
		assertEquals(0.0, ArrayU.parseDoubles("0.0,1.1, 2.2")[0]);
		assertEquals(1.1, ArrayU.parseDoubles("0.0,1.1, 2.2")[1]);
		assertEquals(2.2, ArrayU.parseDoubles("0.0,1.1, 2.2")[2]);
	}
}