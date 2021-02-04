package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.text.*;

class DLabel extends Drawable {
	private final String text;
	private final Font font;

	DLabel(final double x, final double y, final Layout layout, final String text) {
		super(x, y);
		this.text = text;
		font = layout.m.textFont;
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.setFont(font);
		gc.fillText(text, originX, originY);
	}
}
