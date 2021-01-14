/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

public class Rest extends Event {

	Rest(final Duration duration, final Map<Class<? extends Annotation>, Annotation> annotations) {
		super("r", duration, annotations);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	private Rest(final Rest toCopy) {
		super(toCopy);
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new Rest(this);
		result.duration = duration;
		return result;
	}
}
