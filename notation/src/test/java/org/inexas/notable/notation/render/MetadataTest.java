package org.inexas.notable.notation.render;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataTest {
	@SuppressWarnings("unused")
	public static final String testFilePath = "test.json";

	@Test
	public void testFontMetadataFile() {
		final FontMetadataFile file = FontMetadataFile.instance;
		assertEquals("Bravura", file.fontName);
		assertEquals("1.39", file.fontVersion);
		assertEquals(27, file.engravingDefaults.size());
		assertEquals(0.16, file.engravingDefaults.get("arrowShaftThickness"));
	}

	@Test
	public void testClasses() {
		assertEquals("clefs", ClassesMetadataFile.instance.getClass("cClef"));
	}

	@Test
	public void testGlyphNames() {
		final GlyphNamesMetadataFile file = GlyphNamesMetadataFile.instance;
		final Map<String, GlyphNamesMetadataFile.GlyphIdentity> map = file.glyphs;
		assertEquals(2791, map.size());
		final GlyphNamesMetadataFile.GlyphIdentity glyph = map.get("4stringTabClef");
		assertEquals("U+E06E", glyph.codepoint);
		assertEquals("4-string tab clef", glyph.description);
	}

	@Test
	public void testRanges() {
		final RangesMetadataFile file = RangesMetadataFile.load(
				RangesMetadataFile.class,
				RangesMetadataFile.filename);
		assertEquals(123, file.ranges.size());
		final RangesMetadataFile.Range range = file.ranges.get("accordion");
		assertEquals("Accordion", range.description);
		assertEquals("U+E8A0", range.rangeStart);
		assertEquals("U+E8DF", range.rangeEnd);
		assertEquals(55, range.glyphs.length);
		assertEquals("accdnRH3RanksPiccolo", range.glyphs[0]);
	}
}
