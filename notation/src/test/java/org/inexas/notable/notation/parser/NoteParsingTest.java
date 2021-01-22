package org.inexas.notable.notation.parser;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NoteParsingTest {
	private String process(final String string) {
		final Parser parser = Parser.fromString(string);
		if(parser.messages.hasMessages()) {
			System.out.println(parser.messages);
			throw new RuntimeException("Errors");
		}
		return parser.toString();
	}

	@Test
	void quickTest() {
		assertEquals("C C C C", process("" +
				"// This is a the same piece but with a bit more stuff!\n" +
				"title \"Baa, Baa, Black Sheep\"\n" +
				"composer \"Unknown\"\n" +
				"header \"Baa, Baa, Black Sheep is an English nursery rhyme, the " +
				"earliest printed version of which dates from around 1744.\"\n" +
				"\n" +
				"staff treble\n" +
				"key C\n" +
				"time 4/4\n" +
				"tempo 1/4=80  // This means the quarter (1/4) note is 80 BPM\n" +
				"\n" +
				"part \"Piano\"\n" +
				"\n" +
				"C C G G | A8* B C A G2 | F4* F E E | D D C2\n"));
	}
}
