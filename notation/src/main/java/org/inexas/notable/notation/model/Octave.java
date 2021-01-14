/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * todo Not quite sure what this is, probably and octave up or down?
 */
public class Octave extends Line {
	public final int noteCount;

	public Octave(final int noteCount, final int bars, final int beats) {
		super(bars, beats);
		this.noteCount = noteCount;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
