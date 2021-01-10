/*
 * Copyright (C) 2021 Inexas. All rights reserved
 */
package org.inexas.notable.gui;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;

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

		final Sheet sheet = new Sheet();
		primaryStage.setScene(new Scene(sheet, 800, 500));
		primaryStage.show();

		sheet.draw();
	}

}
