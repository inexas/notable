/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Something that occupies time
 */
public abstract class Event extends Element implements Visited {
	public final String name;
	public final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
	// todo This should be final
	public Duration duration;
	/**
	 * The note 'slot' is a slot between 9 and 96 that represents
	 * the note as a combination of the octave x BASE + note index where
	 * C is 0, C# is 1 etc.
	 */
	public final int slot;

	Event(
			final String name,
			final int slot,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		assert name != null : "name cannot be null";
		assert duration != null : "duration cannot be null";
		assert annotations != null : "annotations cannot be null but may be empty";

		this.name = name;
		this.duration = duration;
		this.slot = slot;
		this.annotations.putAll(annotations);
	}

	Event(final Duration duration) {
		assert duration != null;
		this.duration = duration;
		name = null;
		slot = -1;
	}

	Event(final Event toCopy) {
		name = toCopy.name;
		slot = toCopy.slot;
		duration = toCopy.duration;
		// todo Copy annotations? If not, why not
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

	public <T> T get(final Class<T> clazz) {
		//noinspection unchecked
		return (T) annotations.get(clazz);
	}

	public String getLabel() {
		return name;
	}
}
