/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Chord extends Event {
	public final List<Note> notes = new ArrayList<>();

	public Chord(
			final Duration duration,
			final List<Event> events,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super("<Chord>", -1, duration, annotations);
		// todo Trap Rest, Ghosts....
		for(final Event event : events) {
			notes.add((Note) event);
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
	public Event copy(final Duration duration) {
		final Event result = new Chord(this);
		result.duration = duration;
		return result;
	}
}
