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
	void testDiatonicC() {
		// C not flats or sharps
		final KeySignature key = KeySignature.C;

		assertTrue(key.isDiatonic(Note.Bs));   // Enharmonic with C
		assertTrue(key.isDiatonic(Note.C));
		assertTrue(key.isDiatonic(Note.D));
		assertTrue(key.isDiatonic(Note.E));
		assertTrue(key.isDiatonic(Note.Fb));   // Enharmonic with E
		assertTrue(key.isDiatonic(Note.Es));    // Enharmonic with F
		assertTrue(key.isDiatonic(Note.F));
		assertTrue(key.isDiatonic(Note.G));
		assertTrue(key.isDiatonic(Note.A));
		assertTrue(key.isDiatonic(Note.B));
		assertTrue(key.isDiatonic(Note.Cb));   // Enharmonic with B

		assertFalse(key.isDiatonic(Note.Cs));
		assertFalse(key.isDiatonic(Note.Db));
		assertFalse(key.isDiatonic(Note.Ds));
		assertFalse(key.isDiatonic(Note.Eb));
		assertFalse(key.isDiatonic(Note.Fs));
		assertFalse(key.isDiatonic(Note.Gb));
		assertFalse(key.isDiatonic(Note.Gs));
		assertFalse(key.isDiatonic(Note.Ab));
		assertFalse(key.isDiatonic(Note.As));
		assertFalse(key.isDiatonic(Note.Bb));
	}

	@Test
	void testDiatonicD() {
		// D has sharps F, C
		final KeySignature key = KeySignature.get("D");

		assertTrue(key.isDiatonic(Note.D));
		assertTrue(key.isDiatonic(Note.E));
		assertTrue(key.isDiatonic(Note.Fb));
		assertTrue(key.isDiatonic(Note.Fs));
		assertTrue(key.isDiatonic(Note.Gb));
		assertTrue(key.isDiatonic(Note.G));
		assertTrue(key.isDiatonic(Note.A));
		assertTrue(key.isDiatonic(Note.B));
		assertTrue(key.isDiatonic(Note.Cb));
		assertTrue(key.isDiatonic(Note.Cs));
		assertTrue(key.isDiatonic(Note.Db));

		assertFalse(key.isDiatonic(Note.Ds));
		assertFalse(key.isDiatonic(Note.Eb));
		assertFalse(key.isDiatonic(Note.Es));
		assertFalse(key.isDiatonic(Note.F));
		assertFalse(key.isDiatonic(Note.Gs));
		assertFalse(key.isDiatonic(Note.Ab));
		assertFalse(key.isDiatonic(Note.As));
		assertFalse(key.isDiatonic(Note.Bb));
		assertFalse(key.isDiatonic(Note.Bs));
		assertFalse(key.isDiatonic(Note.C));
	}

	@Test
	void testDiatonicEb() {
		// Eb has flats B E
		final KeySignature key = KeySignature.get("Eb");

		assertTrue(key.isDiatonic(Note.Ds));
		assertTrue(key.isDiatonic(Note.Eb));
		assertTrue(key.isDiatonic(Note.Es));
		assertTrue(key.isDiatonic(Note.F));
		assertTrue(key.isDiatonic(Note.G));
		assertTrue(key.isDiatonic(Note.Gs));
		assertTrue(key.isDiatonic(Note.Ab));
		assertTrue(key.isDiatonic(Note.As));
		assertTrue(key.isDiatonic(Note.Bb));
		assertTrue(key.isDiatonic(Note.Bs));
		assertTrue(key.isDiatonic(Note.C));
		assertTrue(key.isDiatonic(Note.D));

		assertFalse(key.isDiatonic(Note.Cb));
		assertFalse(key.isDiatonic(Note.Cs));
		assertFalse(key.isDiatonic(Note.Db));
		assertFalse(key.isDiatonic(Note.Fb));
		assertFalse(key.isDiatonic(Note.Fs));
		assertFalse(key.isDiatonic(Note.Gb));
	}

	@Test
	void testDiatonicCs() {
		// Cs all sharps
		final KeySignature key = KeySignature.get("C#");

		assertTrue(key.isDiatonic(Note.Cs));
		assertTrue(key.isDiatonic(Note.Db));
		assertTrue(key.isDiatonic(Note.Ds));
		assertTrue(key.isDiatonic(Note.Eb));
		assertTrue(key.isDiatonic(Note.Es));
		assertTrue(key.isDiatonic(Note.F));
		assertTrue(key.isDiatonic(Note.Fs));
		assertTrue(key.isDiatonic(Note.Gb));
		assertTrue(key.isDiatonic(Note.Gs));
		assertTrue(key.isDiatonic(Note.Ab));
		assertTrue(key.isDiatonic(Note.As));
		assertTrue(key.isDiatonic(Note.Bb));
		assertTrue(key.isDiatonic(Note.Bs));
		assertTrue(key.isDiatonic(Note.C));

		assertFalse(key.isDiatonic(Note.Cb));
		assertFalse(key.isDiatonic(Note.D));
		assertFalse(key.isDiatonic(Note.E));
		assertFalse(key.isDiatonic(Note.Fb));
		assertFalse(key.isDiatonic(Note.G));
		assertFalse(key.isDiatonic(Note.A));
		assertFalse(key.isDiatonic(Note.B));
	}

	@Test
	void testDiatonicCb() {
		// Cb all flats
		final KeySignature key = KeySignature.get("Cb");

		assertTrue(key.isDiatonic(Note.B));
		assertTrue(key.isDiatonic(Note.Cb));
		assertTrue(key.isDiatonic(Note.Cs));
		assertTrue(key.isDiatonic(Note.Db));
		assertTrue(key.isDiatonic(Note.Ds));
		assertTrue(key.isDiatonic(Note.Eb));
		assertTrue(key.isDiatonic(Note.E));
		assertTrue(key.isDiatonic(Note.Fb));
		assertTrue(key.isDiatonic(Note.Fs));
		assertTrue(key.isDiatonic(Note.Gb));
		assertTrue(key.isDiatonic(Note.Gs));
		assertTrue(key.isDiatonic(Note.Ab));
		assertTrue(key.isDiatonic(Note.As));
		assertTrue(key.isDiatonic(Note.Bb));

		assertFalse(key.isDiatonic(Note.C));
		assertFalse(key.isDiatonic(Note.D));
		assertFalse(key.isDiatonic(Note.Es));
		assertFalse(key.isDiatonic(Note.F));
		assertFalse(key.isDiatonic(Note.G));
		assertFalse(key.isDiatonic(Note.A));
		assertFalse(key.isDiatonic(Note.Bs));
	}

	@Test
	void yLookup() {
		Staff staff = new Staff(Staff.Type.treble, KeySignature.C);

		double[] yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(300, yLookup[0]);
		assertEquals(300, yLookup[1]);
		assertEquals(290, yLookup[2]);

		yLookup = KeySignature.C.yLookup(staff, 100, 10);
		assertEquals(400, yLookup[0]);
		assertEquals(400, yLookup[1]);
		assertEquals(390, yLookup[2]);

		staff = new Staff(Staff.Type.treble, KeySignature.get("D"));
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(300, yLookup[0]);
		assertEquals(300, yLookup[1]);
		assertEquals(290, yLookup[2]);
	}

	@Test
	void testNormalize() {
		final KeySignature keyC = KeySignature.get("C");
		assertEquals(Note.C4, keyC.normalize(Note.C4));
		assertEquals(Note.D1, keyC.normalize(Note.D1));
		assertEquals(Note.B3, keyC.normalize(Note.B3));
		assertThrows(AssertionError.class, () -> keyC.normalize(Note.Cs));

		final KeySignature keyA = KeySignature.get("A");
		assertEquals(Note.A3, keyA.normalize(Note.A3));
		assertEquals(Note.Fs4, keyA.normalize(Note.F4));
		assertEquals(Note.Cs4, keyA.normalize(Note.C4));
		assertEquals(Note.Gs4, keyA.normalize(Note.G4));
	}
}
