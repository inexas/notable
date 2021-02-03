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
	/**
	 * The amount of space in pixels to be left of the glyph clear
	 */
	final double lBearing;
	/**
	 * The amount of space in pixels to be right of the glyph clear
	 */
	final double rBearing;

	Glyph(
			final String name,
			final double scaleFactor,
			final double lBearingEm,
			final double rBearingEm) {
		this.name = name;

		// Identity
		final GlyphNamesMetadataFile.GlyphIdentity identity =
				GlyphNamesMetadataFile.instance.glyphs.get(name);

		codepoint = identity.codepoint;

		// U+1D100 to character...
		c = Character.toString(Integer.parseInt(identity.codepoint.substring(2), 16));

		description = identity.description;

		// Bounding box
		final Map<String, Double[]> box = FontMetadataFile.instance.glyphBBoxes.get(name);
		xNE = box.get("bBoxNE")[0] * scaleFactor;
		yNE = box.get("bBoxNE")[1] * scaleFactor;
		xSW = box.get("bBoxSW")[0] * scaleFactor;
		ySW = box.get("bBoxSW")[1] * scaleFactor;

		// Bounding box dimensions
		height = (yNE - ySW);
		width = (xNE - xSW);

		lBearing = width * lBearingEm;
		rBearing = width * rBearingEm;
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
				", l=" + lBearing +
				", r=" + rBearing +
				'}';
	}
}
