package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {
	private static final Map<String, Double> defaults = FontMetadataFile.instance.engravingDefaults;

	private final double arrowShaftThickness = defaults.get("arrowShaftThickness");
	private final double barlineSeparation = defaults.get("barlineSeparation");

	private final double F = 40; // Factor
	private final double f = 10; // Factor
	private final Metrics metrics = new Metrics(F);

	@Test
	public void testBasics() {

		assertEquals(arrowShaftThickness * f, metrics.arrowShaftThickness);
		assertEquals(barlineSeparation * f, metrics.barlineSeparation);

		assertEquals(F, metrics.staffHeight);
		assertEquals(20.0 * F, metrics.paperWidth);
	}

	@Test
	public void testFont() {
		assertEquals("Bravura", metrics.font.getName());
	}

	@Test
	public void testGlyphs() {
		final Glyph glyph = metrics.glyphFactory.getGlyph("gClef");
		assertEquals(0xE050, glyph.c.charAt(0));
		assertEquals("gClef", glyph.name);
		assertEquals("U+E050", glyph.codepoint);
		assertEquals("G clef", glyph.description);

		assertEquals(2.684, glyph.xNE);
		assertEquals(4.392, glyph.yNE);
		assertEquals(0.0, glyph.xSW);
		assertEquals(-2.632, glyph.ySW);

		final double height = (4.392 + 2.632) * 10;
		final double width = (2.684 + 0.0) * 10;

		assertEquals(height, glyph.height);
		assertEquals(width, glyph.width);
		final double rBearing = width * 1.3;
		assertEquals(rBearing, glyph.rBearing);
	}

	@Test
	public void testY() {
		final Staff staff = new Staff(Staff.Type.treble);
		final Metrics.Y y = metrics.getY(staff);
		assertEquals(metrics.topMargin, y.index[Note.F5], 0.0001);
	}
}
