package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantConditions")
public class KeySignatureTest {
	@Test
	void testMap() {
		assertEquals("C", KeySignature.map.get("C").name);
		assertEquals("Cb", KeySignature.map.get("Cb").name);
		assertEquals("C", KeySignature.C.name);
	}

	@Test
	void testSharpFlat() {
		assertFalse(KeySignature.map.get("C").isSharp());
		assertFalse(KeySignature.map.get("C").isFlat());

		assertTrue(KeySignature.map.get("C#").isSharp());
		assertTrue(KeySignature.map.get("Cb").isFlat());
	}

	@Test
	void testDiatonicC() {
		// C not flats or sharps
		final KeySignature key = KeySignature.C;

		assertTrue(key.isDiatonic[Note.Bs]);   // Enharmonic with C
		assertTrue(key.isDiatonic[Note.C]);
		assertTrue(key.isDiatonic[Note.D]);
		assertTrue(key.isDiatonic[Note.E]);
		assertTrue(key.isDiatonic[Note.Fb]);   // Enharmonic with E
		assertTrue(key.isDiatonic[Note.Es]);    // Enharmonic with F
		assertTrue(key.isDiatonic[Note.F]);
		assertTrue(key.isDiatonic[Note.G]);
		assertTrue(key.isDiatonic[Note.A]);
		assertTrue(key.isDiatonic[Note.B]);
		assertTrue(key.isDiatonic[Note.Cb]);   // Enharmonic with B

		assertFalse(key.isDiatonic[Note.Cs]);
		assertFalse(key.isDiatonic[Note.Db]);
		assertFalse(key.isDiatonic[Note.Ds]);
		assertFalse(key.isDiatonic[Note.Eb]);
		assertFalse(key.isDiatonic[Note.Fs]);
		assertFalse(key.isDiatonic[Note.Gb]);
		assertFalse(key.isDiatonic[Note.Gs]);
		assertFalse(key.isDiatonic[Note.Ab]);
		assertFalse(key.isDiatonic[Note.As]);
		assertFalse(key.isDiatonic[Note.Bb]);
	}

	@Test
	void testDiatonicD() {
		// D has sharps F, C
		final KeySignature key = KeySignature.map.get("D");

		assertTrue(key.isDiatonic[Note.D]);
		assertTrue(key.isDiatonic[Note.E]);
		assertTrue(key.isDiatonic[Note.Fb]);
		assertTrue(key.isDiatonic[Note.Fs]);
		assertTrue(key.isDiatonic[Note.Gb]);
		assertTrue(key.isDiatonic[Note.G]);
		assertTrue(key.isDiatonic[Note.A]);
		assertTrue(key.isDiatonic[Note.B]);
		assertTrue(key.isDiatonic[Note.Cb]);
		assertTrue(key.isDiatonic[Note.Cs]);
		assertTrue(key.isDiatonic[Note.Db]);

		assertFalse(key.isDiatonic[Note.Ds]);
		assertFalse(key.isDiatonic[Note.Eb]);
		assertFalse(key.isDiatonic[Note.Es]);
		assertFalse(key.isDiatonic[Note.F]);
		assertFalse(key.isDiatonic[Note.Gs]);
		assertFalse(key.isDiatonic[Note.Ab]);
		assertFalse(key.isDiatonic[Note.As]);
		assertFalse(key.isDiatonic[Note.Bb]);
		assertFalse(key.isDiatonic[Note.Bs]);
		assertFalse(key.isDiatonic[Note.C]);
	}

	@Test
	void testDiatonicEb() {
		// Eb has flats B E
		final KeySignature key = KeySignature.map.get("Eb");

		assertTrue(key.isDiatonic[Note.Ds]);
		assertTrue(key.isDiatonic[Note.Eb]);
		assertTrue(key.isDiatonic[Note.Es]);
		assertTrue(key.isDiatonic[Note.F]);
		assertTrue(key.isDiatonic[Note.G]);
		assertTrue(key.isDiatonic[Note.Gs]);
		assertTrue(key.isDiatonic[Note.Ab]);
		assertTrue(key.isDiatonic[Note.As]);
		assertTrue(key.isDiatonic[Note.Bb]);
		assertTrue(key.isDiatonic[Note.Bs]);
		assertTrue(key.isDiatonic[Note.C]);
		assertTrue(key.isDiatonic[Note.D]);

		assertFalse(key.isDiatonic[Note.Cb]);
		assertFalse(key.isDiatonic[Note.Cs]);
		assertFalse(key.isDiatonic[Note.Db]);
		assertFalse(key.isDiatonic[Note.Fb]);
		assertFalse(key.isDiatonic[Note.Fs]);
		assertFalse(key.isDiatonic[Note.Gb]);
	}

	@Test
	void testDiatonicCs() {
		// Cs all sharps
		final KeySignature key = KeySignature.map.get("C#");

		assertTrue(key.isDiatonic[Note.Cs]);
		assertTrue(key.isDiatonic[Note.Db]);
		assertTrue(key.isDiatonic[Note.Ds]);
		assertTrue(key.isDiatonic[Note.Eb]);
		assertTrue(key.isDiatonic[Note.Es]);
		assertTrue(key.isDiatonic[Note.F]);
		assertTrue(key.isDiatonic[Note.Fs]);
		assertTrue(key.isDiatonic[Note.Gb]);
		assertTrue(key.isDiatonic[Note.Gs]);
		assertTrue(key.isDiatonic[Note.Ab]);
		assertTrue(key.isDiatonic[Note.As]);
		assertTrue(key.isDiatonic[Note.Bb]);
		assertTrue(key.isDiatonic[Note.Bs]);
		assertTrue(key.isDiatonic[Note.C]);

		assertFalse(key.isDiatonic[Note.Cb]);
		assertFalse(key.isDiatonic[Note.D]);
		assertFalse(key.isDiatonic[Note.E]);
		assertFalse(key.isDiatonic[Note.Fb]);
		assertFalse(key.isDiatonic[Note.G]);
		assertFalse(key.isDiatonic[Note.A]);
		assertFalse(key.isDiatonic[Note.B]);
	}

	@Test
	void testDiatonicCb() {
		// Cb all flats
		final KeySignature key = KeySignature.map.get("Cb");

		assertTrue(key.isDiatonic[Note.B]);
		assertTrue(key.isDiatonic[Note.Cb]);
		assertTrue(key.isDiatonic[Note.Cs]);
		assertTrue(key.isDiatonic[Note.Db]);
		assertTrue(key.isDiatonic[Note.Ds]);
		assertTrue(key.isDiatonic[Note.Eb]);
		assertTrue(key.isDiatonic[Note.E]);
		assertTrue(key.isDiatonic[Note.Fb]);
		assertTrue(key.isDiatonic[Note.Fs]);
		assertTrue(key.isDiatonic[Note.Gb]);
		assertTrue(key.isDiatonic[Note.Gs]);
		assertTrue(key.isDiatonic[Note.Ab]);
		assertTrue(key.isDiatonic[Note.As]);
		assertTrue(key.isDiatonic[Note.Bb]);

		assertFalse(key.isDiatonic[Note.C]);
		assertFalse(key.isDiatonic[Note.D]);
		assertFalse(key.isDiatonic[Note.Es]);
		assertFalse(key.isDiatonic[Note.F]);
		assertFalse(key.isDiatonic[Note.G]);
		assertFalse(key.isDiatonic[Note.A]);
		assertFalse(key.isDiatonic[Note.Bs]);
	}

	@Test
	void yLookup() {
		Staff staff = new Staff("test1", 0);

		double[] yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(0, yLookup[0]);
		assertEquals(0, yLookup[1]);
		assertEquals(-10, yLookup[2]);

		yLookup = KeySignature.C.yLookup(staff, 100, 10);
		assertEquals(100, yLookup[0]);
		assertEquals(100, yLookup[1]);
		assertEquals(90, yLookup[2]);

		staff = new Staff("test2", 2);
		yLookup = KeySignature.C.yLookup(staff, 0, 10);
		assertEquals(10, yLookup[0]);
		assertEquals(10, yLookup[1]);
		assertEquals(0, yLookup[2]);
	}
}
