/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

/**
 * Connect notes in either a tie or a slur
 */
class Bind extends Line {

	Bind(final int noteCount) {
		super(noteCount, 0);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	int getNoteCount() {
		return bars;
	}
}
