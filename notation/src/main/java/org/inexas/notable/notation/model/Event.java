/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Something that occupies time
 */
public abstract class Event extends Miki implements Visited {
	public final String name;
	public final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
	public Duration duration;

	Event(
			final String name,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		assert duration != null;
		this.name = name;
		this.duration = duration;
		this.annotations.putAll(annotations);
		if(annotations.size() != this.annotations.size()) {
			System.out.println("What");
		}
		annotations.clear();
	}

	Event(final Duration duration) {
		assert duration != null;
		this.duration = duration;
		this.name = null;
	}

	Event(final Event toCopy) {
		name = toCopy.name;
		duration = toCopy.duration;
	}

	public void add(final Annotation annotation) {
		annotations.put(annotation.getClass(), annotation);
	}

	/**
	 * Create an Event of the same time but of a different curation
	 *
	 * @param duration The new Duration
	 * @return The new event
	 */
	public abstract Event copy(final Duration duration);
}
