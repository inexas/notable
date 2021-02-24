package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DurationTest {

	@Test
	void clickCounts() {
		Assertions.assertEquals(32, Duration.whole.clicks);

		assertEquals(16, Duration.half.clicks);
		assertEquals(24, Duration.halfDot.clicks);
		assertEquals(28, Duration.halfDotDot.clicks);
		assertEquals(30, Duration.halfDotDotDot.clicks);
		assertEquals(31, Duration.halfDotDotDotDot.clicks);

		assertEquals(8, Duration.quarter.clicks);
		assertEquals(12, Duration.quarterDot.clicks);
		assertEquals(14, Duration.quarterDotDot.clicks);
		assertEquals(15, Duration.quarterDotDotDot.clicks);

		assertEquals(4, Duration.eighth.clicks);
		assertEquals(6, Duration.eighthDot.clicks);
		assertEquals(7, Duration.eighthDotDot.clicks);

		assertEquals(2, Duration.sixteenth.clicks);
		assertEquals(3, Duration.sixteenthDot.clicks);

		assertEquals(1, Duration.thirtySecond.clicks);
	}

	@Test
	void getByMiki() {
		assertEquals(1, Duration.getByMiki("32").clicks);
		assertEquals(2, Duration.getByMiki("16").clicks);
		assertEquals(4, Duration.getByMiki("8").clicks);
		assertEquals(8, Duration.getByMiki("4").clicks);
		assertEquals(16, Duration.getByMiki("2").clicks);
		assertEquals(32, Duration.getByMiki("1").clicks);
	}

	@Test
	void getByClicksCalculation() {
		for(int i = 1; i < 32; i++) {
			final Duration[] durations = Duration.getByClicks(i);
			int total = 0;
			for(final Duration duration : durations) {
				total += duration.clicks;
			}
			assertEquals(i, total);
		}
	}

	@Test
	void testDuration() {
		assertEquals("32", Duration.thirtySecond.miki);
		assertEquals("16", Duration.sixteenth.miki);
		assertEquals("16.", Duration.sixteenthDot.miki);
		assertEquals("8", Duration.eighth.miki);
		assertEquals("8.", Duration.eighthDot.miki);
		assertEquals("8..", Duration.eighthDotDot.miki);
		assertEquals("4", Duration.quarter.miki);
		assertEquals("4.", Duration.quarterDot.miki);
		assertEquals("4..", Duration.quarterDotDot.miki);
		assertEquals("2", Duration.half.miki);
		assertEquals("2.", Duration.halfDot.miki);
		assertEquals("2..", Duration.halfDotDot.miki);
		assertEquals("1", Duration.whole.miki);
	}

	@Test
	void allFields() {
		assertEquals("whole", Duration.whole.name);
		assertEquals(1, Duration.whole.denominator);
		assertEquals(32, Duration.whole.clicks);
		assertFalse(Duration.whole.setDefault);
		assertEquals("1", Duration.whole.miki);
		assertEquals(0, Duration.whole.dots);
		assertEquals(Duration.whole, Duration.whole.root);

		assertEquals("halfDot", Duration.halfDot.name);
		assertEquals(2, Duration.halfDot.denominator);
		assertEquals(24, Duration.halfDot.clicks);
		assertFalse(Duration.halfDot.setDefault);
		assertEquals("2.", Duration.halfDot.miki);
		assertEquals(1, Duration.halfDot.dots);
		assertEquals(Duration.half, Duration.halfDot.root);

		final Duration duration = Duration.getByMiki("4..*");
		assertEquals("quarterDotDot", duration.name);
		assertEquals(4, duration.denominator);
		assertEquals(14, duration.clicks);
		assertTrue(duration.setDefault);
		assertEquals("4..*", duration.miki);
		assertEquals(2, duration.dots);
		assertEquals(Duration.quarter, duration.root);
	}

	@Test
	void getByClicks() {
		assertEquals("4.", Duration.getByClicks(12)[0].miki);
	}

	@Test
	void getByDenominator() {
		assertEquals("quarter", Duration.getByDenominator(4).name);
	}
}