package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CpmTest extends ParserTestAbc {

	@Test
	void testFraction() {
		Fraction f = new Fraction(1, 2);
		assertEquals(1, f.numerator);
		assertEquals(2, f.denominator);

		f = new Fraction("1/2");
		assertEquals(1, f.numerator);
		assertEquals(2, f.denominator);

		f = new Fraction("11/22");
		assertEquals(11, f.numerator);
		assertEquals(22, f.denominator);

		assertEquals("11/22", f.toString());
	}

	@Test
	void testCpmMeasure() {
		assertEquals("cpm 16\nC C |||\n", toMiki("cpm 16 C C |||"));
	}
}
