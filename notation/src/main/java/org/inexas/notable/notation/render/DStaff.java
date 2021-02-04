package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

class DStaff extends Drawable {

	DStaff(final double originX, final double originY,
	       final Layout layout,
	       final List<Double> barlines,
	       final String labelText) {
		super(originX, originY);
		final Metrics m = layout.m;
		final double endX = m.paperWidth - m.sideMargin;
		final double spaceHeight = m.staffSpaceHeight;

		final List<Drawable> drawables = new ArrayList<>();

		double cursorX = originX;

		// Label
		if(labelText != null) {
			final double[] widthHeight = m.WidthHeight(labelText);
			final double y = originY - 3.0 * m.slotHeight;
			drawables.add(new DLabel(cursorX, y, layout, labelText));
			cursorX += widthHeight[0] + m.labelBearing;
		}

		// Barlines...
		final double y = originY - m.staffHeight;
		drawables.add(new DLine(cursorX, originY, cursorX, y, m.staffLineThickness));
		for(final double x : barlines) {
			drawables.add(new DLine(x, originY, x, y, m.staffLineThickness));
		}

		// Staff lines
		double cursorY = originY;
		for(int i = 0; i < 5; i++) {
			drawables.add(new DLine(cursorX, cursorY, endX, cursorY, m.staffLineThickness));
			cursorY -= spaceHeight;
		}

		// Clef
		final Glyph glyph = m.gClef;
		cursorX += glyph.lBearing;
		cursorY = originY;
		drawables.add(new DEvent(cursorX, cursorY - m.staffSpaceHeight, glyph, layout));

		// Key signature
		drawables.add(new DKeySignature(cursorX, cursorY, layout, KeySignature.get("A")));

		setDrawables(drawables);
	}
}
