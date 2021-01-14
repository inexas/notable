/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

public class Ghost extends Event {

	Ghost(
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super("x", duration, annotations);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	private Ghost(final Ghost toCopy) {
		super(toCopy);
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new Ghost(this);
		result.duration = duration;
		return result;
	}
}
