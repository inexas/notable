/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

/**
 * Pedal down
 */
public class Pedal extends Line {
	public Pedal(final int bars, final int beats) {
		super(bars, beats);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
