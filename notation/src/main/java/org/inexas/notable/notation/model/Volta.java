/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * A repeat sign is a sign that indicates a section should be repeated.
 */
public class Volta extends Line {
	public final int ordinal;

	public Volta(final int ordinal, final int bars, final int beats) {
		super(bars, beats);
		this.ordinal = ordinal;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
