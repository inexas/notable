package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;

public class Sheet extends VBox {
	final Metrics metrics = new Metrics();
	private final Canvas canvas;

	public Sheet() {
		canvas = new Canvas(metrics.paperWidth, metrics.paperHeight);
		getChildren().add(canvas);
	}

	public void draw() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(1.0);
		final double x = metrics.paperWidth - metrics.sideMargins - metrics.sideMargins;
		double y = metrics.topAndBottomMargin;
		for(int i = 0; i < 5; i++) {
			gc.strokeLine(metrics.sideMargins, y, x, y);
			y += metrics.staffSpaceHeight;
		}

		gc.setFont(metrics.bravura);
		gc.fillText("\uE1D4", 10, 10);
		gc.fillText("\uE1D4", 40, 15);
		gc.fillText("\uE1D4", 70, 20);
	}
}
