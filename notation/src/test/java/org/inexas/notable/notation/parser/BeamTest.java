package org.inexas.notable.notation.parser;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BeamTest extends ParserTestAbc {

	@Test
	void simple() {
		assertEquals("(C C) R R |||\n", toMiki("(CC) RR|||"));
		assertEquals("C (A8 B8 C8 D8) R |||\n", toMiki("C (A8 B8 C8 D8) R|||"));
	}
}
