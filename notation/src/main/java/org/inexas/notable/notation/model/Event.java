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

	public void add(final Annotation annotation) {
		annotations.put(annotation.getClass(), annotation);
	}

	public <T> T get(final Class<T> clazz) {
		//noinspection unchecked
		return (T) annotations.get(clazz);
	}

	public String getLabel() {
		return name;
	}
}
