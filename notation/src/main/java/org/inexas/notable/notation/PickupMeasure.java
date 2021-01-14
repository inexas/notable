/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

// todo Not sure if this is needed
public class PickupMeasure extends Miki implements Visited {
	// Implicit 4/4, don't annotate
	public final int numerator;
	public final int denominator;
	public final String name;

	public PickupMeasure(final int numerator, final int denominator) {
		if(!Duration.isDenominator(denominator)) {
			throw new MusicParseException("Invalid denominator", null);
		}
		this.numerator = numerator;
		this.denominator = denominator;
		name = String.valueOf(numerator) + '/' + denominator;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return The size of each measure in thirty-seconds
	 */
	@SuppressWarnings("unused")
	public int getMeasureSize() {
		return numerator * Duration.getByDenominator(denominator).clicks;
	}
}
