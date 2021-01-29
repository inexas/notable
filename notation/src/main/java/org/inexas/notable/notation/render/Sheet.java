package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.inexas.notable.notation.model.*;

import java.util.*;

/**
 * Conventions used
 */
public class Sheet extends VBox {
	final static Metrics metrics = new Metrics(Metrics.XL);
	private final Canvas canvas;
	private final Score score;

	public Sheet(final Score score) {
		this.score = score;
		canvas = new Canvas(metrics.paperWidth, metrics.paperHeight);
		getChildren().add(canvas);
	}

	public void draw() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);

		// Draw staff...

		// Gather some useful constants...
		final Staff staff = score.staff;
		final Metrics.Y y = metrics.new Y(staff);
		final double staffSpaceHeight = metrics.staffSpaceHeight;
		final double xOrigin = metrics.sideMargin;
		final double width = metrics.width;
		final KeySignature key = score.key;

		// Temporary variables used to avoid repetive local calcuation
		double _x, _y;
		Glyph glyph;

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

		// Clef
		double xCursor = xOrigin + metrics.barlineAdvance;
		gc.setFont(metrics.font);
		switch(staff.type) {
			case alto -> {
				glyph = metrics.cClef;
				_y = y.l2;
			}
			case bass -> {
				glyph = metrics.fClef;
				_y = y.l3;
			}
			case tenor -> {
				glyph = metrics.cClef;
				_y = y.l3;
			}
			case treble -> {
				glyph = metrics.gClef;
				_y = y.l1;
			}
			default -> throw new RuntimeException("Unknown type: " + staff.type);
		}
		gc.fillText(glyph.c, xCursor, _y);
		xCursor += metrics.gClef.advance;

		// Key signature...
		if(key.accidentalCount > 0) {
			final int[] accidentals = staff.getAccidentals(key);
			final Glyph accidental = key.isSharp() ? metrics.accidentalSharp : metrics.accidentalFlat;
			for(int i = 0; i < key.accidentalCount; i++) {
				gc.fillText(accidental.c, xCursor, y.index[accidentals[i]]);
				xCursor += accidental.advance;
			}
		}

		// Time signature...
		final Glyph numerator = metrics.timeSignature[score.timeSignature.numerator];
		final Glyph denominator = metrics.timeSignature[score.timeSignature.denominator];
		gc.fillText(numerator.c, xCursor, y.l3);
		gc.fillText(denominator.c, xCursor, y.l1);
		xCursor += numerator.advance;

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

				// Draw note head...
				glyph = metrics.get(note);
				_y = y.index[note.slot];
				gc.fillText(glyph.c, xCursor, _y);
				// Stem...
				if(note.duration.clicks < 32) {
					gc.setLineWidth(metrics.stemThickness);
					final int relativeSlot = note.slot - staff.lowSlot;
					final double tilt = glyph.height / 5;
					if(relativeSlot < 4) {
						// Stem up...
						_x = xCursor + glyph.width - metrics.stemThickness / 2.0;
						gc.strokeLine(_x, _y - metrics.stemLength, _x, _y - tilt);
						// Flag?
						glyph = metrics.getFlag(note.duration.clicks, relativeSlot);
						if(glyph != null) {
							gc.fillText(glyph.c, _x, _y - metrics.stemLength);
						}
					} else {
						// Stem down...
						_x = xCursor + metrics.stemThickness / 2.0;
						gc.strokeLine(_x, _y + tilt, _x, _y + metrics.stemLength);
						// Flag?
						glyph = metrics.getFlag(note.duration.clicks, relativeSlot);
						if(glyph != null) {
							gc.fillText(glyph.c, _x, _y + metrics.stemLength);
						}
					}
				}
			} else if(event instanceof Rest) {
				final Rest rest = (Rest) event;
				glyph = metrics.get(rest);
				_y = rest.duration.clicks == 32 ? y.wholeRest : y.rest;
				gc.fillText(glyph.c, xCursor, _y);
			}
			xCursor += duration.clicks * perClick;
		}
	}

	void drawNote(final Note note) {

	}
}
