package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;

public class DGlyph extends Drawable {
	private final String glyph;

	DGlyph(final double originX, final double originY, final Glyph glyph) {
		super(originX, originY);
		this.glyph = glyph.c;
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.fillText(glyph, originX, originY);
	}
}
