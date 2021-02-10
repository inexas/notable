/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Tuplet, e.g. Triplet
 */
public class Tuplet extends Event implements Venue {
	public final List<Event> events = new ArrayList();

	public Tuplet(final Duration duration) {
		super(duration);
	}

	@Override
	public void add(final Event event) {
		events.add(event);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Event event : events) {
			event.accept(visitor);
		}
		visitor.exit(this);
	}
}
