package org.inexas.notable.notation.render;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;

public class GlyphWorks extends Application {
	private GraphicsContext gc;
	private final double size = 320.0;
	private final double scale = size / 4.0;

	private final double xIncrement = 150;
	private final double hLineX1 = 50.0;
	private final double hLineX2 = 900.0;
	private final double vLineY1 = 50.0;
	private final double vLineY2 = 800.0;
	private double x = 100;
	private final double y = 400;

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) {
		stage.setTitle("GlyphWorks");

		// Size and position window...
		stage.setX(10);
		stage.setY(10);

		final Font font = loadFont(size);

		Canvas canvas = new Canvas(1000, 900);
		gc = canvas.getGraphicsContext2D();
		gc.setFont(font);
		x = 100;
		notes();
		Pane pane = new Pane();
		pane.getChildren().add(canvas);
		final Tab noteTab = new Tab("Notes", pane);

		canvas = new Canvas(1000, 900);
		gc = canvas.getGraphicsContext2D();
		gc.setFont(font);
		x = 100;
		rests();
		pane = new Pane();
		pane.getChildren().add(canvas);
		final Tab restTab = new Tab("Rests", pane);

		final TabPane tabPane = new TabPane();
		tabPane.getTabs().add(noteTab);
		tabPane.getTabs().add(restTab);

		final VBox vBox = new VBox(tabPane);
		final Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.show();
	}

	private Font loadFont(final double size) {
		final Font returnValue;

		try {
			final ClassLoader classLoader = FontMetadataFile.class.getClassLoader();
			final InputStream is = classLoader.getResourceAsStream("Bravura.otf");
			returnValue = Font.loadFont(is, size);
		} catch(final Exception e) {
			throw new RuntimeException("Error loading font", e);
		}

		return returnValue;
	}

	private void drawHorizontalLines() {
		gc.strokeLine(hLineX1, y - size, hLineX2, y - size);
		gc.strokeLine(hLineX1, y, hLineX2, y);
		gc.strokeLine(hLineX1, y + size, hLineX2, y + size);
	}

	private void draw(final Glyph glyph) {
		gc.strokeLine(x, vLineY1, x, vLineY2);
		gc.fillText(glyph.c, x, y);
		x += xIncrement;
	}

	private void notes() {
		final Glyph head = new Glyph("noteheadBlack", scale);
		final Glyph stem = new Glyph("stem", scale);
		final Glyph flag8thUp = new Glyph("flag8thUp", scale);
		final Glyph flag8thDown = new Glyph("flag8thDown", scale);

		drawHorizontalLines();
		draw(head);
		draw(stem);
		draw(flag8thUp);
		draw(flag8thDown);

		// Compose
		draw(head);
		x -= xIncrement;

		// "stemUpSE": 1.18,0.168
		gc.fillText(stem.c,
				x + 1.18 * scale - stem.width / 2,
				y - 0.168 * scale);

		// "flag8thUp": { ...  "stemUpNW": 0.0,-0.04
		gc.fillText(flag8thUp.c,
				x + 1.18 * scale - stem.width,
				y - stem.height - 0.168 * scale);
	}

	private void rests() {
		final Glyph rest1 = new Glyph("restWhole", scale);
		final Glyph rest2 = new Glyph("restHalf", scale);
		final Glyph rest4 = new Glyph("restQuarter", scale);
		final Glyph rest8 = new Glyph("rest8th", scale);

		drawHorizontalLines();
		draw(rest1);
		draw(rest2);
		draw(rest4);
		draw(rest8);
	}
}
