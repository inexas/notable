package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BasicParserTest extends ParserTestAbc {

	@Test
	void testEmpty() {
		assertEquals("", toMiki(""));
	}

	@Test
	void testTitle() {
		assertEquals("title \"My work\"\n", toMiki("title \"My work\""));
		assertEquals("", toMiki("title\"\""));
	}

	@Test
	void testComposer() {
		assertEquals("composer \"abc\"\n", toMiki("composer \"abc\""));
	}

	@Test
	void testHeader() {
		assertEquals("header \"a\nb\"\n", toMiki("header \"a\nb\""));
	}

	@Test
	void testStructure1() {
		final Score score = toScore("C R R R|||");
		assertEquals(1, score.parts.size());
		final Part part = score.parts.getFirst();
		assertEquals("", part.name);
		assertEquals(1, part.phrases.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("", phrase.name);
//		assertEquals("time 4/4\n", score.timeSignature.toString());
//		assertNull(score.pickupMeasure);
//		assertEquals("tempo \"Andante\"\n", score.tempo.toString());
//		assertEquals("", score.key.toString());
	}

	@Test
	void testStructure2() {
		// Name the children
		final Score score = toScore("part \"p1\" phrase \"f1\" CCCC|||");
		assertEquals(1, score.parts.size());
		final Part part = score.parts.getFirst();
		assertEquals("p1", part.name);
		assertEquals(1, part.phrases.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("f1", phrase.name);
	}


	@Test
	void testFingering() {
		assertEquals("~2 C R R2 |||\n", toMiki("~2 C R R2|||"));
	}

	@Test
	void testText() {
		assertEquals("\"Text\" C R R2 |||\n", toMiki("\"Text\"C R R2|||"));
	}

	private void expectMessage(final String excerpt, final String toTest) {
		final MikiParser parser = MikiParser.fromString(toTest);
		assertTrue(parser.messages.contains(excerpt));
	}

	@Test
	void testArticulationsNotSupported() {
		expectMessage("articulation", "[t A B C].");
	}

	@Test
	void testTuplet() {
		assertEquals("[t A8* B C] A B R |||\n", toMiki("[t A8* B C] A B R|||"));
		assertEquals("[t A B C] A B C |||\n", toMiki("[t A B C] A B C|||"));
		assertEquals("[t A B C]8 R8 A B C |||\n", toMiki("[t A B C]8 R8 A B C|||"));
	}

	@Test
	void measureFilling() {
		errorExpected("underflow", "A B C|||");
		errorExpected("overflow", "A B C D2|||");
	}
}
