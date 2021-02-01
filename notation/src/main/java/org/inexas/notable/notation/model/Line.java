/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

/**
 * Concrete classes of Lines are anchored to a given Event and
 * extend for a number of measures and beats
 * <p>
 * todo Change beats into clicks
 */
public abstract class Line extends Element implements Annotation {
	public final int beats;
	public final int bars;

	Line(final int bars, final int beats) {
		this.bars = bars;
		this.beats = beats;
	}
}
