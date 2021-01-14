/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

public class BarRest extends Line {
	public BarRest(final int bars, final int beats) {
		super(bars, beats);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
