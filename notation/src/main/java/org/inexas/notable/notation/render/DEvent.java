package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.text.*;

class DEvent extends Drawable {
	int clicks;
	private Font font;
	Glyph glyph;
	double width;

	DEvent(final double originX, final double originY, final Layout layout, final Glyph glyph) {
		super(originX, originY);
		this.glyph = glyph;
		font = layout.m.font;
	}

	DEvent(final double originX, final double originY) {
	}

	DEvent() {
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.setFont(font);
		gc.fillText(glyph.c, originX, originY);
	}
}
