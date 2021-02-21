package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChordTests extends ParserTestAbc {
	@Test
	void quick() {
		assertEquals("[C E G] R R R |||\n", toMiki("[CEG] RRR|||"));
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
}
