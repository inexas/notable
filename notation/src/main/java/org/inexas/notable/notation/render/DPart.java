package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

class DPart {
	private final Layout layout;
	public String name;
	final DPhrase[] phrases;

	DPart(final Layout layout, final Part part, final boolean singleton) {
		this.layout = layout;
		//noinspection StringEquality
		if(singleton && part.name == Part.IMPLICIT) {
			name = null;
		} else {
			name = StringU.nullOrText(part.name);
		}

		final int phraseCount = part.phraseMap.size();
		if(phraseCount > 2) {
			throw new RuntimeException("Too many Phrases");
		}
		phrases = new DPhrase[phraseCount];
		int index = 0;
		for(final Phrase phrase : part.phraseMap.values()) {
			final DPhrase dPhrase = new DPhrase(layout, phrase, part.phraseMap.size() == 1);
			phrases[index++] = dPhrase;
		}
	}
}
