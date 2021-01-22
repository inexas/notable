package org.inexas.notable.notation.parser;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BarAnnotationTest {
	private String process(final String string) {
		final MikiParser parser = MikiParser.fromString(string);
		if(parser.messages.hasMessages()) {
			System.out.println(parser.messages);
			throw new RuntimeException("Got unexpected messages");
		}
		return parser.toString();
	}

	@Test
	void empty() {
		assertEquals("", process(""));
	}

	@Test
	void endBarline() {
		assertEquals("C R R2 ||\n", process("C|"));
	}

	@Test
	void lessThanOneMeasure() {
		assertEquals("C R R2 ||\n", process("C"));
		assertEquals("C R R2 ||\n", process("|C"));
		assertEquals("C R R2 ||\n", process("|C|"));
		assertEquals("C R R2 ||\n", process("|C||"));
	}

	@Test
	void oneMeasure() {
		assertEquals("C R R2 ||\n", process("C R R2 "));
		assertEquals("C1 ||\n", process("C1"));
		assertEquals("C C C C ||\n", process("C C C C"));
		assertEquals("C C C C ||\n", process("C C C C|"));
	}

	@Test
	void twoMeasures() {
		assertEquals("C C C C | C R R2 ||\n", process("C C C C | C"));
		assertEquals("C1 | C1 ||\n", process("C1|C1"));
		assertEquals("C1 | C1 ||\n", process("C1|C1|"));
	}

	@Test
	void fiveMeasures() {
		assertEquals("C1 | C1 | C1 | C1 |\nC1 ||\n", process("C1|C1|C1|C1|C1"));
	}
}
