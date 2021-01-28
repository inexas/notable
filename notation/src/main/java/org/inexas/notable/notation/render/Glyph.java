package org.inexas.notable.notation.render;

import java.util.*;

/**
 * See https://w3c.github.io/smufl/gitbook/tables/individual-notes.html
 */
public class Glyph {
	public final String name;
	public final String description;
	public final String codepoint;
	public final String c;
	final double xNE;
	final double yNE;
	final double xSW;
	final double ySW;
	final double height;
	final double width;
	final double advance;

	Glyph(final String name, final double sizeFactor, final double advanceFactor) {
		this.name = name;

		// Identity
		final GlyphNamesMetadataFile.GlyphIdentity identity =
				GlyphNamesMetadataFile.instance.glyphs.get(name);

		codepoint = identity.codepoint;

		c = Character.toString(Integer.parseInt(identity.codepoint.substring(2), 16));

		description = identity.description;

		// Bounding box
		final Map<String, Double[]> box = FontMetadataFile.instance.glyphBBoxes.get(name);
		xNE = box.get("bBoxNE")[0];
		yNE = box.get("bBoxNE")[1];
		xSW = box.get("bBoxSW")[0];
		ySW = box.get("bBoxSW")[1];

		// Size
		height = (yNE - ySW) * sizeFactor;
		width = (xNE - xSW) * sizeFactor;
		advance = width * advanceFactor;
	}

	@Override
	public String toString() {
		return "Glyph {" +
				name + ": " + description + " " + codepoint + ' ' + c +
				", xNE=" + xNE +
				", yNE=" + yNE +
				", xSW=" + xSW +
				", ySW=" + ySW +
				", h=" + height +
				", w=" + width +
				'}';
	}
}
