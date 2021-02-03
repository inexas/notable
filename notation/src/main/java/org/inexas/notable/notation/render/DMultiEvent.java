package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

class DMultiEvent extends DEvent {
	private final DSingleEvent[] events;

	DMultiEvent(final Event event) {
		super(event);

		throw new ImplementMeException();
//			final List<? extends Event> modelEvents = tuplet.events;
//			int count = modelEvents.size();
//			events = new DSingleEvent[count];
//			for(int i = 0; i < count; i++) {
//				events[i] = new DSingleEvent(modelEvents.get(i));
//			}
	}

	@Override
	void draw() {
		for(final DEvent event : events) {
			event.draw();
		}
	}
}
