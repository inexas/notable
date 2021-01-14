package org.inexas.notable.notation.render;

import java.util.*;

public class FontMetadataFile extends MetadataFile {
	public static final String filename = "bravura_metadata.json";

	// Body of file starts
	// Font name
	public String fontName;

	// Font version
	public String fontVersion;

	// engravingDefaults
	public Map<String, Double> engravingDefaults;
	// GlyphBBoxes
	public Map<String, Map<String, Double[]>> glyphBBoxes;
	public Map<String, Map<String, List<Alternate>>> glyphsWithAlternates;
	// Glyphs With Anchors
	public Map<String, Map<String, Double[]>> glyphsWithAnchors;
	public Map<String, Ligature> ligatures;
	public Map<String, OptionalGlyph> optionalGlyphs;
	public Map<String, Variation> sets;

	public void printEngravingDefaults() {
		for(final Map.Entry<String, Double> entry : engravingDefaults.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	// Glyphs with alternates
	public static class Alternate {
		public String codepoint;
		public String name;
	}

	// Ligatures
	public static class Ligature {
		public String codepoint;
		public String[] componentGlyphs;
		public String description;
	}

	// Optional Glyphs
	public static class OptionalGlyph {
		public String[] classes;
		public String codepoint;
		public String description;
	}

	// Sets
	public static class Glyph {
		public String alternateFor;
		public String codepoint;
		public String description;
		public String name;
	}

	public static class Variation {
		public String description;
		public Glyph[] glyphs;
		public String type;
	}
}
