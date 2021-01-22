/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

public abstract class Line extends Element implements Annotation {
	public final int beats;
	public final int bars;

	Line(final int bars, final int beats) {
		this.bars = bars;
		this.beats = beats;
	}
}
