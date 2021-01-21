package org.inexas.notable.notation.render;

import java.util.*;

public class FontMetadataFile extends MetadataFile {
	public static final String filename = "bravura_metadata.json";
	public static final FontMetadataFile instance =
			FontMetadataFile.load(FontMetadataFile.class, filename);

	// Body of file starts
	// Font name
	public String fontName;

	// Font version
	public String fontVersion;

	// engravingDefaults
	public Map<String, Double> engravingDefaults;

	// Glyph Bounding Boxes
	public Map<String, Map<String, Double[]>> glyphBBoxes;

	// Glyphs With Alternates
	public static class Alternate {
		public String codepoint;
		public String name;
	}
	public Map<String, Map<String, List<Alternate>>> glyphsWithAlternates;

	// Glyphs With Anchors
	public Map<String, Map<String, Double[]>> glyphsWithAnchors;

	// Ligatures
	public static class Ligature {
		public String codepoint;
		public String[] componentGlyphs;
		public String description;
	}
	public Map<String, Ligature> ligatures;

	// Optional Glyphs
	public static class OptionalGlyph {
		public String[] classes;
		public String codepoint;
		public String description;
	}
	public Map<String, OptionalGlyph> optionalGlyphs;

	// Sets
	public static class GlyphVariation {
		public String alternateFor;
		public String codepoint;
		public String description;
		public String name;
	}
	public static class Variation {
		public String description;
		public GlyphVariation[] glyphs;
		public String type;
	}
	public Map<String, Variation> sets;

	public void printEngravingDefaults() {
		for(final Map.Entry<String, Double> entry : engravingDefaults.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
