/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

class Chord extends Event {
	final List<Note> notes = new ArrayList<>();

	Chord(
			final Duration duration,
			final List<Event> events,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(null, duration, annotations);
		// todo Trap Rest, Ghosts....
		for(final Event event : events) {
			notes.add((Note)event);
		}
	}

	private Chord(final Chord toCopy) {
		super(toCopy);
		notes.addAll(toCopy.notes);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Note note : notes) {
			note.accept(visitor);
		}
		visitor.exit(this);
	}

	@Override
	Event copy(final Duration duration) {
		final Event result = new Chord(this);
		result.duration = duration;
		return result;
	}
}
