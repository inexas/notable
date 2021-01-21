package org.inexas.notable.notation.render;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class GlyphNamesMetadataFile extends MetadataFile {
	public static final String filename = "glyphnames.json";
	final static GlyphNamesMetadataFile instance = GlyphNamesMetadataFile.load(
			GlyphNamesMetadataFile.class,
			GlyphNamesMetadataFile.filename);

	@JsonIgnore
	public Map<String, GlyphIdentity> glyphs = new HashMap<>();

	@JsonAnySetter
	@SuppressWarnings("unused") // Called by Jackson
	public void setDetail(String key, Object value) {
		@SuppressWarnings("unchecked") final Map<String, String> map = (Map<String, String>) value;
		final GlyphIdentity glyph = new GlyphIdentity(map.get("codepoint"), map.get("description"));
		glyphs.put(key, glyph);
	}

	public static class GlyphIdentity {
		final String codepoint;
		final String description;

		public GlyphIdentity(final String codepoint, final String description) {
			this.codepoint = codepoint;
			this.description = description;
		}
	}
}
