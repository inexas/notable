package org.inexas.notable.notation.render;


import javafx.scene.text.*;

import java.io.*;
import java.util.*;

/**
 * Metrics stores the engraving defaults used for rendering.
 * <p>
 * See: https://w3c.github.io/smufl/gitbook/specification/engravingdefaults.html
 */
@SuppressWarnings("unused")
public class Metrics {

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
	private final double barlineSeparation;
	/**
	 * The distance between the inner edge of the primary and outer
	 * edge of subsequent secondary beams e.g. between beams on 16th notes
	 */
	private final double beamSpacing;

	/**
	 * The thickness of a beam
	 */
	private final double beamThickness;
	/**
	 * The thickness of the vertical line of a bracket grouping
	 * staves together
	 */
	private final double bracketThickness;
	/**
	 * The length of the dashes to be used in a dashed barline
	 */
	private final double dashedBarlineDashLength;
	/**
	 * The length of the gap between dashes in a dashed barline
	 */
	private final double dashedBarlineGapLength;
	/**
	 * The thickness of a dashed barline
	 */
	private final double dashedBarlineThickness;
	/**
	 * The thickness of the horizontal line drawn between two
	 * vertical lines, known as the H-bar, in a
	 * multi-bar rest.
	 * <p>
	 * Note this does not appear in the meta data file, I put
	 * 0.16 in as a guess so I have no idea what the real value
	 * should be. Correct it if you find a reasonable value.
	 */
	final double hBarThickness = 0.16;
	/**
	 * The thickness of a crescendo/diminuendo hairpin
	 */
	private final double hairpinThickness;
	/**
	 * The amount by which a leger line should extend either side
	 * of a notehead, scaled proportionally with the notehead's
	 * size, e.g. when scaled down as a grace note - notes may
	 * vary in width so how much wider than the note
	 */
	private final double legerLineExtension;
	/**
	 * The thickness of a leger line (normally somewhat thicker
	 * than a staff line)
	 */
	private final double legerLineThickness;
	/**
	 * The thickness of the lyric extension line to indicate a
	 * melisma in vocal music
	 */
	private final double lyricLineThickness;
	/**
	 * The thickness of the dashed line used for an octave line
	 */
	private final double octaveLineThickness;
	/**
	 * The thickness of the line used for piano pedaling
	 */
	private final double pedalLineThickness;
	/**
	 * The default horizontal distance between the dots and the inner
	 * barline of a repeat barline, measured from the edge of the dots
	 * to the edge of the barline.
	 */
	private final double repeatBarlineDotSeparation;
	/**
	 * The thickness of the brackets drawn to indicate repeat endings
	 */
	private final double repeatEndingLineThickness;
	/**
	 * The thickness of the end of a slur
	 */
	private final double slurEndpointThickness;
	/**
	 * The thickness of the mid-point of a slur (i.e. its thickest point)
	 */
	private final double slurMidpointThickness;
	/**
	 * The thickness of each staff line
	 */
	final double staffLineThickness;
	/**
	 * The thickness of a stem
	 */
	private final double stemThickness;
	/**
	 * The thickness of the vertical line of a sub-bracket grouping
	 * staves belonging to the same instrument together
	 */
	private final double subBracketThickness;
	/**
	 * The thickness of a box drawn around text instructions (e.g.
	 * rehearsal marks)
	 */
	private final double textEnclosureThickness;
	/**
	 * The thickness of a thick barline, e.g. in a final barline
	 * or a repeat barline
	 */
	private final double thickBarlineThickness;
	/**
	 * The thickness of a thin barline, e.g. a normal barline, or
	 * each of the lines of a double barline
	 */
	private final double thinBarlineThickness;
	/**
	 * The default distance between a pair of thin and thick
	 * barlines when locked together, e.g. between the thin and
	 * thick barlines making a final barline, or between
	 * the thick and thin barlines making a start repeat
	 * barline.
	 * <p>
	 * Note I didn't find a value for this one so 2 is wrong for sure
	 */
	final double thinThickBarlineSeparation = 2;
	/**
	 * The thickness of the end of a tie
	 */
	private final double tieEndpointThickness;
	/**
	 * The thickness of the mid-point of a tie
	 */
	private final double tieMidpointThickness;
	/**
	 * The thickness of the brackets drawn either side of tuplet
	 * numbers
	 */
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
	 * Width of full width staff in pixels
	 */
	final double width;
	/**
	 * The height of space between staff lines. Staff lines don't occupy
	 * extra space on the staff, they just overlay the line that joins two
	 * spaces.
	 */
	final double staffSpaceHeight;

	/**
	 * Height of a five line staff, 4 x staffSpaceHeight
	 */
	private final int staffHeight;

	// Font defaults
	final Font bravura;


	Metrics(final int paperWidth, final int sideMargin, final int staffSpaceHeight) {
		this.paperWidth = paperWidth;
		this.sideMargin = sideMargin;
		this.staffSpaceHeight = staffSpaceHeight;

		paperHeight = paperWidth * aspectRatio;
		topMargin = (int) (this.sideMargin * aspectRatio);
		width = paperWidth - 2 * sideMargin;

		staffHeight = staffSpaceHeight * 4;

		bravura = loadFont(staffHeight);

		// Load engraving defaults...
		final double factor = 0.5729 * staffHeight;

		final Map<String, Double> engravingDefaults = FontMetadataFile.instance.engravingDefaults;
		arrowShaftThickness = engravingDefaults.get("arrowShaftThickness") * staffSpaceHeight;
		barlineSeparation = engravingDefaults.get("barlineSeparation") * staffSpaceHeight;
		beamSpacing = engravingDefaults.get("beamSpacing") * staffSpaceHeight;
		beamThickness = engravingDefaults.get("beamThickness") * staffSpaceHeight;
		bracketThickness = engravingDefaults.get("bracketThickness") * staffSpaceHeight;
		dashedBarlineDashLength = engravingDefaults.get("dashedBarlineDashLength") * staffSpaceHeight;
		dashedBarlineGapLength = engravingDefaults.get("dashedBarlineGapLength") * staffSpaceHeight;
		dashedBarlineThickness = engravingDefaults.get("dashedBarlineThickness") * staffSpaceHeight;
		hairpinThickness = engravingDefaults.get("hairpinThickness") * staffSpaceHeight;
		legerLineExtension = engravingDefaults.get("legerLineExtension") * staffSpaceHeight;
		legerLineThickness = engravingDefaults.get("legerLineThickness") * staffSpaceHeight;
		lyricLineThickness = engravingDefaults.get("lyricLineThickness") * staffSpaceHeight;
		octaveLineThickness = engravingDefaults.get("octaveLineThickness") * staffSpaceHeight;
		pedalLineThickness = engravingDefaults.get("pedalLineThickness") * staffSpaceHeight;
		repeatBarlineDotSeparation = engravingDefaults.get("repeatBarlineDotSeparation") * staffSpaceHeight;
		repeatEndingLineThickness = engravingDefaults.get("repeatEndingLineThickness") * staffSpaceHeight;
		slurEndpointThickness = engravingDefaults.get("slurEndpointThickness") * staffSpaceHeight;
		slurMidpointThickness = engravingDefaults.get("slurMidpointThickness") * staffSpaceHeight;
		staffLineThickness = engravingDefaults.get("staffLineThickness") * staffSpaceHeight;
		stemThickness = engravingDefaults.get("stemThickness") * staffSpaceHeight;
		subBracketThickness = engravingDefaults.get("subBracketThickness") * staffSpaceHeight;
		textEnclosureThickness = engravingDefaults.get("textEnclosureThickness") * staffSpaceHeight;
		thickBarlineThickness = engravingDefaults.get("thickBarlineThickness") * staffSpaceHeight;
		thinBarlineThickness = engravingDefaults.get("thinBarlineThickness") * staffSpaceHeight;
		tieEndpointThickness = engravingDefaults.get("tieEndpointThickness") * staffSpaceHeight;
		tieMidpointThickness = engravingDefaults.get("tieMidpointThickness") * staffSpaceHeight;
		tupletBracketThickness = engravingDefaults.get("tupletBracketThickness") * staffSpaceHeight;
	}

	private Font loadFont(final int size) {
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
}
