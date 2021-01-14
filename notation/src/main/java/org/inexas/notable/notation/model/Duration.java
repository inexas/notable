/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import java.util.*;

public class Duration {
	private final static Map<String, Duration> lookupByMiki = new HashMap<>();
	private final static Map<Integer, Duration> lookupByDenominator = new HashMap<>();
	public final static Duration quarter = new Duration(
			"quarter", "4", 4, 8, null);
	final static Duration thirtySecond = new Duration(
			"thirtySecond", "32", 32, 1, null);
	final static Duration sixteenth = new Duration(
			"sixteenth", "16", 16, 2, null);
	@SuppressWarnings("unused")
	final static Duration sixteenthDot = new Duration(
			"sixteenthDot", "16,", 16, 3, sixteenth);
	final static Duration eighth = new Duration(
			"eighth", "8", 8, 4, null);
	@SuppressWarnings("unused")
	final static Duration eighthDot = new Duration(
			"eighthDot", "8,", 8, 6, eighth);
	@SuppressWarnings("unused")
	final static Duration eighthDotDot = new Duration(
			"eighthDotDot", "8,,", 8, 7, eighth);
	@SuppressWarnings("unused")
	final static Duration quarterDot = new Duration(
			"quarterDot", "4,", 4, 12, quarter);
	@SuppressWarnings("unused")
	final static Duration quarterDotDot = new Duration(
			"quarterDotDot", "4,,", 4, 14, quarter);
	final static Duration half = new Duration(
			"half", "2", 2, 16, null);
	@SuppressWarnings("unused")
	final static Duration halfDot = new Duration(
			"halfDot", "2,", 2, 24, half);
	@SuppressWarnings("unused")
	final static Duration halfDotDot = new Duration(
			"halfDotDot", "2,,", 2, 28, half);
	final static Duration whole = new Duration(
			"whole", "1", 1, 32, null);
	/**
	 * This array is used to lookup the combination of notes that will add up to
	 * given total of 1/32's. For example, durations[3] will return the Duration(s)
	 * needed to generate a Note or Rest of 3/32's. From 0..32 are covered, smallest
	 * fraction first by convention. Maximum of two notes, either 0, 1 or two dots.
	 */
	private final static Duration[][] durations = new Duration[][]{
			{},                                         // 0
			{thirtySecond},                             // 1
			{sixteenth},                                // 2
			{thirtySecond, sixteenth},                  // 3
			{eighth},                                   // 4
			{thirtySecond, eighth},                     // 5
			{sixteenth, eighth},                        // 6
			{thirtySecond, sixteenth, eighth},          // 7
			{quarter},                                  // 8
			{thirtySecond, quarter},                    // 9
			{sixteenth, quarter},                       // 10
			{thirtySecond, sixteenth, quarter},         // 11
			{eighth, quarter},                          // 12
			{thirtySecond, eighth, quarter},            // 13
			{sixteenth, eighth, quarter},               // 14
			{thirtySecond, sixteenth, eighth, quarter}, // 15
			{half},                                     // 16
			{thirtySecond, half},                       // 17
			{sixteenth, half},                          // 18
			{thirtySecond, sixteenth, half},            // 19
			{eighth, half},                             // 20
			{thirtySecond, eighth, half},               // 21
			{sixteenth, eighth, half},                  // 22
			{thirtySecond, sixteenth, eighth, half},    // 23
			{quarter, half},                            // 24
			{thirtySecond, quarter, half},              // 25
			{sixteenth, quarter, half},                 // 26
			{thirtySecond, sixteenth, quarter, half},   // 27
			{eighth, quarter, half},                    // 28
			{thirtySecond, eighth, quarter, half},      // 29
			{sixteenth, quarter, half},                 // 30
			{thirtySecond, sixteenth, quarter, half},   // 31
			{whole}                                     // 32
	};
	/**
	 * The duration in 1/32nds, i.e. a quarter note' duration is 8 clicks
	 */
	public final int clicks;
	/**
	 * Duration applies to this and all subsequent Events
	 */
	@SuppressWarnings("WeakerAccess")
	public final boolean setDefault;
	/**
	 * E.g. quarter, half, ...
	 */
	@SuppressWarnings("WeakerAccess")
	final String name;
	/**
	 * E.g. 2 = half, 4 = quarter, ...
	 */
	public final int denominator;
	/**
	 * E.g. "2" for half
	 */
	final String miki;
	/**
	 * true if this Duration is dotted, e.g. 8,
	 */
	final boolean dotted;
	/**
	 * Points to the non-dotted version, e.g. for "8," the root is "8"
	 */
	@SuppressWarnings("WeakerAccess")
	final Duration root;
	private Duration(
			final String name,
			final String miki,
			final int denominator,
			final int clicks,
			final Duration root) {
		this.name = name;
		this.denominator = denominator;
		this.miki = miki;
		if(miki.charAt(miki.length() - 1) == ',') {
			dotted = true;
		} else {
			dotted = false;
			lookupByDenominator.put(denominator, this);
		}
		this.clicks = clicks;
		this.root = root == null ? this : root;
		setDefault = false;

		lookupByMiki.put(miki, this);

		// Create the setDefault version
		new Duration(this);
	}
	private Duration(final Duration toCopy) {
		name = toCopy.name;
		miki = toCopy.miki + '*';
		denominator = toCopy.denominator;
		clicks = toCopy.clicks;
		root = toCopy.root;
		dotted = toCopy.dotted;
		setDefault = true;
		lookupByMiki.put(miki, this);
	}

	public static Duration getByDenominator(final int denominator) {
		return lookupByDenominator.get(denominator);
	}

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

	public static boolean isDenominator(final int candidate) {
		return candidate == 1 ||
				candidate == 2 ||
				candidate == 4 ||
				candidate == 8 ||
				candidate == 16 ||
				candidate == 32;
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
