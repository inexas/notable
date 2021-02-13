/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class Cpm extends Element implements Visited {
	public final int clicks;

	public Cpm(final Messages messages, final int clicks) {
		if(clicks < 1) {
			messages.error("CPM out of range: " + clicks);
		}
		this.clicks = clicks;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return The size of each measure in 1/32nds
	 */
	@SuppressWarnings("unused")
	public int getMeasureSize() {
		return clicks;
	}
}
