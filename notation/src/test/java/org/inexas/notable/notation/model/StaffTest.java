package org.inexas.notable.notation.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {

	@Test
	void countLegerlines() {
		final Staff staff = Staff.trebleC;
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
	void positionsAbove() {
		final Staff staff = Staff.trebleC;

		// No notes yet...
		assertEquals(0, staff.positionsAbove());
		assertEquals(0, staff.positionsBelow());

		// Notes only in staff...
		staff.accountFor(Note.E4);
		assertEquals(0, staff.positionsAbove());
		assertEquals(0, staff.positionsBelow());

		// Margin above...
		staff.accountFor(Note.G5);
		assertEquals(10, staff.positionsAbove());
		staff.accountFor(Note.A5);
		assertEquals(20, staff.positionsAbove());
		assertEquals(0, staff.positionsBelow());

		// Margin below
		staff.accountFor(Note.D4);
		assertEquals(10, staff.positionsBelow());
		staff.accountFor(Note.C4);
		assertEquals(20, staff.positionsBelow());
		staff.accountFor(Note.B3);
		assertEquals(30, staff.positionsBelow());
	}
}
