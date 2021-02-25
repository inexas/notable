/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class MultimeasureRest extends Event {
	public final int measureCount;

	public MultimeasureRest(
			final Map<Class<? extends Annotation>, Annotation> annotations,
			final int measureCount) {
		super("M", -1, Duration.whole, annotations);
		assert measureCount >= 2 && measureCount <= 99;
		this.measureCount = measureCount;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
