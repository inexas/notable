/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class PickupMeasure extends Element implements Visited {
	public final Fraction fraction;

	public PickupMeasure(final String fraction) {
		this.fraction = new Fraction(fraction);
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
		return fraction.numerator * Duration.getByDenominator(fraction.denominator).clicks;
	}
}
