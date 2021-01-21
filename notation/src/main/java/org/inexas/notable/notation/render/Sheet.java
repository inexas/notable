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

		// Setup
		final double staffSpaceHeight = metrics.staffSpaceHeight;
		final double noteHeight = staffSpaceHeight / 2;
		final double x = metrics.sideMargin;
		final double y = metrics.topAndBottomMargin;
		final double width = metrics.paperWidth - 2 * metrics.sideMargin;
		final double height = staffSpaceHeight * 4;

		// y coordinate of lines and spaces...
		final double staff8y = y;
		final double staff7y = y + staffSpaceHeight * 0.5;
		final double staff6y = y + staffSpaceHeight * 1;
		final double staff5y = y + staffSpaceHeight * 1.5;
		final double staff4y = y + staffSpaceHeight * 2;
		final double staff3y = y + staffSpaceHeight * 2.5;
		final double staff2y = y + staffSpaceHeight * 3;
		final double staff1y = y + staffSpaceHeight * 3.5;
		final double staff0y = y + staffSpaceHeight * 4;

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
		final KeySignature keySignature = score.keySignature;
		if(keySignature.accidentalCount > 0) {
			final int[] lines = {8, 5, 9, 6, 3, 7, 4};
			final Glyph accidental = keySignature.isSharp() ?
					Glyph.accidentalSharp : Glyph.accidentalFlat;
			for(int i = 0; i < keySignature.accidentalCount; i++) {
				final double y1 = staff0y - noteHeight * lines[i];
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
				staff0y,
				staffSpaceHeight / 2);

		final List<Event> events = score.getFirstPart().getFirstPhrase().events;
		for(final Event event : events) {
			final Duration duration = event.duration;
			if(event instanceof Note) {
				final Note note = (Note) event;
				final Glyph glyph = Glyph.get(note);
				gc.fillText(glyph.c, cursor, yLookup[note.number]);
			} else if(event instanceof Rest) {
				final Rest rest = (Rest) event;
			}
			cursor += duration.clicks * perClick;
			// todo Ghost, Tuplet, Ghost, Chords
		}
	}
}
