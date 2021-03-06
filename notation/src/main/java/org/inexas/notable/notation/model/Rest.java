/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Rest extends Event {

	public Rest(final Duration duration, final Map<Class<? extends Annotation>, Annotation> annotations) {
		super("R", -1, duration, annotations);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
