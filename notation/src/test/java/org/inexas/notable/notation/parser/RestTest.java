package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class RestTest {
	private String toMiki(final String string) {
		final Score score = MikiParser.fromString(string).score;
		return score.toString();
	}

	@Test
	void simple() {
		assertEquals("C R R2 ||\n", toMiki("C"));
		assertEquals("C R R2 ||\n", toMiki("C RRR"));
		assertEquals("C16 R8. R R2 ||\n", toMiki("C16"));
		assertEquals("C16 R8. R R R8. C16 ||\n", toMiki("C16 R16 R8 R  R R8 R16 C16"));
		assertEquals("R2 R R8. C16 ||\n", toMiki("RRR R8. C16"));
		assertEquals("R8. C16 R R2 ||\n", toMiki("R8. C16"));
	}
}
