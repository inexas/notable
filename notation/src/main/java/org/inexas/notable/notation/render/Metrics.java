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
	 * Width of paper in pixels
	 */
	final double paperWidth = 1000;
	/**
	 * Height of paper in pixels
	 */
	final double paperHeight = paperWidth * aspectRatio;
	/**
	 * Side margin width of paper in pixels
	 */
	final double sideMargin = 50;      // Left and right
	/**
	 * Top and bottom margins width of paper in pixels
	 */
	final double topAndBottomMargin = (int) (sideMargin * aspectRatio);
	/**
	 * The height of space between staff lines. Staff lines don't occupy
	 * extra space on the staff, they just overlay the line that joins two
	 * spaces.
	 */
	final double staffSpaceHeight = 10;

	// Font defaults
	final Font bravura;
	private final Font bravuraText;

	public final static Metrics instance = new Metrics();

	private Metrics() {
		// Load fonts
		bravura = loadFont("Bravura.otf", 36);
		bravuraText = loadFont("BravuraText.otf", 36);

		assert "Bravura".equals(FontMetadataFile.instance.fontName);

		// Load engraving defaults...

		final Map<String, Double> engravingDefaults = FontMetadataFile.instance.engravingDefaults;
		arrowShaftThickness = engravingDefaults.get("arrowShaftThickness");
		barlineSeparation = engravingDefaults.get("barlineSeparation");
		beamSpacing = engravingDefaults.get("beamSpacing");
		beamThickness = engravingDefaults.get("beamThickness");
		bracketThickness = engravingDefaults.get("bracketThickness");
		dashedBarlineDashLength = engravingDefaults.get("dashedBarlineDashLength");
		dashedBarlineGapLength = engravingDefaults.get("dashedBarlineGapLength");
		dashedBarlineThickness = engravingDefaults.get("dashedBarlineThickness");
		hairpinThickness = engravingDefaults.get("hairpinThickness");
		legerLineExtension = engravingDefaults.get("legerLineExtension");
		legerLineThickness = engravingDefaults.get("legerLineThickness");
		lyricLineThickness = engravingDefaults.get("lyricLineThickness");
		octaveLineThickness = engravingDefaults.get("octaveLineThickness");
		pedalLineThickness = engravingDefaults.get("pedalLineThickness");
		repeatBarlineDotSeparation = engravingDefaults.get("repeatBarlineDotSeparation");
		repeatEndingLineThickness = engravingDefaults.get("repeatEndingLineThickness");
		slurEndpointThickness = engravingDefaults.get("slurEndpointThickness");
		slurMidpointThickness = engravingDefaults.get("slurMidpointThickness");
		staffLineThickness = engravingDefaults.get("staffLineThickness");
		stemThickness = engravingDefaults.get("stemThickness");
		subBracketThickness = engravingDefaults.get("subBracketThickness");
		textEnclosureThickness = engravingDefaults.get("textEnclosureThickness");
		thickBarlineThickness = engravingDefaults.get("thickBarlineThickness");
		thinBarlineThickness = engravingDefaults.get("thinBarlineThickness");
		tieEndpointThickness = engravingDefaults.get("tieEndpointThickness");
		tieMidpointThickness = engravingDefaults.get("tieMidpointThickness");
		tupletBracketThickness = engravingDefaults.get("tupletBracketThickness");
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

	final static String gClef = "\uE050";
	final static String fClef = "\uE062";
}
