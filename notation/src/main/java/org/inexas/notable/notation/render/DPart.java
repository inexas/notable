package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

class DPart {
	private final Layout layout;
	final List<DPhrase> phrases = new ArrayList<>();
	String name;

	DPart(final Layout layout, final Part part) {
		this.layout = layout;
	}

	void add(final DPhrase dPhrase) {
		phrases.add(dPhrase);
	}
}
