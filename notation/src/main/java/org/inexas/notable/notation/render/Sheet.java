package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import org.inexas.notable.notation.model.*;

import java.util.*;

/**
 * Conventions used
 */
public class Sheet extends VBox {
	final Metrics metrics = Metrics.instance;
	private final Canvas canvas;
	private final Score score;

	public Sheet(final Score score) {
		this.score = score;
		canvas = new Canvas(metrics.paperWidth, metrics.paperHeight);
		getChildren().add(canvas);
	}

	public void draw() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();

		// Draw staff...

		// Gather some useful variables...
		final double staffSpaceHeight = metrics.staffSpaceHeight;
		final double noteHeight = staffSpaceHeight / 2;
		final double x = metrics.sideMargin;
		final double y = metrics.topAndBottomMargin;
		final double width = metrics.paperWidth - 2 * metrics.sideMargin;
		final double height = staffSpaceHeight * 4;

		// y coordinate of lines and spaces...
		final double topLine = y;

		// Account for leger lines
		final KeySignature keySignature = score.key;


		final double baseLine = topLine + staffSpaceHeight * 4;

		gc.setLineWidth(metrics.staffLineThickness * 7.7);

		// Horizontal lines...
		double tmp = y;
		for(int i = 0; i < 5; i++) {
			gc.strokeLine(x, tmp, x + width, tmp);
			tmp += staffSpaceHeight;
		}

		// Vertical lines
		gc.strokeLine(x, y, x, y + height);
		gc.strokeLine(x + width, y, x + width, y + height);

		gc.setFont(metrics.bravura);

		double cursor = x;

		// G Clef
		cursor += 5;
		gc.fillText(Glyph.gClef.c, cursor, y + 3 * staffSpaceHeight);
		cursor += Glyph.gClef.width * 10;

		// Key signature...
		if(keySignature.accidentalCount > 0) {
			final int[] lines = {8, 5, 9, 6, 3, 7, 4};
			final Glyph accidental = keySignature.isSharp() ?
					Glyph.accidentalSharp : Glyph.accidentalFlat;
			for(int i = 0; i < keySignature.accidentalCount; i++) {
				final double y1 = baseLine - noteHeight * lines[i];
				gc.fillText(accidental.c, cursor, y1);
				cursor += accidental.width * 8;
			}
			cursor += 7;
		}

		// Time signature
		final Glyph numerator = Glyph.timeSignature[score.timeSignature.numerator];
		final Glyph denominator = Glyph.timeSignature[score.timeSignature.denominator];
		gc.fillText(numerator.c, cursor, y + staffSpaceHeight);
		gc.fillText(denominator.c, cursor, y + 3 * staffSpaceHeight);
		cursor += numerator.width * 20;

		// Notes
		final double usableWidth = width - (cursor - metrics.sideMargin);

		// Four measures, 32 clicks per
		final double perClick = usableWidth / (4 * 32);

		final double[] yLookup = KeySignature.C.yLookup(
				score.staff,
				baseLine,
				staffSpaceHeight / 2);

		final List<Event> events = score.getFirstPart().getFirstPhrase().events;
		boolean first = true;
		for(final Event event : events) {
			// Are there Barlines?
			if(first) {
				first = false;
			} else {
				final Barline barline = event.get(Barline.class);
				if(barline != null) {
					gc.strokeLine(cursor - 8, topLine, cursor - 8, baseLine);
				}
			}

			final Duration duration = event.duration;
			if(event instanceof Note) {
				final Note note = (Note) event;
				final Glyph glyph = Glyph.get(note);
				gc.fillText(glyph.c, cursor, yLookup[note.position]);
			} else if(event instanceof Rest) {
				final Rest rest = (Rest) event;
				final Glyph glyph = Glyph.get(rest);
			}
			cursor += duration.clicks * perClick;
			// todo Ghost, Tuplet, Ghost, Chords
		}
	}

	private void calculateRange() {
		int minNote = score.staff.lowLineNumber;
		int maxNote = score.staff.highLineNumber;
		for(final Event event : score.getFirstPart().getFirstPhrase().events) {
			final int position = ((Note) event).position;
			if(position < minNote) {
				minNote = position;
			} else if(position > maxNote) {
				maxNote = position;
			}
		}
	}
}
