package org.inexas.notable.notation.render;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GlyphFactoryTest {

	@Test
	void testBasics() {
		final GlyphFactory factory = new GlyphFactory(40.0);
		final Glyph glyph = factory.getGlyph("cClef");

		assertEquals("C clef", glyph.description);
		assertEquals("U+E05C", glyph.codepoint);
		assertNotNull(glyph.c);

		assertEquals(27.96, glyph.xNE, 0.0001);
		assertEquals(20.24, glyph.yNE, 0.0001);
		assertEquals(0.0, glyph.xSW, 0.0001);
		assertEquals(-20.24, glyph.ySW, 0.0001);

		assertEquals(40.48, glyph.height, 0.0001);
		assertEquals(27.96, glyph.width, 0.0001);

		assertEquals(5.592, glyph.lBearing, 0.0001);
		assertEquals(8.388, glyph.rBearing, 0.0001);
		assertEquals(41.94, glyph.advance, 0.0001);
	}

	@Test
	void testClasses() {
		assertEquals("clefs", ClassesMetadataFile.instance.getClass("cClef"));
		assertNull(ClassesMetadataFile.instance.getClass("accidentalDoubleFlatParens"));
	}
}
