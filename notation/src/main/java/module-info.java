module org.inexas.notable.notation {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.antlr.antlr4.runtime;

	requires org.inexas.notable.util;
	requires com.fasterxml.jackson.databind;

	exports org.inexas.notable.notation.model;
	exports org.inexas.notable.notation.render;
}
