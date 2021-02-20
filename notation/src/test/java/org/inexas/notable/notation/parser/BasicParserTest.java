package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

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
		final Score score = toScore("part \"p1\" phrase \"f1\"");
		assertEquals(1, score.parts.size());
		final Part part = score.parts.getFirst();
		assertEquals("p1", part.name);
		assertEquals(1, part.phrases.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("f1", phrase.name);
	}

	@Test
	void testStructure3() {
		// Part:Phrase
		final Score score = toScore("phrase \"p1:f1\"");
		assertEquals(1, score.parts.size());
		final Part part = score.parts.getFirst();
		assertEquals("p1", part.name);
		assertEquals(1, part.phrases.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("f1", phrase.name);
	}

	@Test
	void testAccidentals() {
		// [#bn]
		assertEquals("B Bb A# Bn |||\n", toMiki("B Bb A# Bn |||"));
	}

	@Test
	void testConstructedChords() {
		assertEquals("[C E G] R R2 |||\n", toMiki("[CEG] R R2 |||"));
	}

	@Test
	void testNamedChord() {
		final NamedChord namedChord = NamedChord.parse(
				"[Abm-7add2sus4i3]2*.",
				Duration.quarter,
				new HashMap<>());
		assertEquals("[Abm-7add2sus4i3]", namedChord.name);
		assertEquals(Duration.half, namedChord.duration);
		assertEquals(1, namedChord.annotations.size());
		assertEquals(Articulation.staccato, namedChord.annotations.get(Articulation.class));
		assertEquals('A', namedChord.tonic);
		assertEquals('m', namedChord.mode);
		assertEquals('-', namedChord.dimAug);
		assertTrue(namedChord.seventh);
		assertEquals(2, namedChord.add);
		assertEquals(4, namedChord.sus);
		assertEquals(3, namedChord.inversion);

		final NamedChord namedChord1 = NamedChord.parse(
				"[A]",
				Duration.half,
				new HashMap<>());
		assertEquals("[A]", namedChord1.name);
	}

	@Test
	void testNamedChords() {
		assertEquals("[C]2 R2 |||\n", toMiki("[C]2 R2|||"));
	}

	@Test
	void testArticulations() {
		assertEquals("f", Articulation.fermata.miki);
		assertEquals("g", Articulation.glissando.miki);
		assertEquals("", Articulation.legato.miki);
		assertEquals("!", Articulation.marcato.miki);
		assertEquals("!!", Articulation.marcatissimo.miki);
		assertEquals(".", Articulation.staccato.miki);
		assertEquals("..", Articulation.staccatissimo.miki);
		assertEquals("_", Articulation.tenuto.miki);
		assertEquals(".", Articulation.getByName("staccato").miki);
		assertNotNull(Articulation.getByName("staccato").description);

		// [._!fg]+
		assertEquals("C. R R2 |||\n", toMiki("C. R R2|||"));
		assertEquals("", toMiki("C. C.. C_ C! | C!! Cf Cg C |||"));
		assertEquals("[C E G]. R R2 |||\n", toMiki("[CEG]. R R2|||"));
	}

	@Test
	void testDynamic() {
		assertEquals("fff", Dynamic.fff.name);
		assertEquals("ff", Dynamic.ff.name);
		assertEquals("f", Dynamic.f.name);
		assertEquals("mf", Dynamic.mf.name);
		assertEquals("mp", Dynamic.mp.name);
		assertEquals("p", Dynamic.p.name);
		assertEquals("pp", Dynamic.pp.name);
		assertEquals("ppp", Dynamic.ppp.name);

		assertEquals("fff C R R2 |||\n", toMiki("fff C"));
	}

	@Test
	void testFingering() {
		assertEquals("~2 C R R2 |||\n", toMiki("~2 C R R2|||"));
	}

	@Test
	void testLines() {
		assertEquals("{b 2} C E G R |||\n", toMiki("{b 2} C E G R|||"));
		assertEquals("{b 2} C E G R |||\n", toMiki("{bind 2} C E G R|||"));
		assertEquals("{c 1.2} C E G R |||\n", toMiki("{cre 1.2} C E G R|||"));
		assertEquals("{d 1.2} C E G R |||\n", toMiki("{decrescendo 1.2} C E G R|||"));
		assertEquals("{o-8 2} C R R2 |||\n", toMiki("{o-8 2} C R R2 |||"));
		assertEquals("{o8 2} C R R2 |||\n", toMiki("{o8 2} C R R2 |||"));
		assertEquals("{p 2.2} C D A R |||\n", toMiki("{pedal 2.2} C D A R|||"));
		assertEquals("{p 2.2} C D A R |||\n", toMiki("{pedal 2.2} C D A R|||"));
		assertEquals("{r 2} C R R2 |||\n", toMiki("{rest 2} C R R2 |||"));
		assertEquals("{v1 2} C R R2 |||\n", toMiki("{volta1 2} C R R2 |||"));
	}

	@Test
	void testText() {
		assertEquals("\"Text\" C R R2 |||\n", toMiki("\"Text\"C R R2|||"));
	}

	private void expectMessage(final String string, final String excerpt) {
		final MikiParser parser = MikiParser.fromString(string);
		assertTrue(parser.messages.contains(excerpt));
	}

	@Test
	void testArticulationsNotSupported() {
		expectMessage("[t A B C].", "articulation");
	}

	@Test
	void testTuplet() {
		assertEquals("[t A8* B C] A B R |||\n", toMiki("[t A8* B C] A B R|||"));
		assertEquals("[t A B C] A B C |||\n", toMiki("[t A B C] A B C|||"));
		assertEquals("[t A B C]8* A (B C) R2 |||\n", toMiki("[t A B C]8* A B C|||"));
		assertEquals("[t A B C]8 A B C R8 |||\n", toMiki("[t A B C]8 A B C R8|||"));
	}


	@Test
	void testOverflow() {
		expectMessage("A B C [t A B C]2", "reduced");
	}
}
