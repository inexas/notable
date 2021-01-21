package org.inexas.notable.notation.render;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {
	private static Metrics metrics;

	@BeforeAll
	public static void setup() {
		metrics = Metrics.instance;
	}

	@Test
	public void testBasics() {
		assertEquals(0.16, metrics.arrowShaftThickness);
	}
}
