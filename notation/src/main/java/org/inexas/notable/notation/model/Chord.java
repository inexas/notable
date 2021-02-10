/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Chord extends Event implements Venue {
	public final List<Note> notes = new ArrayList<>();

	public Chord(final Duration duration) {
		super(duration);
	}

	@Override
	public void add(final Event event) {
		notes.add((Note) event);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Note note : notes) {
			note.accept(visitor);
		}
		visitor.exit(this);
	}
}
