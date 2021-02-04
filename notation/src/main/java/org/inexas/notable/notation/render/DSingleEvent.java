package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

// todo Move to DSingleEvent?
class DSingleEvent {
	private final Layout layout;
	private final Glyph glyph;
	private boolean stem;
	int slot;

	DSingleEvent(final Layout layout, final Event event) {
		this.layout = layout;
		glyph = layout.m.getItemGlyph(event);
		if(event instanceof Rest) {
			stem = false;
		} else {
//			stem = clicks < 16;
		}
	}

	void draw(final GraphicsContext gc) {
		throw new ImplementMeException();
	}
}
