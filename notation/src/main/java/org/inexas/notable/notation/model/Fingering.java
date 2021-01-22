/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class Fingering extends Element implements Annotation {
	public final String label;

	public Fingering(final String label) {
		this.label = label;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
