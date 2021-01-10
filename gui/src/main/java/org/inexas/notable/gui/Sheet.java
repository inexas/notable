package org.inexas.notable.gui;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.jetbrains.annotations.*;

import java.io.*;

public class Sheet extends VBox {
	private final Canvas canvas;
	private final int canvasWidth = 800;
	private final int canvasHeight = 600;

	private final Font bravura;
	@SuppressWarnings("unused")
	private final Font bravuraText;

	private final int leftMargin = 10;
	private final int topMargin = 10;
	private final int rightMargin = 10;
	private final int staffLineHeight = 10;

	public Sheet() {
		bravura = loadFont("fonts/Bravura.otf", 30);
		bravuraText = loadFont("fonts/BravuraText.otf", 32);
		canvas = new Canvas(canvasWidth, canvasHeight);
		getChildren().add(canvas);
	}

	public void draw() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(1.0);
		int y = topMargin;
		int x = canvasWidth - leftMargin - rightMargin;
		for(int i = 0; i < 5; i++) {
			gc.strokeLine(leftMargin, y, x, y);
			y += staffLineHeight;
		}

		gc.setFont(bravura);
		gc.fillText("\uE1D4", 10, 10);
		gc.fillText("\uE1D4", 40, 15);
		gc.fillText("\uE1D4", 70, 20);
	}

	private Font loadFont(
			@NotNull final String pathname,
			final int size) {
		final Font returnValue;

		final File file = new File(pathname);
		if(file.exists() && file.isFile()) {
			try {
				final InputStream is = new FileInputStream(file);
				returnValue = Font.loadFont(is, size);
			} catch(final FileNotFoundException e) {
				throw new RuntimeException("Failed to load font", e);
			}
		} else {
			throw new RuntimeException("No such file: " + file.getAbsolutePath());
		}

		return returnValue;
	}
}
