package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

class DPhrase {
	private final Layout layout;
	final List<DMeasure> measures = new ArrayList<>();

	DPhrase(final Layout layout, final Phrase phrase) {
		this.layout = layout;
	}

	void add(final DMeasure measure) {
		measures.add(measure);
	}
}
