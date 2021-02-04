package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

class DPhrase {
	private final Layout layout;
	String name;
	DEvent[] events;

	DPhrase(final Layout layout, final Phrase phrase, final boolean singleton) {
		this.layout = layout;
		//noinspection StringEquality
		if(singleton && phrase.name == Phrase.IMPLICIT) {
			name = null;
		} else {
			name = StringU.nullOrText(phrase.name);
		}

		final int count = phrase.events.size();
		events = new DEvent[count];
		for(int i = 0; i < count; i++) {
			final DEvent dEvent;
			final Event event = phrase.events.get(i);
			if(event instanceof Note ||
					event instanceof Rest ||
					event instanceof Ghost) {
//				dEvent = new DSingleEvent(layout, event);
			} else {
//				dEvent = new DMultiEvent(event);
			}
//			events[i] = dEvent;
		}
	}
}
