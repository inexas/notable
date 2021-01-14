/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * Connect notes in either a tie or a slur
 */
public class Bind extends Line {

	public Bind(final int noteCount) {
		super(noteCount, 0);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	public int getNoteCount() {
		return bars;
	}
}
