package org.inexas.notable.notation.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClefTest {

	@Test
	void basics() {
		final Clef clef = new Clef(Clef.Type.treble);
		final Clef.Type type = clef.type;

		assertEquals(Clef.Type.treble, type);
	}

	@SuppressWarnings("ObviousNullCheck")
	@Test
	void types() {
		final Clef clef = new Clef(Clef.Type.treble);
		assertNotNull(new Clef(Clef.Type.alto));
		assertNotNull(new Clef(Clef.Type.bass));
		assertNotNull(new Clef(Clef.Type.tenor));
		assertNotNull(new Clef(Clef.Type.treble));
	}

//	@Test
//	void countLegerLines1() {
//		Clef clef = MikiParser.fromString(" + C").score.clef;
//		assertEquals(Notes.E4, clef.type.lowSlot);
//		assertEquals(Notes.F5, clef.type.highSlot);
//		assertEquals(Notes.C5, clef.type.lowSlot);
//		assertEquals(Notes.C5, clef.type.highSlot);
//
//		clef = MikiParser.fromString("C o5 G").score.clef;
//		assertEquals(Notes.E4, clef.type.lowSlot);
//		assertEquals(Notes.F5, clef.type.highSlot);
//		assertEquals(Notes.C4, clef.type.lowSlot);
//		assertEquals(Notes.G5, clef.type.highSlot);
//	}

	@Test
	void countLegerLines2() {
		final Clef clef = new Clef(Clef.Type.treble);
		//clef.accountFor(Notes.E4);

		// Above...
		assertEquals(2, clef.countLedgerLines(Notes.C6));
		assertEquals(1, clef.countLedgerLines(Notes.B5));
		assertEquals(1, clef.countLedgerLines(Notes.A5));
		assertEquals(0, clef.countLedgerLines(Notes.G5));
		assertEquals(0, clef.countLedgerLines(Notes.F5));


		// Below...
		assertEquals(0, clef.countLedgerLines(Notes.E4));
		assertEquals(0, clef.countLedgerLines(Notes.D4));
		assertEquals(-1, clef.countLedgerLines(Notes.C4));
		assertEquals(-1, clef.countLedgerLines(Notes.B3));
		assertEquals(-2, clef.countLedgerLines(Notes.A3));
	}

	// fixme
//	@Test
//	void slotsAbove() {
//		final Clef clef = new Clef(Clef.Type.treble);
//
//		// No notes yet...
//		assertEquals(0, clef.slotsAbove());
//		assertEquals(0, clef.slotsBelow());
//
//		// Notes only in clef...
//		clef.accountFor(Notes.E4);
//		assertEquals(0, clef.slotsAbove());
//		assertEquals(0, clef.slotsBelow());
//
//		// Margin above...
//		clef.accountFor(Notes.G5);
//		assertEquals(1, clef.slotsAbove());
//		clef.accountFor(Notes.A5);
//		assertEquals(2, clef.slotsAbove());
//		assertEquals(0, clef.slotsBelow());
//
//		// Margin below
//		clef.accountFor(Notes.D4);
//		assertEquals(1, clef.slotsBelow());
//		clef.accountFor(Notes.C4);
//		assertEquals(2, clef.slotsBelow());
//		clef.accountFor(Notes.B3);
//		assertEquals(3, clef.slotsBelow());
//	}
//
//	@Test
//	void accidentals() {
//		final Clef clef = new Clef(Clef.Type.treble);
//		clef.accountFor(Notes.B3);
//		assertEquals(3, clef.slotsBelow());
//	}
}
