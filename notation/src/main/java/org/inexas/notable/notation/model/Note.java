/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * A representation of a sound event of a given duration and pitch. A
 * note can be represented either as an octave + tonic e.g. C4 or
 * as a note slot: the octave x 12 + tonic where the tonic is a slot
 * between 0 and 11 for C..B
 */
public class Note extends Event {
	final static int BASE = 7;
	private final static int MINIMUM = 5;
	public final static int MAXIMUM = 56;

	static class SearchSpace {
		private static final int[] lookup = {0, 1, 2, 3, -3, -2, -1};
		private int anchor, anchorTonic;

		SearchSpace(final int slot) {
			setAnchor(slot);
		}

		void setAnchor(final int slot) {
			if(slot < 8) {
				anchor = 8;
			} else if(slot > 53) {
				anchor = 53;
			} else {
				anchor = slot;
			}
			anchorTonic = tonic(anchor);
		}

		int lookup(final int tonic) {
			return anchor + lookup[(BASE + tonic - anchorTonic) % BASE];
		}

		/**
		 * Move the SearchSpace up or down by a given amount
		 *
		 * @param vector E.g. '++' gives a vector of +2
		 */
		void moveAnchor(final int vector) {
			assert vector != 0;

			final boolean up = vector > 0;
			int distance = up ? vector : -vector;
			final int newAnchor = anchor + (up ? 4 : -5);
			distance--;
			setAnchor(newAnchor + (up ? distance * BASE : distance * -BASE));
		}

		int getAnchor() {
			return anchor;
		}
	}

	private static final String[] labels = {"C", "D", "E", "F", "G", "A", "B"};

	/**
	 * Covert an octave and tonic to a slot
	 *
	 * @param octave Octave value 0 to 8
	 * @param tonic  Tonic value  0..6
	 * @return Note slot from A0=5, through C4=28, to C8=56
	 */
	static int slot(final int octave, final int tonic) {
		final int returnValue;

		assert tonic >= 0 && tonic < BASE : "Tonic out of range: " + tonic;
		assert octave >= 0 & octave < 9 : "Octave out of range: " + octave;

		returnValue = octave * BASE + tonic;
		assert returnValue >= MINIMUM && returnValue <= MAXIMUM : "Note slot out of range: " + returnValue;

		return returnValue;
	}

	/**
	 * Return the octave given the note slot
	 *
	 * @param slot Note slot, e.g. 28
	 * @return The octave, e.g. 4
	 */
	public static int octave(final int slot) {
		assert slot >= MINIMUM && slot <= MAXIMUM : "Slot out of range: " + slot;
		return slot / BASE;
	}

	/**
	 * Return the tonic 0..11 given the note slot
	 *
	 * @param slot Note slot, e.g. 28
	 * @return The tonic, e.g. 0
	 */
	public static int tonic(final int slot) {
		assert slot >= MINIMUM && slot <= MAXIMUM : "Note slot out of range: " + slot;
		return slot % BASE;
	}

	/**
	 * Given a slot, return the name.
	 *
	 * @param slot The slot to resolve, e.g. 48
	 * @return The corresponding name, e.g. C4
	 */
	public static String name(final int slot) {
		assert isValid(slot);
		return tonicName[slot % BASE] + (slot / BASE);
	}

	/**
	 * Covert an octave and tonic name to note slot
	 *
	 * @param octave Octave value 0 to 8
	 * @param name   E.g. "C", "Bb"
	 * @return Note slot from A0, through C4, to C8
	 */
	static int slot(final int octave, final String name) {
		assert octave >= 0 & octave < 9 : "Octave out of range: " + octave;
		return slot(octave, getTonic(name));
	}

	private static int getTonic(final String tonicText) {
		int returnValue = tonicText.charAt(0) - 'C';
		if(returnValue < 0) {
			returnValue += 7;
		}
		return returnValue;
	}


	static boolean isValid(final int slot) {
		return slot >= MINIMUM && slot <= MAXIMUM;
	}

	public final static int C = 0;
	final static int D = 1;
	public final static int E = 2;
	public final static int F = 3;
	public final static int G = 4;
	public final static int A = 5;
	final static int B = 6;

	final static int C2 = slot(2, C);
	final static int C3 = slot(3, C);
	public final static int C4 = slot(4, C);
	final static int C5 = slot(5, C);
	final static int C6 = slot(6, C);
	final static int C8 = slot(8, C);
	final static int D1 = slot(1, D);
	final static int D4 = slot(4, D);
	final static int D3 = slot(3, D);
	final static int D5 = slot(5, D);
	final static int D6 = slot(6, D);
	final static int E1 = slot(1, E);
	final static int E3 = slot(3, E);
	final static int E4 = slot(4, E);
	final static int E5 = slot(5, E);
	final static int F1 = slot(1, F);
	final static int F2 = slot(2, F);
	public final static int F3 = slot(3, F);
	public final static int F4 = slot(4, F);
	public final static int F5 = slot(5, F);
	public final static int G2 = slot(2, G);
	final static int G3 = slot(3, G);
	public final static int G4 = slot(4, G);
	final static int G5 = slot(5, G);
	final static int A0 = slot(0, A);
	final static int A2 = slot(2, A);
	public final static int A3 = slot(3, A);
	final static int A4 = slot(4, A);
	final static int A5 = slot(5, A);
	final static int B0 = slot(0, B);
	public final static int B3 = slot(3, B);
	final static int B2 = slot(2, B);
	final static int B4 = slot(4, B);
	final static int B5 = slot(5, B);

	private static final String[] tonicName = {"C", "D", "E", "F", "G", "A", "B"};

	private static class Key {
		final int slot;
		final Duration duration;
		final Map<Class<? extends Annotation>, Annotation> annotations;

		private Key(
				final int slot,
				final Duration duration,
				final Map<Class<? extends Annotation>, Annotation> annotations) {
			this.slot = slot;
			this.duration = duration;
			this.annotations = annotations;
		}
	}

	private final static Map<Key, Note> cache = new HashMap<>();

	/**
	 * Note factory
	 *
	 * @param slot        E.g. 48 for C4
	 * @param duration    e.g. Duration.quarter
	 * @param annotations Annotations (not null but empty is OK)
	 * @return The Note
	 */
	public static Note get(
			final int slot,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		Note returnValue;

		assert slot >= MINIMUM && slot <= MAXIMUM : "Slot out of range";

		final Key key = new Key(slot, duration, annotations);
		returnValue = cache.get(key);
		if(returnValue == null) {
			returnValue = new Note(slot, duration, annotations);
			cache.put(key, returnValue);
		}

		return returnValue;
	}

	public static String toName(final int slot) {
		return tonicName[slot % BASE] + slot / BASE;
	}

	/**
	 * The octave slot of this note valid both for slot and slot
	 */
	public final int octave;

	/**
	 * The slot representing the tonic independently to the
	 * octave, e.g. C = 0, C#, Db = 1, ...
	 */
	public final int tonic;


	private Note(
			final int slot,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(toName(slot), slot, duration, annotations);

		octave = slot / BASE;
		tonic = slot % BASE;
	}

	// todo I think I can get rid of the copy constructors
	private Note(final Note toCopy) {
		super(toCopy);
		tonic = toCopy.tonic;
		octave = toCopy.octave;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new Note(this);
		result.duration = duration;
		return result;
	}

	/**
	 * Calculator that given the last note slot and a new tonic, calculates
	 * the next slot.
	 *
	 * @param lastSlot       The previous note slot
	 * @param absoluteOctave Octave override using 'o4'. -1 means no override
	 * @param relativeOctave Octave override 0 = none requested otherwise the
	 *                       slot will be the count of +'s or -'s in the miki
	 * @param tonicText      The tonic to move to, e.g. "C"
	 * @return The note slot of the next note
	 */
	public static int next(
			final int lastSlot,
			final int absoluteOctave,
			final int relativeOctave,
			final String tonicText) {
		final int returnValue;

		final int tonic = getTonic(tonicText);

		if(absoluteOctave >= 0) {
			// Absolute octave has been specified
			assert relativeOctave == 0;
			assert absoluteOctave < 8 || absoluteOctave == 8 && tonic == 0;
			returnValue = absoluteOctave * BASE + tonic;
		} else {
			final SearchSpace searchSpace = new SearchSpace(lastSlot);
			if(relativeOctave != 0) {
				searchSpace.moveAnchor(relativeOctave);
			}
			returnValue = searchSpace.lookup(tonic);
		}

		return returnValue;
	}

	@Override
	public String getLabel() {
		return labels[tonic];
	}
}
