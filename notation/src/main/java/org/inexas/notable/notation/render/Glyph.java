package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class Glyph {
	// Clefs
	public static Glyph gClef = new Glyph("gClef");

	// Notes
	public static Glyph noteWhole = new Glyph("noteWhole");
	public static Glyph noteHalfUp = new Glyph("noteHalfUp");
	public static Glyph noteHalfDown = new Glyph("noteHalfDown");
	public static Glyph noteQuarterUp = new Glyph("noteQuarterUp");
	public static Glyph noteQuarterDown = new Glyph("noteQuarterDown");
	public static Glyph note8thUp = new Glyph("note8thUp");
	public static Glyph note8thDown = new Glyph("note8thDown");
	public static Glyph note16thUp = new Glyph("note16thUp");
	public static Glyph note16thDown = new Glyph("note16thDown");

	public static Glyph get(final Note note) {
		final Glyph returnValue;

		switch(note.duration.clicks) {
			case 32 -> returnValue = noteWhole;
			case 16 -> returnValue = noteHalfUp;
			case 8 -> returnValue = noteQuarterUp;
			case 4 -> returnValue = note8thUp;
			case 2 -> returnValue = note16thUp;
			default -> throw new RuntimeException("Note not supported: " + note);
		}

		return returnValue;
	}

	public static Glyph get(final Rest note) {
		// todo Load rests
		return null;
	}

	// Rests
	public static Glyph restWhole = new Glyph("restWhole");
	public static Glyph restHalf = new Glyph("restHalf");
	public static Glyph restQuarter = new Glyph("restQuarter");
	public static Glyph rest8th = new Glyph("rest8th");
	public static Glyph rest16th = new Glyph("rest16th");

	// Time signatures
	public static Glyph[] timeSignature = {
			null,
			new Glyph("timeSig1"),
			new Glyph("timeSig2"),
			new Glyph("timeSig3"),
			new Glyph("timeSig4"),
			new Glyph("timeSig5"),
			new Glyph("timeSig6"),
			new Glyph("timeSig7"),
			new Glyph("timeSig8"),
			new Glyph("timeSig9")
	};

	// Accidentals
	public static Glyph accidentalFlat = new Glyph("accidentalFlat");
	public static Glyph accidentalNatural = new Glyph("accidentalNatural");
	public static Glyph accidentalSharp = new Glyph("accidentalSharp");


	public final String name;
	public final String description;
	public final String codepoint;
	public final String c;
	private final double xNE;
	private final double yNE;
	private final double xSW;
	private final double ySW;
	private final double height;
	final double width;

	private Glyph(final String name) {
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
		height = yNE - ySW;
		width = xNE - xSW;
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
