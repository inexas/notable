package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class KeySignatureTest {
	@Test
	void testMap() {
		Assertions.assertEquals("C", KeySignature.get("C").name);
		assertEquals("Cb", KeySignature.get("Cb").name);
		assertEquals("C", KeySignature.C.name);
	}

	@Test
	void testSharpFlat() {
		assertTrue(KeySignature.get("C").isNatural());
		assertTrue(KeySignature.get("C#").isSharp());
		assertTrue(KeySignature.get("Cb").isFlat());

		assertEquals(0, KeySignature.get("C").accidentalCount);
		assertEquals(7, KeySignature.get("C#").accidentalCount);
		assertEquals(1, KeySignature.get("F").accidentalCount);
		assertEquals(1, KeySignature.get("G").accidentalCount);
	}
}
