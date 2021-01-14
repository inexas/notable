/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

public class TimeSignature extends Miki implements Visited, Annotation {
	public final static TimeSignature COMMON = new TimeSignature(4, 4);
	public final static TimeSignature CUT = new TimeSignature(2, 2);
	// Implicit 4/4, don't annotate
	public final static TimeSignature DEFAULT = new TimeSignature(4, 4);
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
}
