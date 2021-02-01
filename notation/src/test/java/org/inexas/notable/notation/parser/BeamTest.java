package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BeamTest {
	private String toMiki(final String string) {
		final Score score = MikiParser.fromString(string).score;
		return score.toString();
	}

	@Test
	void simple() {
		assertEquals("(C16* C C C) R4 R2 ||\n", toMiki("C16* C C C"));
		assertEquals("(C) R R2 ||\n", toMiki("(C)"));
		assertEquals("C (A8 B8) (C8 D8) R ||\n", toMiki("C (A8 B8 C8 D8)"));
		assertEquals("C (A8 B8) (C8 D8) R ||\n", toMiki("C A8 B8 C8 D8"));
	}
}
