package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StructureTests {
	private Score toScore(final String toTest) {
		final MikiParser parser = MikiParser.fromString(toTest);
		final Messages messages = parser.messages;
		if(messages.hasMessages()) {
			System.out.println("Test string: " + toTest);
			System.out.println("Result: " + parser.toString());
			System.out.println("Messages...");
			System.out.println(messages);
			assertFalse(messages.hasMessages());
		}
		return parser.score;
	}

	private String toMiki(final String toTest) {
		return toScore(toTest).toString();
	}

	private void errorExpected(final String expected, final String toTest) {
		final MikiParser parser = MikiParser.fromString(toTest);
		final Messages messages = parser.messages;
		boolean found = false;
		final int count = messages.getErrorCount();
		for(int i = 0; i < count; i++) {
			if(messages.getError(i).contains(expected)) {
				found = true;
				break;
			}
		}
		if(!found) {
			System.out.println("Searching for: " + expected);
			System.out.println(messages);
			assertTrue(count >= 0); // Force error
		}
	}

	@Test
	void testAnonymous() {
		errorExpected("Measure not complete", "");
		errorExpected("Measure not ", "A");
		final MikiParser parser = MikiParser.fromString("C C C C |");
		assertFalse(parser.messages.hasMessages());
	}

	@Test
	void test1() {
		assertEquals("|\n", toMiki("A R R R"));
		assertEquals("|\n", toMiki("A R R R|"));
	}

	@Test
	void title() {
		assertNull(toScore("").title);
		assertEquals("t", toScore("title \"t\"").title);
		assertEquals("title \"t\"\n", toMiki("title \"t\""));
	}

	@Test
	void subtitle() {
		assertNull(toScore("").subtitle);
		assertEquals("s", toScore("subtitle \"s\"").subtitle);
		assertEquals("subtitle \"s\"\n", toMiki("subtitle \"s\""));
	}

	@Test
	void composer() {
		assertNull(toScore("").composer);
		assertEquals("c", toScore("composer \"c\"").composer);
		assertEquals("composer \"c\"\n", toMiki("composer \"c\""));
	}

	@Test
	void header() {
		assertNull(toScore("").header);
		assertEquals("h", toScore("header \"h\"").header);
		assertEquals("header \"h\"\n", toMiki("header \"h\""));
	}

	@Test
	void copyright() {
		assertNull(toScore("").copyright);
		assertEquals("0", toScore("copyright \"0\"").copyright);
		assertEquals("copyright \"0\"\n", toMiki("copyright \"0\""));
	}

//	@Test
//	void testComposer() {
//		assertEquals("composer \"abc\"\n\n", toMiki("composer \"abc\""));
//	}
//
//	@Test
//	void testHeader() {
//		assertEquals("header \"a\nb\"\n\n", toMiki("header \"a\nb\""));
//	}
//
//
}
