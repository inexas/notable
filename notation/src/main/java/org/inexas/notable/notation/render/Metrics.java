package org.inexas.notable.notation.render;


import javafx.scene.text.*;

import java.io.*;

/**
 * Metrics stores the engraving defaults used for rendering.
 * <p>
 * See: https://w3c.github.io/smufl/gitbook/specification/engravingdefaults.html
 */
@SuppressWarnings("unused")
public class Metrics {

	// Page dimensions

	/**
	 * Width of paper in pixels
	 */
	final double paperWidth = 1000;
	/**
	 * Side margin width of paper in pixels
	 */
	final double sideMargins = 50;      // Left and right
	/**
	 * The height of space between staff lines. Staff lines don't occupy
	 * extra space on the staff, they just overlay the line that joins two
	 * spaces.
	 */
	final double staffSpaceHeight = 10;
	/**
	 * The thickness of the line used for the shaft of an arrow
	 */
	final double arrowShaftThickness = 0.16;
	/**
	 * The default distance between multiple thin barlines when locked
	 * together, e.g. between two thin barlines making a double barline,
	 * measured from the right-hand edge of the left barline to the
	 * left-hand edge of the right barline.
	 */
	final double barlineSeparation = 0.4;
	/**
	 * The distance between the inner edge of the primary and outer
	 * edge of subsequent secondary beams e.g. between beams on 16th notes
	 */
	final double beamSpacing = 0.25;


	// Font defaults
	/**
	 * The thickness of a beam
	 */
	final double beamThickness = 0.5;
	/**
	 * The thickness of the vertical line of a bracket grouping
	 * staves together
	 */
	final double bracketThickness = 0.5;
	/**
	 * The length of the dashes to be used in a dashed barline
	 */
	final double dashedBarlineDashLength = 0.5;
	/**
	 * The length of the gap between dashes in a dashed barline
	 */
	final double dashedBarlineGapLength = 0.25;
	/**
	 * The thickness of a dashed barline
	 */
	final double dashedBarlineThickness = 0.16;
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
	final double hairpinThickness = 0.16;
	/**
	 * The amount by which a leger line should extend either side
	 * of a notehead, scaled proportionally with the notehead's
	 * size, e.g. when scaled down as a grace note - notes may
	 * vary in width so how much wider than the note
	 */
	final double legerLineExtension = 0.4;
	/**
	 * The thickness of a leger line (normally somewhat thicker
	 * than a staff line)
	 */
	final double legerLineThickness = 0.16;
	/**
	 * The thickness of the lyric extension line to indicate a
	 * melisma in vocal music
	 */
	final double lyricLineThickness = 0.16;
	/**
	 * The thickness of the dashed line used for an octave line
	 */
	final double octaveLineThickness = 0.16;
	/**
	 * The thickness of the line used for piano pedaling
	 */
	final double pedalLineThickness = 0.16;
	/**
	 * The default horizontal distance between the dots and the inner
	 * barline of a repeat barline, measured from the edge of the dots
	 * to the edge of the barline.
	 */
	final double repeatBarlineDotSeparation = 0.16;
	/**
	 * The thickness of the brackets drawn to indicate repeat endings
	 */
	final double repeatEndingLineThickness = 0.16;
	/**
	 * The thickness of the end of a slur
	 */
	final double slurEndpointThickness = 0.1;
	/**
	 * The thickness of the mid-point of a slur (i.e. its thickest point)
	 */
	final double slurMidpointThickness = 0.22;
	/**
	 * The thickness of each staff line
	 */
	final double staffLineThickness = 0.13;
	/**
	 * The thickness of a stem
	 */
	final double stemThickness = 0.12;
	/**
	 * The thickness of the vertical line of a sub-bracket grouping
	 * staves belonging to the same instrument together
	 */
	final double subBracketThickness = 0.16;
	/**
	 * The thickness of a box drawn around text instructions (e.g.
	 * rehearsal marks)
	 */
	final double textEnclosureThickness = 0.16;
	/**
	 * The thickness of a thick barline, e.g. in a final barline
	 * or a repeat barline
	 */
	final double thickBarlineThickness = 0.5;
	/**
	 * The thickness of a thin barline, e.g. a normal barline, or
	 * each of the lines of a double barline
	 */
	final double thinBarlineThickness = 0.16;
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
	final double tieEndpointThickness = 0.1;
	/**
	 * The thickness of the mid-point of a tie
	 */
	final double tieMidpointThickness = 0.22;
	/**
	 * The thickness of the brackets drawn either side of tuplet
	 * numbers
	 */
	final double tupletBracketThickness = 0.16;
	final Font bravura;
	final Font bravuraText;
	private final double aspectRatio = 1.414;   // A4 aspect ratio
	/**
	 * Height of paper in pixels
	 */
	final double paperHeight = paperWidth * aspectRatio;
	/**
	 * Top and bottom margins width of paper in pixels
	 */
	final double topAndBottomMargin = (int) (sideMargins * aspectRatio);

	public Metrics() {
		// todo These need to be separated out
		bravura = loadFont("Bravura.otf", 30);
		bravuraText = loadFont("BravuraText.otf", 32);
	}

	private Font loadFont(final String pathName, final int size) {
		final Font returnValue;

		try {
			final ClassLoader classLoader = FontMetadataFile.class.getClassLoader();
			final InputStream is = classLoader.getResourceAsStream(pathName);
			returnValue = Font.loadFont(is, size);
		} catch(final Exception e) {
			throw new RuntimeException("Error loading font", e);
		}

		return returnValue;
	}
}
