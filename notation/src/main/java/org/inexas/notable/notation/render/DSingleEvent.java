package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

// todo Move to DSingleEvent?
class DSingleEvent extends DEvent {
	private final Layout layout;
	private final Glyph glyph;
	private final boolean stem;
	int slot;

	DSingleEvent(final Layout layout, final Event event) {
		super(event);
		this.layout = layout;
		glyph = layout.m.getItemGlyph(event);
		if(event instanceof Rest) {
			stem = false;
		} else {
			stem = clicks < 16;
		}
	}

	@Override
	void draw() {
		throw new ImplementMeException();
	}
}
