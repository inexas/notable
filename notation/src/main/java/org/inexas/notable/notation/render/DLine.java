package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;

class DLine extends Drawable {
	private final double thickness;
	private final double x1;
	private final double y1;

	DLine(final double originX, final double originY,
	      final double x1, final double y1,
	      final double thickness) {
		super(originX, originY);
		this.x1 = x1;
		this.y1 = y1;
		this.thickness = thickness;
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.setLineWidth(thickness);
		gc.strokeLine(originX, originY, x1, y1);
	}
}
