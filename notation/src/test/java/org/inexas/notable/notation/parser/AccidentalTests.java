package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccidentalTests extends ParserTestAbc {
	@Test
	void miki() {
		Assertions.assertEquals("b", Accidental.flat.miki);
		assertEquals("bb", Accidental.doubleFlat.miki);
		assertEquals("#", Accidental.sharp.miki);
		assertEquals("##", Accidental.doubleSharp.miki);
		assertEquals("n", Accidental.natural.miki);

		assertEquals(Accidental.flat, Accidental.get("b"));
		assertEquals(Accidental.doubleFlat, Accidental.get("bb"));
		assertEquals(Accidental.sharp, Accidental.get("#"));
		assertEquals(Accidental.doubleSharp, Accidental.get("##"));
		assertEquals(Accidental.natural, Accidental.get("n"));
	}

	@Test
	void testAccidentals() {
		assertEquals("B Bb Bbb R |||\n", toMiki("B Bb Bbb R |||\n"));
		assertEquals("B B# B## R |||\n", toMiki("B B# B## R |||\n"));
		assertEquals("B Bn B R |||\n", toMiki("B Bn BR |||\n"));
	}
}
