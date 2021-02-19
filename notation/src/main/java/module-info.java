module org.inexas.notable.notation {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.antlr.antlr4.runtime;
	requires com.fasterxml.jackson.databind;

	requires org.inexas.notable.util;

	exports org.inexas.notable.notation.model;
	exports org.inexas.notable.notation.render;
	exports org.inexas.notable.notation.parser;

	opens org.inexas.notable.notation.parser;
	opens org.inexas.notable.notation.model;
}
