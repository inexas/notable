package org.inexas.notable.notation.render;

import javafx.scene.text.*;
import org.inexas.notable.notation.model.*;

import java.io.*;
import java.util.*;

public class GlyphFactory {
	private final Map<String, Glyph> map = new HashMap<>();
	private final double scaleFactor;

	Glyph[] timeSignatures = new Glyph[10];

	private final Glyph noteheadWhole;
	private final Glyph noteheadHalf;
	Glyph noteheadBlack;
	private final Glyph restWhole;
	private final Glyph restHalf;
	private final Glyph restQuarter;
	private final Glyph rest8th;
	private final Glyph rest16th;
	private final Glyph noteheadXWhole;
	private final Glyph noteheadXBlack;
	private final Glyph flag8thUp;
	private final Glyph flag16thUp;
	private final Glyph flag8thDown;
	private final Glyph flag16thDown;
	Glyph accidentalFlat;
	Glyph accidentalSharp;
	private final Glyph accidentalNatural;

	GlyphFactory(final double staffSize) {
		scaleFactor = staffSize / 4.0;

		timeSignatures[1] = getGlyph("timeSig1");
		timeSignatures[2] = getGlyph("timeSig2");
		timeSignatures[3] = getGlyph("timeSig3");
		timeSignatures[4] = getGlyph("timeSig4");
		timeSignatures[5] = getGlyph("timeSig5");
		timeSignatures[6] = getGlyph("timeSig6");
		timeSignatures[7] = getGlyph("timeSig7");
		timeSignatures[8] = getGlyph("timeSig8");
		timeSignatures[9] = getGlyph("timeSig9");

		noteheadWhole = getGlyph("noteheadWhole");
		noteheadHalf = getGlyph("noteheadHalf");
		noteheadBlack = getGlyph("noteheadBlack");
		restWhole = getGlyph("restWhole");
		restHalf = getGlyph("restHalf");
		restQuarter = getGlyph("restQuarter");
		rest8th = getGlyph("rest8th");
		rest16th = getGlyph("rest16th");
		noteheadXWhole = getGlyph("noteheadXWhole");
		noteheadXBlack = getGlyph("noteheadXBlack");
		flag8thUp = getGlyph("flag8thUp");
		flag16thUp = getGlyph("flag16thUp");
		flag8thDown = getGlyph("flag8thDown");
		flag16thDown = getGlyph("flag16thDown");
		accidentalFlat = getGlyph("accidentalFlat");
		accidentalSharp = getGlyph("accidentalSharp");
		accidentalNatural = getGlyph("accidentalNatural");
	}

	Glyph getGlyph(final String name) {
		Glyph returnValue;

		returnValue = map.get(name);
		if(returnValue == null) {
			returnValue = new Glyph(name, scaleFactor);
			map.put(name, returnValue);
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

	/**
	 * All the things you need to render a note
	 */
	static class NoteKit {
		Glyph noteHead;
		Glyph flag;
		Glyph accidental;
		// todo Where is this in SMuFL
		Glyph dot;  // For duration
		Glyph articulation;
	}

	NoteKit getNoteHeadGlyph(final Note note, final boolean stemUp) {
		final NoteKit returnValue = new NoteKit();

		final int clicks = note.duration.clicks;

		// Note head...
		if(note.isGhost) {
			returnValue.noteHead = switch(clicks) {
				case 32:
					yield noteheadWhole;
				case 16:
					yield noteheadHalf;
				default:
					yield noteheadBlack;
			};
		} else {
			returnValue.noteHead = switch(clicks) {
				case 32:
					yield noteheadWhole;
				case 16:
					yield noteheadHalf;
				default:
					yield noteheadBlack;
			};
		}

		// Flag...
		if(stemUp) {
			returnValue.flag = switch(clicks) {
				case 4, 3:
					yield flag8thUp;
				case 2:
					yield flag16thUp;
				default:
					yield null;
			};
		} else { // Stem down
			returnValue.flag = switch(clicks) {
				case 4, 3:
					yield flag8thDown;
				case 2:
					yield flag16thDown;
				default:
					yield null;
			};
		}

		// Accidental...
		final Accidental accidental = note.get(Accidental.class);
		if(accidental == Accidental.flat) {
			returnValue.accidental = accidentalFlat;
		}
		if(accidental == Accidental.sharp) {
			returnValue.accidental = accidentalSharp;
		}
		if(accidental == Accidental.natural) {
			returnValue.accidental = accidentalNatural;
		}

		final Articulation articulation = note.get(Articulation.class);
		if(articulation != null) {
			returnValue.articulation = getGlyph(articulation.name);
		}

		return returnValue;
	}
}
