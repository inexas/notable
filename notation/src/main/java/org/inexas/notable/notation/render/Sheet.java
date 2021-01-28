package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import org.inexas.notable.notation.model.*;

import java.util.*;

/**
 * Conventions used
 */
public class Sheet extends VBox {
	final static Metrics metrics = new Metrics(Metrics.M);
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
		final Metrics.Y y = metrics.new Y(staff);
		final double staffSpaceHeight = metrics.staffSpaceHeight;
		final double slotHeight = metrics.slotHeight;
		final double xOrigin = metrics.sideMargin;
		final double width = metrics.width;
		final KeySignature key = score.key;

		// Temporary variables used to avoid repetive local calcuation
		double _x, _y;

		// Draw staff...

		// Horizontal lines...
		gc.setLineWidth(metrics.staffLineThickness);
		_x = xOrigin + width;
		_y = y.l4;
		for(int i = 0; i < 5; i++) {
			gc.strokeLine(xOrigin, _y, _x, _y);
			_y += staffSpaceHeight;
		}

		// Left and right barlines...
		gc.strokeLine(xOrigin, y.l4, xOrigin, y.l0);
		gc.strokeLine(xOrigin + width, y.l4, xOrigin + width, y.l0);

		// G Clef
		double xCursor = xOrigin + metrics.barlineAdvance;
		gc.setFont(metrics.font);
		gc.fillText(metrics.gClef.c, xCursor, y.l4 + 3 * staffSpaceHeight);
		xCursor += metrics.gClef.advance;

		// Key signature...
		if(key.accidentalCount > 0) {
			final int[] lines = {8, 5, 9, 6, 3, 7, 4};
			final Glyph accidental = key.isSharp() ? metrics.accidentalSharp : metrics.accidentalFlat;
			for(int i = 0; i < key.accidentalCount; i++) {
				final double y1 = y.l0 - slotHeight * lines[i];
				gc.fillText(accidental.c, xCursor, y1);
				xCursor += accidental.advance;
			}
		}

		// Time signature...
		final Glyph numerator = metrics.timeSignature[score.timeSignature.numerator];
		final Glyph denominator = metrics.timeSignature[score.timeSignature.denominator];
		gc.fillText(numerator.c, xCursor, y.l3);
		gc.fillText(denominator.c, xCursor, y.l1);
		xCursor += numerator.width;

		// Notes...

		// Four measures, 32 clicks per
		final double usableWidth = width - (xCursor - metrics.sideMargin);
		final double perClick = usableWidth / (4.0 * 32.0);

		final List<Event> events = score.getFirstPart().getFirstPhrase().events;
		boolean first = true;
		for(final Event event : events) {
			// Are there Barlines?
			if(first) {
				first = false;
			} else {
				final Barline barline = event.get(Barline.class);
				if(barline != null) {
					_x = xCursor - metrics.barlineAdvance;
					gc.strokeLine(_x, y.l4, _x, y.l0);
				}
			}

			final Duration duration = event.duration;
			if(event instanceof Note) {
				final Note note = (Note) event;

				// Draw leger lines...
				final int count = staff.countLedgerLines(note.slot);
				if(count != 0) {
					gc.setLineWidth(metrics.legerLineThickness);
					_x = xCursor - metrics.legerLineExtension;
					final double _x1 = _x + metrics.legerLineLength;
					if(count > 0) { // Above...
						_y = y.l4 - staffSpaceHeight;
						for(int i = 0; i < count; i++) {
							gc.strokeLine(_x, _y, _x1, _y);
							_y -= staffSpaceHeight;
						}
					} else { // Below..
						_y = y.l0 + staffSpaceHeight;
						for(int i = 0; i < -count; i++) {
							gc.strokeLine(_x, _y, _x1, _y);
							_y += staffSpaceHeight;
						}
					}
				}

				// Draw note...
				final Glyph glyph = metrics.get(note);
				gc.fillText(glyph.c, xCursor, y.index[note.slot]);
			} else if(event instanceof Rest) {
				final Rest rest = (Rest) event;
				final Glyph glyph = metrics.get(rest);
				_y = rest.duration.clicks == 32 ? y.wholeRest : y.rest;
				gc.fillText(glyph.c, xCursor, _y);
			}
			xCursor += duration.clicks * perClick;
		}
	}
}
