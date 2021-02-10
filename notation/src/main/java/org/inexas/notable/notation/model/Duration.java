/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import java.util.*;

public class Duration {
	private final static Map<String, Duration> lookupByMiki = new HashMap<>();
	private final static Map<Integer, Duration> lookupByDenominator = new HashMap<>();

	final static Duration thirtySecond = new Duration("thirtySecond", 32, 0, null);

	final static Duration sixteenth = new Duration("sixteenth", 16, 0, null);
	final static Duration sixteenthDot = new Duration("sixteenthDot", 16, 1, sixteenth);

	public final static Duration eighth = new Duration("eighth", 8, 0, null);
	public final static Duration eighthDot = new Duration("eighthDot", 8, 1, eighth);
	final static Duration eighthDotDot = new Duration("eighthDotDot", 8, 2, eighth);

	public final static Duration quarter = new Duration("quarter", 4, 0, null);
	final static Duration quarterDot = new Duration("quarterDot", 4, 1, quarter);
	final static Duration quarterDotDot = new Duration("quarterDotDot", 4, 2, quarter);
	final static Duration quarterDotDotDot = new Duration("quarterDotDotDot", 4, 3, quarter);

	public final static Duration half = new Duration("half", 2, 0, null);
	public final static Duration halfDot = new Duration("halfDot", 2, 1, half);
	final static Duration halfDotDot = new Duration("halfDotDot", 2, 2, half);
	final static Duration halfDotDotDot = new Duration("halfDotDotDot", 2, 3, half);
	final static Duration halfDotDotDotDot = new Duration("halfDotDotDotDot", 2, 4, half);

	public final static Duration whole = new Duration("whole", 1, 0, null);

	/**
	 * This array is used to lookup the combination of notes that will add up to
	 * given total of 1/32's. For example, durations[3] will return the Duration(s)
	 * needed to generate a Note or Rest of 3/32's. From 0..32 are covered, smallest
	 * fraction first by convention. Maximum of two notes, either 0, 1 or two dots.
	 */
	private final static Duration[][] durations = new Duration[][]{
			{},                             // 0
			{thirtySecond},                 // 1
			{sixteenth},                    // 2
			{sixteenthDot},                 // 3
			{eighth},                       // 4
			{thirtySecond, eighth},         // 5
			{eighthDot},                    // 6
			{eighthDotDot},                 // 7
			{quarter},                      // 8
			{thirtySecond, quarter},        // 9
			{sixteenth, quarter},           // 10
			{sixteenthDot, quarter},        // 11
			{quarterDot},                   // 12
			{thirtySecond, quarterDot},     // 13
			{quarterDotDot},                // 14
			{quarterDotDotDot},             // 15
			{half},                         // 16
			{thirtySecond, half},           // 17
			{sixteenth, half},              // 18
			{sixteenthDot, half},           // 19
			{eighth, half},                 // 20
			{thirtySecond, eighth, half},   // 21
			{eighthDot, half},              // 22
			{eighthDotDot, half},           // 23
			{halfDot},                      // 24
			{thirtySecond, halfDot},        // 25
			{sixteenth, halfDot},           // 26
			{sixteenthDot, halfDot},        // 27
			{halfDotDot},                   // 28
			{thirtySecond, halfDotDot},     // 29
			{halfDotDotDot},                // 30
			{halfDotDotDotDot},             // 31
			{whole}                         // 32
	};

	static {
		lookupByDenominator.put(1, Duration.whole);
		lookupByDenominator.put(2, Duration.half);
		lookupByDenominator.put(4, Duration.quarter);
		lookupByDenominator.put(8, Duration.eighth);
		lookupByDenominator.put(16, Duration.sixteenth);
		lookupByDenominator.put(32, Duration.thirtySecond);
	}

	/**
	 * E.g. quarter, half, ...
	 */
	final String name;
	/**
	 * E.g. 2 = half, 4 = quarter, ...
	 */
	public final int denominator;
	/**
	 * The duration in 1/32nds, i.e. a quarter note' duration is 8 clicks
	 */
	public final int clicks;
	/**
	 * Duration applies to this and all subsequent Events
	 */
	public final boolean setDefault;
	/**
	 * E.g. "2" for half
	 */
	public final String miki;
	/**
	 * Number of dots, e.g. "4.." has two
	 */
	public final int dots;
	/**
	 * Points to the non-dotted version, e.g. for "8," the root is "8"
	 */
	final Duration root;

	private Duration(final String name, final int denominator, final int dots, final Duration root) {
		this.name = name;
		this.denominator = denominator;
		this.dots = dots;
		this.root = root == null ? this : root;

		clicks = applyDots(denominator, dots);
		miki = denominator + "....".substring(4 - dots);
		setDefault = false;
		lookupByMiki.put(miki, this);
		new Duration(this); // Create the setDefault version
	}

	private Duration(final Duration toCopy) {
		name = toCopy.name;
		miki = toCopy.miki + '*';
		denominator = toCopy.denominator;
		clicks = toCopy.clicks;
		root = toCopy.root;
		dots = toCopy.dots;
		setDefault = true;
		lookupByMiki.put(miki, this);
	}

	/**
	 * @param denominator A denominator, e.g. 4 for a quarter note
	 * @return The corresponding Duration or null if not found
	 */
	public static Duration getByDenominator(final int denominator) {
		return lookupByDenominator.get(denominator);
	}

	/**
	 * @param miki A miki string, e.g. "4.*"" for a dotted
	 *             quarter note that should become the default Duration
	 * @return The corresponding Duration or null if not found
	 */
	public static Duration getByMiki(final String miki) {
		final Duration result = lookupByMiki.get(miki);
		if(result == null) {
			throw new RuntimeException("No such Duration: " + miki);
		}
		return result;
	}

	/**
	 * Return an array of Durations that's total duration measured in 1/32s
	 * is equal a given number between 0 and 32 inclusive
	 *
	 * @param clicks The required total duration
	 * @return An array of length 1 or 2 Durations
	 */
	public static Duration[] getByClicks(final int clicks) {
		assert clicks != 0 && clicks < durations.length : "Deranged clicks: " + clicks;
		return durations[clicks];
	}

	private static int applyDots(final int denominator, final int dots) {
		int result;

		int clicks = 32 / denominator;
		result = clicks;
		for(int i = 0; i < dots; i++) {
			clicks = clicks / 2;
			result += clicks;
		}

		return result;
	}

	@Override
	public int hashCode() {
		return clicks;
	}

	@Override
	public final boolean equals(final Object rhsObject) {
		final boolean result;

		if(rhsObject == this) {
			result = true;
		} else if(rhsObject == null) {
			result = false;
		} else if(rhsObject.getClass() == Duration.class) {
			final Duration rhs = (Duration) rhsObject;
			result = clicks == rhs.clicks;
		} else {
			result = false;
		}

		return result;
	}

	@Override
	public String toString() {
		return name;
	}
}
