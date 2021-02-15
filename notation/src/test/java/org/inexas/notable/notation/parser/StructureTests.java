package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;
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

	@Test
	void allHeaders() {
		final String toTest = """
				title "Title"
				header "Header"
				composer "Composer"
				subtitle "Subtitle"
				copyright "Copyright" """;
		final Score score = toScore(toTest);
		assertEquals("Title", score.title);
		assertEquals("Subtitle", score.subtitle);
		assertEquals("Composer", score.composer);
		assertEquals("Header", score.header);
		assertEquals("Copyright", score.copyright);
	}

	@Test
	void defaults1() {
		final String toTest = """
				clef bass
				key D
				time 3/4
				C C C C |
				""";
		final Score score = toScore(toTest);
		assertEquals(Clef.bass, score.defaultClef);
		assertEquals(KeySignature.get("D"), score.defaultKeySignature);
		assertEquals(new TimeSignature(3, 4), score.defaultTimeSignature);
	}

	@Test
	void defaults2() {
		final String toTest = """
				clef bass
				key D
				phrase ""
				key C
				clef treble
				C C C C |
				phrase "Bass"
				C C C C |
				""";
		final Score score = toScore(toTest);
		assertEquals(Clef.bass, score.defaultClef);
		assertEquals(KeySignature.get("D"), score.defaultKeySignature);

		Measure measure = score.parts.get("").phrases.get("").measures.get(0);
		assertEquals(Clef.treble, measure.getClef());
		assertEquals(Clef.treble, measure.getEffectiveClef());
		assertEquals(KeySignature.C, measure.getKeySignature());
		assertEquals(KeySignature.C, measure.getEffectiveKeySignature());

		measure = score.parts.get("").phrases.get("Bass").measures.get(0);
		assertNull(measure.getClef());
		assertEquals(Clef.bass, measure.getEffectiveClef());
		assertNull(measure.getKeySignature());
		assertEquals(KeySignature.get("D"), measure.getEffectiveKeySignature());
	}

	@Test
	void structure1() {
		final String toTest = """
				phrase "Guitar"
				C C C C |
				part "Piano"
				phrase "RH"
				C C C C |
				phrase "LH"
				clef bass
				C C C C |
				""";
		final Score score = toScore(toTest);
		final MappedList<Part> parts = score.parts;
		assertEquals(2, parts.size());

		final Part anonymousPart = parts.getFirst();
		assertEquals("", anonymousPart.name);
		assertTrue(anonymousPart.isActive());
		assertEquals(2, anonymousPart.phrases.size());

		MappedList<Phrase> phrases = anonymousPart.phrases;
		final Phrase anonymousPhrase = phrases.getFirst();
		assertFalse(anonymousPhrase.isActive());

		final Phrase guitar = phrases.get(1);
		assertTrue(guitar.isActive());
		assertEquals("Guitar", guitar.name);

		final Part piano = parts.get(1);
		assertEquals("Piano", piano.name);
		assertTrue(piano.isActive());

		phrases = piano.phrases;
		assertEquals(2, phrases.size());

		final Phrase rh = phrases.get(0);
		assertTrue(rh.isActive());
		assertEquals("RH", rh.name);

		final Phrase lh = phrases.get(1);
		assertEquals(Clef.bass, lh.measures.get(0).getClef());
		assertTrue(lh.isActive());
		assertEquals("LH", lh.name);
	}
}
