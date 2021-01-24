package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LegerLinesTest {

	@Test
	void countLegerlines() {
		Staff staff = MikiParser.fromString(" + C").score.staff;
		assertEquals(Note.E4, staff.lowLineNumber);
		assertEquals(Note.F5, staff.highLineNumber);
		assertEquals(Note.C5, staff.lowestNote);
		assertEquals(Note.C5, staff.highestNote);

		staff = MikiParser.fromString("C o5 G").score.staff;
		assertEquals(Note.E4, staff.lowLineNumber);
		assertEquals(Note.F5, staff.highLineNumber);
		assertEquals(Note.C4, staff.lowestNote);
		assertEquals(Note.G5, staff.highestNote);
	}
}
