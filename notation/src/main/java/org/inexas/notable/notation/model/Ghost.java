/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Ghost extends Event {

	public Ghost(
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super("X", -1, duration, annotations);
	}

	private Ghost(final Ghost toCopy) {
		super(toCopy);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new Ghost(this);
		result.duration = duration;
		return result;
	}
}
