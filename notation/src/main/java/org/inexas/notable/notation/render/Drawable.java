package org.inexas.notable.notation.render;

abstract class Drawable {
	/**
	 * The origin is a notional point on the drawable object that
	 * locates it on the JavaFX Canvas os measured in pixels with
	 * 0,0 at the top left
	 */
	double originX, originY;

	@SuppressWarnings("unused")
	abstract void draw();
}
