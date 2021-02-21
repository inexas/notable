package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ArticulationsTest extends ParserTestAbc {

	@Test
	void quick() {
		assertEquals(
				"C. C.. C_ C! | C!! Cf Cg C |||\n",
				toMiki("C. C.. C_ C! | C!! Cf Cg C |||"));
	}

	@Test
	void enums() {
		assertEquals("f", Articulation.fermata.miki);
		assertEquals("g", Articulation.glissando.miki);
		assertEquals("", Articulation.legato.miki);
		assertEquals("!", Articulation.marcato.miki);
		assertEquals("!!", Articulation.marcatissimo.miki);
		assertEquals(".", Articulation.staccato.miki);
		assertEquals("..", Articulation.staccatissimo.miki);
		assertEquals("_", Articulation.tenuto.miki);
		assertEquals(".", Articulation.valueOf("staccato").miki);
		assertNotNull(Articulation.valueOf("staccato").description);
	}

	@Test
	void parsing() {
		// [._!fg]+
		assertEquals(
				"C. C.. C_ C! | C!! Cf Cg C |||\n",
				toMiki("C. C.. C_ C! | C!! Cf Cg C |||"));
		assertEquals("C. R R2 |||\n", toMiki("C. R R2|||"));
		assertEquals("[C E G]. R R2 |||\n", toMiki("[CEG]. R R2|||"));
	}
}
