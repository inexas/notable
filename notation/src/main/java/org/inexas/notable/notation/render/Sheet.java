package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import org.inexas.notable.notation.model.*;

import java.util.*;

/**
 * Conventions used
 */
public class Sheet extends VBox {
	private final static int paperWidth = 1000;
	private final static int margin = 50;
	private final static int staffSpaceHeight = 10;
	final static Metrics metrics = new Metrics(paperWidth, margin, staffSpaceHeight);
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

		// Gather some useful constants...
		final Staff staff = score.staff;
		final double staffSpaceHeight = metrics.staffSpaceHeight;
		final double positionHeight = staffSpaceHeight / 2;
		final double xOrigin = metrics.sideMargin;
		final double yOrigin = metrics.topMargin;
		final double width = metrics.paperWidth - 2 * metrics.sideMargin;
		final double topLine = yOrigin + staff.positionsAbove() * positionHeight;
		final double staffHeight = staffSpaceHeight * 4;
		final double baseLine = topLine + staffHeight;
		final KeySignature key = score.key;
		final double[] yLookup = KeySignature.C.yLookup(staff, baseLine, positionHeight);
		final double yRest = yLookup[staff.lowLinePosition + 4];
		final double yWholeRest = yLookup[staff.lowLinePosition + 6];

		// Draw staff...

		// Horizontal lines...
		gc.setLineWidth(metrics.staffLineThickness * 7.7);
		double y = topLine;
		for(int i = 0; i < 5; i++) {
			gc.strokeLine(xOrigin, y, xOrigin + width, y);
			y += staffSpaceHeight;
		}

		// Vertical lines...
		gc.strokeLine(xOrigin, topLine, xOrigin, topLine + staffHeight);
		gc.strokeLine(xOrigin + width, yOrigin, xOrigin + width, yOrigin + staffHeight);

		// G Clef
		double xCursor = xOrigin + 5;
		gc.setFont(metrics.bravura);
		gc.fillText(Glyph.gClef.c, xCursor, topLine + 3 * staffSpaceHeight);
		xCursor += Glyph.gClef.width * 10;

		// Key signature...
		if(key.accidentalCount > 0) {
			final int[] lines = {8, 5, 9, 6, 3, 7, 4};
			final Glyph accidental = key.isSharp() ? Glyph.accidentalSharp : Glyph.accidentalFlat;
			for(int i = 0; i < key.accidentalCount; i++) {
				final double y1 = baseLine - positionHeight * lines[i];
				gc.fillText(accidental.c, xCursor, y1);
				xCursor += accidental.width * 8;
			}
			xCursor += 7;
		}

		// Time signature...
		final Glyph numerator = Glyph.timeSignature[score.timeSignature.numerator];
		final Glyph denominator = Glyph.timeSignature[score.timeSignature.denominator];
		gc.fillText(numerator.c, xCursor, yOrigin + staffSpaceHeight);
		gc.fillText(denominator.c, xCursor, yOrigin + 3 * staffSpaceHeight);
		xCursor += numerator.width * 20;

		// Notes...

		// Four measures, 32 clicks per
		final double usableWidth = width - (xCursor - metrics.sideMargin);
		final double perClick = usableWidth / (4 * 32);

		final List<Event> events = score.getFirstPart().getFirstPhrase().events;
		boolean first = true;
		for(final Event event : events) {
			// Are there Barlines?
			if(first) {
				first = false;
			} else {
				final Barline barline = event.get(Barline.class);
				if(barline != null) {
					gc.strokeLine(xCursor - 8, yOrigin, xCursor - 8, baseLine);
				}
			}

			final Duration duration = event.duration;
			if(event instanceof Note) {
				final Note note = (Note) event;

				// Draw leger lines...
				final int count = staff.countLedgerLines(note.position);
				if(count > 0) {
					double yLine = yLookup[staff.highLinePosition + 2];
					for(int i = 0; i < count; i++) {
						gc.strokeLine(xCursor - 4, yLine, xCursor + 16, yLine);
						yLine -= staffSpaceHeight;
					}
				} else if(count < 0) {
					double yLine = yLookup[staff.lowLinePosition - 2];
					for(int i = 0; i < -count; i++) {
						gc.strokeLine(xCursor - 4, yLine, xCursor + 16, yLine);
						yLine += staffSpaceHeight;
					}
				}

				// Draw note...
				final Glyph glyph = Glyph.get(note);
				gc.fillText(glyph.c, xCursor, yLookup[note.position]);
			} else if(event instanceof Rest) {
				final Rest rest = (Rest) event;
				final Glyph glyph = Glyph.get(rest);
				y = rest.duration.clicks == 32 ? yWholeRest : yRest;
				gc.fillText(glyph.c, xCursor, y);
			}
			xCursor += duration.clicks * perClick;
		}
	}
}
