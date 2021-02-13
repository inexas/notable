/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class TimeSignature extends Modifier {
	public final static TimeSignature COMMON = new TimeSignature(4, 4);
	public final static TimeSignature fourFour = new TimeSignature(4, 4);
	public final static TimeSignature CUT = new TimeSignature(2, 2);
	public final int numerator;
	public final int denominator;
	public final String name;
	public final boolean isCompound;

	public TimeSignature(final int numerator, final int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		name = String.valueOf(numerator) + '/' + denominator;
		isCompound = numerator % 3 == 0;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return The size of each measure in thirty-seconds
	 */
	public int getMeasureSize() {
		return numerator * Duration.getByDenominator(denominator).clicks;
	}

	Duration getDefaultDuration() {
		return Duration.getByDenominator(denominator);
	}
}
