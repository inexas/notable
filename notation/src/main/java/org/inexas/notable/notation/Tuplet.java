/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

/**
 * Tuplet, e.g. Triplet
 */
class Tuplet extends Event implements Annotation {
	final List<Event> events;

	Tuplet(final Duration duration, final List<Event> events) {
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
	Event copy(final Duration duration) {
		return null;
	}
}
