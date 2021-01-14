module org.inexas.notable.gui {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.inexas.notable.notation;

	// javafx.graphics needs Access to the Application implementation
	exports org.inexas.notable.gui;
}