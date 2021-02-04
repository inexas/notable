package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.text.*;

class DEvent extends Drawable {
	private final Font font;
	private final Glyph glyph;

	DEvent(
			final double originX, final double originY,
			final Glyph glyph,
			final Layout layout) {
		super(originX, originY);
		this.glyph = glyph;
		font = layout.m.font;
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.setFont(font);
		gc.fillText(glyph.c, originX, originY);
	}
}
