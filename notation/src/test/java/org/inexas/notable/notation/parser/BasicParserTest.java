package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BasicParserTest {
	private String toMiki(final String string) {
		final Score score = Parser.fromString(string).score;
		return score.toString();
	}

	private Score toScore(final String string) {
		return Parser.fromString(string).score;
	}

	@Test
	void quickTest() {
		assertEquals("C C C C", toMiki("C C C C"));
	}

	@Test
	void testEmpty() {
		assertEquals("", toMiki(""));
	}

	@Test
	void testTitle() {
		assertEquals("title \"My work\"\n\n", toMiki("title \"My work\""));
		assertEquals("", toMiki("title\"\""));
	}

	@Test
	void testComposer() {
		assertEquals("composer \"abc\"\n\n", toMiki("composer \"abc\""));
	}

	@Test
	void testHeader() {
		assertEquals("header \"a\nb\"\n\n", toMiki("header \"a\nb\""));
	}

	@Test
	void testStructure1() {
		final Score score = toScore("");
		assertEquals(1, score.partMap.size());
		final Part part = score.getFirstPart();
		assertEquals("#IMPLICIT#", part.name);
		assertEquals(1, part.phraseMap.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("#IMPLICIT#", phrase.name);
		assertEquals("time 4/4\n", score.timeSignature.toString());
		assertNull(score.pickupMeasure);
		assertEquals("tempo \"Andante\"\n", score.tempo.toString());
		assertEquals("", score.keySignature.toString());
		assertEquals("staff grand\n", score.staff.toString());
	}

	@Test
	void testStructure2() {
		// Name the children
		final Score score = toScore("part \"p1\" phrase \"f1\"");
		assertEquals(1, score.partMap.size());
		final Part part = score.getFirstPart();
		assertEquals("p1", part.name);
		assertEquals(1, part.phraseMap.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("f1", phrase.name);
	}

	@Test
	void testStructure3() {
		// Part:Phrase
		final Score score = toScore("phrase \"p1:f1\"");
		assertEquals(1, score.partMap.size());
		final Part part = score.getFirstPart();
		assertEquals("p1", part.name);
		assertEquals(1, part.phraseMap.size());
		final Phrase phrase = part.getFirstPhrase();
		assertEquals("f1", phrase.name);
	}

	@Test
	void testPickupMeasure() {
		assertEquals("pickup 1/4\n\n", toMiki("pickup 1/4"));
	}

	@Test
	void testAccidentals() {
		// [#bn]
		assertEquals("B Bb A# B ||\n", toMiki("B Bb A# Bn"));
		// todo Handle double sharps and flats
	}

	@Test
	void testConstructedChords() {
		assertEquals("[C E G] r r2 ||\n", toMiki("[CEG] "));
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
		assertEquals("[C]2 r2 ||\n", toMiki("[C]2"));
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
		assertEquals("C. r r2 ||\n", toMiki("C."));
		assertEquals("C. C.. C_ C! | C!! Cf Cg r ||\n", toMiki("C. C.. C_ C! C!! Cf Cg"));
		assertEquals("[C E G]. r r2 ||\n", toMiki("[CEG]."));
	}

	@Test
	void testBarlines() {
		// More to stop compiler warnings than anything else...
		assertEquals("|", Barline.bar.miki);
		assertEquals("|:", Barline.beginRepeat.miki);
		assertEquals(":|:", Barline.beginEndRepeat.miki);
		assertEquals(":|", Barline.endRepeat.miki);
		assertEquals("|-", Barline.thickThin.miki);
		assertEquals("-|", Barline.thinThick.miki);
		assertEquals("||", Barline.doubleBar.miki);
		assertEquals("|- A B C D -| E r r2 ||\n", toMiki("|- A B C D -| E"));
	}

	@Test
	void testDuration() {
		assertEquals("32", Duration.thirtySecond.miki);
		assertEquals("16", Duration.sixteenth.miki);
		assertEquals("16,", Duration.sixteenthDot.miki);
		assertEquals("8", Duration.eighth.miki);
		assertEquals("8,", Duration.eighthDot.miki);
		assertEquals("8,,", Duration.eighthDotDot.miki);
		assertEquals("4", Duration.quarter.miki);
		assertEquals("4,", Duration.quarterDot.miki);
		assertEquals("4,,", Duration.quarterDotDot.miki);
		assertEquals("2", Duration.half.miki);
		assertEquals("2,", Duration.halfDot.miki);
		assertEquals("2,,", Duration.halfDotDot.miki);
		assertEquals("1", Duration.whole.miki);
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

		assertEquals("fff C r r2 ||\n", toMiki("fff C"));
	}

	@Test
	void testFingering() {
		assertEquals("~2 C r r2 ||\n", toMiki("~2 C"));
	}

	@Test
	void testLines() {
		assertEquals("{b 2} C E G r ||\n", toMiki("{b 2} C E G"));
		assertEquals("{b 2} C E G r ||\n", toMiki("{bind 2} C E G"));
		assertEquals("{c 1.2} C E G r ||\n", toMiki("{cre 1.2} C E G"));
		assertEquals("{d 1.2} C E G r ||\n", toMiki("{decrescendo 1.2} C E G"));
		assertEquals("{o-8 2} C r r2 ||\n", toMiki("{o-8 2} C "));
		assertEquals("{o8 2} C r r2 ||\n", toMiki("{o8 2} C "));
		assertEquals("{p 2.2} C D A r ||\n", toMiki("{pedal 2.2} C D A"));
		assertEquals("{p 2.2} C D A r ||\n", toMiki("{pedal 2.2} C D A"));
		assertEquals("{r 2} C r r2 ||\n", toMiki("{rest 2} C"));
		assertEquals("{v1 2} C r r2 ||\n", toMiki("{volta1 2} C"));
	}

	@Test
	void testText() {
		assertEquals("\"Text\" C r r2 ||\n", toMiki("\"Text\"C"));
	}

	private void expectMessage(final String string, final String excerpt) {
		final Parser parser = Parser.fromString(string);
		assertTrue(parser.messages.containExcerpt(excerpt));
	}

	@Test
	void testArticulationsNotSupported() {
		expectMessage("[t A B C].", "articulation");
	}

	@Test
	void testTuplet() {
		assertEquals("[t A8* B C] A B r ||\n", toMiki("[t A8* B C] A B"));
		assertEquals("[t A B C] A B C ||\n", toMiki("[t A B C] A B C"));
		assertEquals("[t A B C]8 A8 B8 C8 r2 ||\n", toMiki("[t A B C]8 A B C"));
		assertEquals("[t A B C]8* A B C r2 ||\n", toMiki("[t A B C]8* A B C"));
	}


	@Test
	void testOverflow() {
		expectMessage("A B C [t A B C]2", "reduced");
	}
}