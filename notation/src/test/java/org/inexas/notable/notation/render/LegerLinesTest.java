package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LegerLinesTest {

	@Test
	void countLegerlines() {
		Staff staff = MikiParser.fromString(" + C").score.staff;
		assertEquals(Note.E4, staff.lowLinePosition);
		assertEquals(Note.F5, staff.highLinePosition);
		assertEquals(Note.C5, staff.lowestNote);
		assertEquals(Note.C5, staff.highestNote);

		staff = MikiParser.fromString("C o5 G").score.staff;
		assertEquals(Note.E4, staff.lowLinePosition);
		assertEquals(Note.F5, staff.highLinePosition);
		assertEquals(Note.C4, staff.lowestNote);
		assertEquals(Note.G5, staff.highestNote);
	}

	@Test
	void baselineAdjustment() {
		Staff staff;
		double[] yLookup;

		staff = MikiParser.fromString("E").score.staff;
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(0, yLookup[Note.E4]);

		staff = MikiParser.fromString("o5 G").score.staff;
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(10, yLookup[Note.E4]);

		staff = MikiParser.fromString("o5 B").score.staff;
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(30, yLookup[Note.E4]);
	}
}
