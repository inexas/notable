package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class ParserTestAbc {
	/**
	 * Use this method when you expect no messages
	 *
	 * @param toTest miki to parse
	 * @return The parsed Score
	 */
	Score toScore(final String toTest) {
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


	/**
	 * Use this method when you expect no messages
	 *
	 * @param toTest miki to parse
	 * @return The parsed Score converted back into miki
	 */
	String toMiki(final String toTest) {
		return toScore(toTest).toString();
	}

	/**
	 * Use this method when you expect at least one error message.
	 * Warnings are ignored
	 *
	 * @param toTest miki to parse
	 */
	void errorExpected(final String expected, final String toTest) {
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

	/**
	 * Use this method when you expect no error messages but
	 * at least one warning
	 *
	 * @param toTest miki to parse
	 */
	String warningExpected(final String expected, final String toTest) {
		final MikiParser parser = MikiParser.fromString(toTest);
		final Messages messages = parser.messages;
		boolean found = false;
		assertEquals(0, messages.getErrorCount());
		final int count = messages.getWarningCount();
		for(int i = 0; i < count; i++) {
			if(messages.getWarning(i).contains(expected)) {
				found = true;
				break;
			}
		}
		if(!found) {
			System.out.println("Searching for: " + expected);
			System.out.println(messages);
			assertTrue(count >= 0); // Force error
		}
		return parser.score.toString();
	}
}
