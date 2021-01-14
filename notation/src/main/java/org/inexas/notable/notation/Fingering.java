/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

public class Fingering extends Miki implements Annotation {
	final String label;

	public Fingering(final String label) {
		this.label = label;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
