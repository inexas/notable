package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {

	@Test
	void basics() {
		final Staff staff = new Staff(Staff.Type.treble);
		final Staff.Type type = staff.type;

		assertEquals(Staff.Type.treble, type);
		assertEquals(type.low, staff.low);
		assertEquals(type.high, staff.high);

		assertEquals(-1, staff.lowestNote);
		assertEquals(-1, staff.highestNote);
	}

	@Test
	void types() {
		final Staff staff = new Staff(Staff.Type.treble);
		assertNotNull(new Staff(Staff.Type.alto));
		assertNotNull(new Staff(Staff.Type.bass));
		assertNotNull(new Staff(Staff.Type.tenor));
		assertNotNull(new Staff(Staff.Type.treble));
	}

	@Test
	void countLegerLines1() {
		Staff staff = MikiParser.fromString(" + C").score.staff;
		assertEquals(Note.E4, staff.low);
		assertEquals(Note.F5, staff.high);
		assertEquals(Note.C5, staff.lowestNote);
		assertEquals(Note.C5, staff.highestNote);

		staff = MikiParser.fromString("C o5 G").score.staff;
		assertEquals(Note.E4, staff.low);
		assertEquals(Note.F5, staff.high);
		assertEquals(Note.C4, staff.lowestNote);
		assertEquals(Note.G5, staff.highestNote);
	}

	@Test
	void countLegerLines2() {
		final Staff staff = new Staff(Staff.Type.treble);
		staff.accountFor(Note.E4);

		// Above...
		assertEquals(2, staff.countLedgerLines(Note.C6));
		assertEquals(1, staff.countLedgerLines(Note.B5));
		assertEquals(1, staff.countLedgerLines(Note.A5));
		assertEquals(0, staff.countLedgerLines(Note.G5));
		assertEquals(0, staff.countLedgerLines(Note.F5));


		// Below...
		assertEquals(0, staff.countLedgerLines(Note.E4));
		assertEquals(0, staff.countLedgerLines(Note.D4));
		assertEquals(-1, staff.countLedgerLines(Note.C4));
		assertEquals(-1, staff.countLedgerLines(Note.B3));
		assertEquals(-2, staff.countLedgerLines(Note.A3));
	}

	@Test
	void slotsAbove() {
		final Staff staff = new Staff(Staff.Type.treble);

		// No notes yet...
		assertEquals(0, staff.slotsAbove());
		assertEquals(0, staff.slotsBelow());

		// Notes only in staff...
		staff.accountFor(Note.E4);
		assertEquals(0, staff.slotsAbove());
		assertEquals(0, staff.slotsBelow());

		// Margin above...
		staff.accountFor(Note.G5);
		assertEquals(1, staff.slotsAbove());
		staff.accountFor(Note.A5);
		assertEquals(2, staff.slotsAbove());
		assertEquals(0, staff.slotsBelow());

		// Margin below
		staff.accountFor(Note.D4);
		assertEquals(1, staff.slotsBelow());
		staff.accountFor(Note.C4);
		assertEquals(2, staff.slotsBelow());
		staff.accountFor(Note.B3);
		assertEquals(3, staff.slotsBelow());
	}

	@Test
	void accidentals() {
		final Staff staff = new Staff(Staff.Type.treble);
		staff.accountFor(Note.B3);
		assertEquals(3, staff.slotsBelow());
	}
}
