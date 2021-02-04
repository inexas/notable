package org.inexas.notable.notation.render;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.inexas.notable.notation.model.*;
import org.inexas.notable.notation.parser.*;

public class Experiment extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) {
		stage.setTitle("Renderer Experiment");

		// Size and position window...
		stage.setX(10);
		stage.setY(10);

		final Score score = MikiParser.fromString("A").score;

		final Layout layout = new Layout(
				score,
				Layout.Format.a4,
				Layout.Style.linear,
				Metrics.M);

		final Metrics m = layout.m;

		final Canvas canvas = new Canvas(m.paperWidth, m.paperHeight);
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		final Pane root = new Pane();
		root.getChildren().add(canvas);

		layout.pages.get(0).draw(gc);

		final Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
