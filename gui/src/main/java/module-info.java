module org.inexas.notable.gui {
	requires javafx.controls;
	requires javafx.fxml;
	// Not sure why this needs to be exported, javafx.graphics needs it
	exports org.inexas.notable.gui;
}