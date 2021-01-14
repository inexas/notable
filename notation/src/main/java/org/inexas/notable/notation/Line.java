/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

abstract class Line extends Miki implements Annotation {
	final int beats;
	final int bars;

	Line(final int bars, final int beats) {
		this.bars = bars;
		this.beats = beats;
	}
}
