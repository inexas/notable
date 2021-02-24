package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClefTest {

	@Test
	void countLegerLines1() {
		Assertions.assertEquals(Notes.E4, Clef.treble.lowSlot);
		assertEquals(Notes.F5, Clef.treble.highSlot);
		assertEquals(Notes.G2, Clef.bass.lowSlot);
		assertEquals(Notes.A3, Clef.bass.highSlot);
	}

	@Test
	void countLegerLines() {
		final Clef clef = Clef.treble;

		// Above...
		assertEquals(2, clef.countLedgerLines(Notes.C6));
		assertEquals(1, clef.countLedgerLines(Notes.B5));
		assertEquals(1, clef.countLedgerLines(Notes.A5));
		assertEquals(0, clef.countLedgerLines(Notes.G5));

		// Within
		assertEquals(0, clef.countLedgerLines(Notes.F5));
		assertEquals(0, clef.countLedgerLines(Notes.E4));

		// Below...
		assertEquals(0, clef.countLedgerLines(Notes.D4));
		assertEquals(-1, clef.countLedgerLines(Notes.C4));
		assertEquals(-1, clef.countLedgerLines(Notes.B3));
		assertEquals(-2, clef.countLedgerLines(Notes.A3));
	}
}
