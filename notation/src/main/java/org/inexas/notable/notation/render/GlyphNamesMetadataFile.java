package org.inexas.notable.notation.render;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class GlyphNamesMetadataFile extends MetadataFile {
	public static final String filename = "glyphnames.json";
	@JsonIgnore
	public Map<String, Glyph> glyphs = new HashMap<>();

	@JsonAnySetter
	@SuppressWarnings("unused") // Called by Jackson
	public void setDetail(String key, Object value) {
		@SuppressWarnings("unchecked") final Map<String, String> map = (Map<String, String>) value;
		final Glyph glyph = new Glyph(map.get("codepoint"), map.get("description"));
		glyphs.put(key, glyph);
	}

	public static class Glyph {
		final String codepoint;
		final String description;

		public Glyph(final String codepoint, final String description) {
			this.codepoint = codepoint;
			this.description = description;
		}
	}
}
