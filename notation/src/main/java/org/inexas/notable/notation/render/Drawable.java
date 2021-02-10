package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;

import java.util.*;

abstract class Drawable {
	/**
	 * The origin is a notional point on the drawable object that
	 * locates it on the JavaFX Canvas os measured in pixels with
	 * 0,0 at the top left
	 */
	double originX;
	final double originY;
	private Drawable[] drawables;

	Drawable(final double originX, final double originY) {
		this.originX = originX;
		this.originY = originY;
	}

	Drawable() {
		originX = 0.0;
		originY = 0;
	}

	void setDrawables(final List<Drawable> drawables) {
		final int size = drawables.size();
		this.drawables = new Drawable[size];
		for(int i = 0; i < size; i++) {
			this.drawables[i] = drawables.get(i);
		}
	}

	void draw(final GraphicsContext gc) {
		for(int i = 0; i < drawables.length; i++) {
			drawables[i].draw(gc);
		}
	}
}
