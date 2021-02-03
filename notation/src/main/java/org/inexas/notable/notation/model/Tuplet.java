/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Tuplet, e.g. Triplet
 */
public class Tuplet extends Event implements Annotation {
	public final List<Event> events;

	public Tuplet(final Duration duration, final List<Event> events) {
		super(duration);
		this.events = events;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Event event : events) {
			event.accept(visitor);
		}
		visitor.exit(this);
	}

	@Override
	public Event copy(final Duration duration) {
		return null;
	}
}
