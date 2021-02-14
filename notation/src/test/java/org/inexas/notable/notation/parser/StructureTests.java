package org.inexas.notable.notation.parser;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StructureTests {

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
			assertTrue(found);
		}
	}

	@Test
	void testAnonymous() {
		errorExpected("Measure not complete", "");
		errorExpected("Measure not complete", "A");
		final MikiParser parser = MikiParser.fromString("ARRR|");
		assertFalse(parser.messages.hasMessages());
	}
}
