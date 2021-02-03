package org.inexas.notable.notation.render;


import javafx.scene.text.*;
import org.inexas.notable.notation.model.*;

import java.io.*;
import java.util.*;

/**
 * Metrics stores the engraving defaults used for rendering.
 * <p>
 * See: https://w3c.github.io/smufl/gitbook/specification/engravingdefaults.html
 */
public class Metrics {
	// Rendering sizes
	@SuppressWarnings("unused")
	public final static double S = 32;
	@SuppressWarnings("unused")
	final static double M = 48;
	@SuppressWarnings("unused")
	final static double L = 80;
	@SuppressWarnings("unused")
	final static double XL = 120;

	// Load engraving defaults...

	/**
	 * The thickness of the line used for the shaft of an arrow
	 */
	final double arrowShaftThickness;
	/**
	 * The default distance between multiple thin barlines when locked
	 * together, e.g. between two thin barlines making a double barline,
	 * measured from the right-hand edge of the left barline to the
	 * left-hand edge of the right barline.
	 */
	final double barlineSeparation;
	/**
	 * The distance between the inner edge of the primary and outer
	 * edge of subsequent secondary beams e.g. between beams on 16th notes
	 */
	@SuppressWarnings("unused")
	private final double beamSpacing;

	/**
	 * The thickness of a beam
	 */
	@SuppressWarnings("unused")
	private final double beamThickness;
	/**
	 * The thickness of the vertical line of a bracket grouping
	 * staves together
	 */
	@SuppressWarnings("unused")
	private final double bracketThickness;
	/**
	 * The length of the dashes to be used in a dashed barline
	 */
	@SuppressWarnings("unused")
	private final double dashedBarlineDashLength;
	/**
	 * The length of the gap between dashes in a dashed barline
	 */
	@SuppressWarnings("unused")
	private final double dashedBarlineGapLength;
	/**
	 * The thickness of a dashed barline
	 */
	@SuppressWarnings("unused")
	private final double dashedBarlineThickness;
	/*
	 * The thickness of the horizontal line drawn between two
	 * vertical lines, known as the H-bar, in a
	 * multi-bar rest.
	 * <p>
	 * Note this does not appear in the meta data file, I put
	 * 0.16 in as a guess so I have no idea what the real value
	 * should be. Correct it if you find a reasonable value.
	 */
	//final double hBarThickness = 0.16;
	/**
	 * The thickness of a crescendo/diminuendo hairpin
	 */
	@SuppressWarnings("unused")
	private final double hairpinThickness;
	/**
	 * The amount by which a leger line should extend either side
	 * of a notehead, scaled proportionally with the notehead's
	 * size, e.g. when scaled down as a grace note - notes may
	 * vary in width so how much wider than the note
	 */
	final double legerLineExtension;
	/**
	 * The thickness of a leger line (normally somewhat thicker
	 * than a staff line)
	 */
	final double legerLineThickness;
	/**
	 * The total length of leger lines
	 */
	final double legerLineLength;
	/**
	 * The thickness of the lyric extension line to indicate a
	 * melisma in vocal music
	 */
	@SuppressWarnings("unused")
	private final double lyricLineThickness;
	/**
	 * The thickness of the dashed line used for an octave line
	 */
	@SuppressWarnings("unused")
	private final double octaveLineThickness;
	/**
	 * The thickness of the line used for piano pedaling
	 */
	@SuppressWarnings("unused")
	private final double pedalLineThickness;
	/**
	 * The default horizontal distance between the dots and the inner
	 * barline of a repeat barline, measured from the edge of the dots
	 * to the edge of the barline.
	 */
	@SuppressWarnings("unused")
	private final double repeatBarlineDotSeparation;
	/**
	 * The thickness of the brackets drawn to indicate repeat endings
	 */
	@SuppressWarnings("unused")
	private final double repeatEndingLineThickness;
	/**
	 * The thickness of the end of a slur
	 */
	@SuppressWarnings("unused")
	private final double slurEndpointThickness;
	/**
	 * The thickness of the mid-point of a slur (i.e. its thickest point)
	 */
	@SuppressWarnings("unused")
	private final double slurMidpointThickness;
	/**
	 * The thickness of each staff line
	 */
	final double staffLineThickness;
	/**
	 * The thickness of a stem
	 */
	final double stemThickness;
	/**
	 * Length of stem on standalone notes
	 */
	final double stemLength;

	/**
	 * The thickness of the vertical line of a sub-bracket grouping
	 * staves belonging to the same instrument together
	 */
	@SuppressWarnings("unused")
	private final double subBracketThickness;
	/**
	 * The thickness of a box drawn around text instructions (e.g.
	 * rehearsal marks)
	 */
	@SuppressWarnings("unused")
	private final double textEnclosureThickness;
	/**
	 * The thickness of a thick barline, e.g. in a final barline
	 * or a repeat barline
	 */
	@SuppressWarnings("unused")
	private final double thickBarlineThickness;
	/**
	 * The thickness of a thin barline, e.g. a normal barline, or
	 * each of the lines of a double barline
	 */
	@SuppressWarnings("unused")
	private final double thinBarlineThickness;
	/*
	 * The default distance between a pair of thin and thick
	 * barlines when locked together, e.g. between the thin and
	 * thick barlines making a final barline, or between
	 * the thick and thin barlines making a start repeat
	 * barline.
	 * <p>
	 * Note I didn't find a value for this one so 2 is wrong for sure
	 */
	// final double thinThickBarlineSeparation = 2;
	/**
	 * The thickness of the end of a tie
	 */
	@SuppressWarnings("unused")
	private final double tieEndpointThickness;
	/**
	 * The thickness of the mid-point of a tie
	 */
	@SuppressWarnings("unused")
	private final double tieMidpointThickness;
	/**
	 * The thickness of the brackets drawn either side of tuplet
	 * numbers
	 */
	@SuppressWarnings("unused")
	private final double tupletBracketThickness;

	// Page dimensions...

	private final double aspectRatio = 1.414;   // A4 aspect ratio
	/**
	 * This factor cam be used to ca
	 * private final double factor = 0.5729;
	 * /**
	 * Width of paper in pixels
	 */
	final double paperWidth;
	/**
	 * Height of paper in pixels
	 */
	final double paperHeight;
	/**
	 * Side margin width in pixels
	 */
	final double sideMargin;
	/**
	 * Top and bottom margins in pixels
	 */
	final double topMargin;
	/**
	 * Width available to staff in pixels
	 */
	final double width;
	/**
	 * The height of space between staff lines. Staff lines don't occupy
	 * extra space on the staff, they just overlay the line that joins two
	 * spaces.
	 */
	final double staffSpaceHeight;

	private final double slotHeight;

	/**
	 * Minimum amount of white space following barline
	 */
	final double barlineAdvance;
	// todo Review barlineAdvance - is this the best way?

	/**
	 * Height of a five line staff, 4 x staffSpaceHeight
	 */
	final double staffHeight;

	final Font font;

	// Clefs
	public Glyph cClef; // Alto and Tenor
	public Glyph fClef; // Bass
	public Glyph gClef; // Treble

	// Note components
	public final Glyph noteheadWhole;
	public final Glyph noteheadHalf;
	public final Glyph noteheadBlack;
	public final Glyph noteheadXBlack;
	public final Glyph noteheadXWhole;

	public final Glyph flag8thUp;
	public final Glyph flag8thDown;
	public final Glyph flag16thUp;
	public final Glyph flag16thDown;

	// Rests
	public final Glyph restWhole;
	public final Glyph restHalf;
	public final Glyph restQuarter;
	public final Glyph rest8th;
	public final Glyph rest16th;

	// Accidentals
	public Glyph accidentalFlat;
	public Glyph accidentalNatural;
	public Glyph accidentalSharp;

	// Time signatures
	public Glyph[] timeSignature;


	private Map<String, Double> tmpEngravingDefaults;

	Metrics(final double staffHeight) {
		this.staffHeight = staffHeight;
		paperWidth = 20.0 * staffHeight;  // 20 because it looks OK
		paperHeight = paperWidth * aspectRatio;
		sideMargin = staffHeight / 2.0;
		topMargin = sideMargin * aspectRatio;
		staffSpaceHeight = staffHeight / 4.0;
		slotHeight = staffHeight / 8.0;
		width = paperWidth - 2.0 * sideMargin;
		barlineAdvance = slotHeight;

		// Load engraving defaults...
		tmpEngravingDefaults = FontMetadataFile.instance.engravingDefaults;
		arrowShaftThickness = load("arrowShaftThickness");
		barlineSeparation = load("barlineSeparation");
		beamSpacing = load("beamSpacing");
		beamThickness = load("beamThickness");
		bracketThickness = load("bracketThickness");
		dashedBarlineDashLength = load("dashedBarlineDashLength");
		dashedBarlineGapLength = load("dashedBarlineGapLength");
		dashedBarlineThickness = load("dashedBarlineThickness");
		hairpinThickness = load("hairpinThickness");
		legerLineExtension = load("legerLineExtension");
		legerLineThickness = load("legerLineThickness");
		lyricLineThickness = load("lyricLineThickness");
		octaveLineThickness = load("octaveLineThickness");
		pedalLineThickness = load("pedalLineThickness");
		repeatBarlineDotSeparation = load("repeatBarlineDotSeparation");
		repeatEndingLineThickness = load("repeatEndingLineThickness");
		slurEndpointThickness = load("slurEndpointThickness");
		slurMidpointThickness = load("slurMidpointThickness");
		staffLineThickness = load("staffLineThickness");
		stemThickness = load("stemThickness");
		subBracketThickness = load("subBracketThickness");
		textEnclosureThickness = load("textEnclosureThickness");
		thickBarlineThickness = load("thickBarlineThickness");
		thinBarlineThickness = load("thinBarlineThickness");
		tieEndpointThickness = load("tieEndpointThickness");
		tieMidpointThickness = load("tieMidpointThickness");
		tupletBracketThickness = load("tupletBracketThickness");
		tmpEngravingDefaults = null; // Thanks for the memory!

		// Load the glyphs
		font = loadFont(staffHeight);

		// Clefs
		cClef = loadGlyph("cClef", 1.5, 1.5);
		fClef = loadGlyph("fClef", 1.5, 1.5);
		gClef = loadGlyph("gClef", 1.5, 1.5);

		// Note components
		noteheadWhole = loadGlyph("noteheadWhole", 0.0, 1.8);
		noteheadHalf = loadGlyph("noteheadHalf", 0.0, 1.8);
		noteheadBlack = loadGlyph("noteheadBlack", 0.0, 1.8);
		noteheadXBlack = loadGlyph("noteheadXBlack", 0.0, 1.8);
		noteheadXWhole = loadGlyph("noteheadXWhole", 0.0, 1.8);

		stemLength = slotHeight * 6.5;
		flag8thUp = loadGlyph("flag8thUp");
		flag8thDown = loadGlyph("flag8thDown");
		flag16thUp = loadGlyph("flag16thUp");
		flag16thDown = loadGlyph("flag16thDown");

		legerLineLength = legerLineExtension + noteheadBlack.width + legerLineExtension;

		// Rests
		restWhole = loadGlyph("restWhole", 0.0, 1.8);
		restHalf = loadGlyph("restHalf", 0.0, 1.8);
		restQuarter = loadGlyph("restQuarter", 0.0, 1.8);
		rest8th = loadGlyph("rest8th", 0.0, 1.8);
		rest16th = loadGlyph("rest16th", 0.0, 1.8);

		// Accidentals
		accidentalFlat = loadGlyph("accidentalFlat", 0.0, 1.2);
		accidentalNatural = loadGlyph("accidentalNatural", 0.0, 1.2);
		accidentalSharp = loadGlyph("accidentalSharp", 0.0, 1.2);

		// Time signatures
		timeSignature = new Glyph[]{
				null,
				loadGlyph("timeSig1"),
				loadGlyph("timeSig2"),
				loadGlyph("timeSig3"),
				loadGlyph("timeSig4"),
				loadGlyph("timeSig5"),
				loadGlyph("timeSig6"),
				loadGlyph("timeSig7"),
				loadGlyph("timeSig8"),
				loadGlyph("timeSig9")
		};
	}

	private Glyph loadGlyph(final String name) {
		return new Glyph(name, staffSpaceHeight, 0.0, 0.0);
	}

	private Glyph loadGlyph(final String name, final double lBearingEm, final double rBearingEm) {
		return new Glyph(name, staffSpaceHeight, lBearingEm, rBearingEm);
	}

	private double load(final String variable) {
		return tmpEngravingDefaults.get(variable) * staffSpaceHeight;
	}

	Glyph getItemGlyph(final Event event) {
		final Glyph returnValue;

		final int clicks = event.duration.clicks;
		if(event instanceof Note) {
			if(clicks >= 32) {
				returnValue = noteheadWhole;
			} else if(clicks >= 16) {
				returnValue = noteheadHalf;
			} else {
				returnValue = noteheadBlack;
			}
		} else if(event instanceof Rest) {
			if(clicks >= 32) {
				returnValue = restWhole;
			} else if(clicks >= 16) {
				returnValue = restHalf;
			} else if(clicks >= 8) {
				returnValue = restQuarter;
			} else if(clicks >= 4) {
				returnValue = rest8th;
			} else {
				returnValue = rest16th;
			}
		} else {
			assert event instanceof Ghost : event.getClass().getSimpleName();
			returnValue = clicks >= 16 ? noteheadXWhole : noteheadXBlack;
		}

		return returnValue;
	}

	Glyph getNoteHeadGlyph(final Note note) {
		final Glyph returnValue;

		switch(note.duration.clicks) {
			case 32 -> returnValue = noteheadWhole;
			case 16 -> returnValue = noteheadHalf;
			default -> returnValue = noteheadBlack;
		}

		return returnValue;
	}

	Glyph getFlag(final int clicks, final int relativeSlot) {
		final Glyph returnValue;

		if(relativeSlot < 4) { // Stem up...
			switch(clicks) {
				case 4 -> returnValue = flag8thUp;
				case 2 -> returnValue = flag16thUp;
				default -> returnValue = null;
			}
		} else { // Stem down
			switch(clicks) {
				case 4 -> returnValue = flag8thDown;
				case 2 -> returnValue = flag16thDown;
				default -> returnValue = null;
			}
		}

		return returnValue;
	}

	private Font loadFont(final double size) {
		final Font returnValue;

		try {
			final ClassLoader classLoader = FontMetadataFile.class.getClassLoader();
			final InputStream is = classLoader.getResourceAsStream("Bravura.otf");
			returnValue = Font.loadFont(is, size);
		} catch(final Exception e) {
			throw new RuntimeException("Error loading font", e);
		}

		return returnValue;
	}


	Y getY(final Staff staff) {
		return new Y(staff);
	}

	class Y {
		final double[] index = new double[8 * 7 + 1];

		Y(final Staff staff) {
			double y = topMargin
					+ (staff.slotsAbove() // Notes above the staff
					+ 8 // Slots in the staff
					+ staff.lowSlot)  // Slots below the staff
					* slotHeight;
			for(int i = 0; i <= Note.MAXIMUM; i++) {
				index[i] = y;
				y -= slotHeight;
			}

			l0 = index[staff.lowSlot];
			s0 = index[staff.lowSlot + 1];
			l1 = index[staff.lowSlot + 2];
			s1 = index[staff.lowSlot + 3];
			l2 = index[staff.lowSlot + 4];
			s2 = index[staff.lowSlot + 5];
			l3 = index[staff.lowSlot + 6];
			s3 = index[staff.lowSlot + 7];
			l4 = index[staff.lowSlot + 8];

			wholeRest = l3;
			rest = l2;
		}

		final double l4;
		final double s3;
		final double l3;
		public final double s2;
		final double l2;
		final double s1;
		final double l1;
		final double s0;
		final double l0;

		final double wholeRest;
		public final double rest;
	}
}
