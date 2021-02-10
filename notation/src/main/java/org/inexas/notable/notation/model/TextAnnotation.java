/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class TextAnnotation extends Annotation {
	public final String text;

	public TextAnnotation(final String text) {
		this.text = text;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
