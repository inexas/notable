/*
 * Copyright (C) 2021 Inexas. All rights reserved
 */
package org.inexas.notable.gui;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import org.inexas.notable.notation.model.*;
import org.inexas.notable.notation.render.*;

public class Gui extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Notable!");

		// Size and position window...
		primaryStage.setX(100);
		primaryStage.setY(200);

		final Score score = Score.fromString("C C G G | A8* B C A G2 | F4* F E E | D D C2");
		final Sheet sheet = new Sheet(score);
		primaryStage.setScene(new Scene(sheet, 800, 500));
		primaryStage.show();

		sheet.draw();
	}
}
