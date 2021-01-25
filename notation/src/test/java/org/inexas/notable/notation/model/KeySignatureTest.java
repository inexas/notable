package org.inexas.notable.notation.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class KeySignatureTest {
	@Test
	void testMap() {
		assertEquals("C", KeySignature.get("C").name);
		assertEquals("Cb", KeySignature.get("Cb").name);
		assertEquals("C", KeySignature.C.name);
	}

	@Test
	void testSharpFlat() {
		assertFalse(KeySignature.get("C").isSharp());
		assertFalse(KeySignature.get("C").isFlat());

		assertTrue(KeySignature.get("C#").isSharp());
		assertTrue(KeySignature.get("Cb").isFlat());
	}

	@Test
	void yLookup() {
		Staff staff = new Staff(Staff.Type.treble, KeySignature.C);

		double[] yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(300, yLookup[0]);
		assertEquals(290, yLookup[1]);
		assertEquals(280, yLookup[2]);

		yLookup = KeySignature.C.yLookup(staff, 100, 10);
		assertEquals(400, yLookup[0]);
		assertEquals(390, yLookup[1]);
		assertEquals(380, yLookup[2]);

		staff = new Staff(Staff.Type.treble, KeySignature.get("D"));
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(300, yLookup[0]);
		assertEquals(290, yLookup[1]);
		assertEquals(280, yLookup[2]);
		assertEquals(0, yLookup[Note.E4]);
	}

	@Test
	void yLookup1() {
		final Staff staff = new Staff(Staff.Type.treble, KeySignature.C);

		final double[] yLookup = KeySignature.C.yLookup(staff, 120, 5);
		assertEquals(80, yLookup[Note.F5]);
		assertEquals(120, yLookup[Note.E4]);
	}
}
