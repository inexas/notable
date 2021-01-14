package org.inexas.notable.notation.render;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataTest {
	@SuppressWarnings("unused")
	public static final String testFilePath = "test.json";

	@Test
	public void testFontMetadataFile() {
		final FontMetadataFile file = FontMetadataFile.load(
				FontMetadataFile.class,
				FontMetadataFile.filename);
		assertEquals("Bravura", file.fontName);
		assertEquals("1.39", file.fontVersion);
		assertEquals(27, file.engravingDefaults.size());
		assertEquals(0.16, file.engravingDefaults.get("arrowShaftThickness"));
		file.printEngravingDefaults();
	}

	@Test
	public void testClasses() {
		final ClassesMetadataFile file = ClassesMetadataFile.load(
				ClassesMetadataFile.class,
				ClassesMetadataFile.filename);
		assertEquals(85, file.classMap.size());
		assertEquals(2, file.classMap.get("accidentalsPersian").length);
		assertEquals("accidentalKoron", file.classMap.get("accidentalsPersian")[0]);
	}

	@Test
	public void testGlyphNames() {
		final GlyphNamesMetadataFile file = GlyphNamesMetadataFile.load(
				GlyphNamesMetadataFile.class,
				GlyphNamesMetadataFile.filename);
		final Map<String, GlyphNamesMetadataFile.Glyph> map = file.glyphs;
		assertEquals(2791, map.size());
		GlyphNamesMetadataFile.Glyph glyph = map.get("4stringTabClef");
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
