package org.inexas.notable.notation.render;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import org.inexas.notable.notation.parser.*;

public class Experiment extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Renderer Experiment");

		// Size and position window...
		primaryStage.setX(10);
		primaryStage.setY(10);

		final MikiParser parser = MikiParser.fromString(
//				"R1 | R2 R4 R8 R8 | R16");
				"key A C C +G G | A8* B C A G2 | F4* F E E | D D C2");
		final Sheet sheet = new Sheet(parser.score);

		final Metrics metrics = sheet.metrics;
		primaryStage.setScene(new Scene(sheet, metrics.paperWidth, metrics.paperHeight));
		primaryStage.show();

		sheet.draw();
	}
}
