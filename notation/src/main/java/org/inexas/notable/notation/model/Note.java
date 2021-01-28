/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * A representation of a sound event of a given duration and pitch. A
 * note can be represented either as an octave + tonic e.g. C4 or
 * as a note position: the octave x 12 + tonic where the tonic is a position
 * between 0 and 11 for C..B
 */
public class Note extends Event {
	final static int BASE = 7;
	private final static int MINIMUM = 5;
	public final static int MAXIMUM = 56;

	static class SearchSpace {
		private static final int[] lookup = {0, 1, 2, 3, -3, -2, -1};
		private int anchor, anchorTonic;

		SearchSpace(final int position) {
			setAnchor(position);
		}

		void setAnchor(final int position) {
			if(position < 8) {
				anchor = 8;
			} else if(position > 53) {
				anchor = 53;
			} else {
				anchor = position;
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
	 * Covert an octave and tonic to a position
	 *
	 * @param octave Octave value 0 to 8
	 * @param tonic  Tonic value  0..6
	 * @return Note position from A0=5, through C4=28, to C8=56
	 */
	public static int position(final int octave, final int tonic) {
		final int returnValue;

		assert tonic >= 0 && tonic < BASE : "Tonic out of range: " + tonic;
		assert octave >= 0 & octave < 9 : "Octave out of range: " + octave;

		returnValue = octave * BASE + tonic;
		assert returnValue >= MINIMUM && returnValue <= MAXIMUM : "Note position out of range: " + returnValue;

		return returnValue;
	}

	/**
	 * Return the octave given the note position
	 *
	 * @param position Note position, e.g. 28
	 * @return The octave, e.g. 4
	 */
	public static int octave(final int position) {
		assert position >= MINIMUM && position <= MAXIMUM : "Position out of range: " + position;
		return position / BASE;
	}

	/**
	 * Return the tonic 0..11 given the note position
	 *
	 * @param position Note position, e.g. 28
	 * @return The tonic, e.g. 0
	 */
	public static int tonic(final int position) {
		assert position >= MINIMUM && position <= MAXIMUM : "Note position out of range: " + position;
		return position % BASE;
	}

	public static int position(final int position) {
		assert position >= MINIMUM && position <= MAXIMUM : "Note position out of range: " + position;
		return position % BASE;
	}

	/**
	 * Given a position, return the name.
	 *
	 * @param position The position to resolve, e.g. 48
	 * @return The corresponding name, e.g. C4
	 */
	public static String name(final int position) {
		assert isValid(position);
		return tonicName[position % BASE] + (position / BASE);
	}

	/**
	 * Covert an octave and tonic name to note position
	 *
	 * @param octave Octave value 0 to 8
	 * @param name   E.g. "C", "Bb"
	 * @return Note position from A0, through C4, to C8
	 */
	public static int position(final int octave, final String name) {
		assert octave >= 0 & octave < 9 : "Octave out of range: " + octave;
		return position(octave, getTonic(name));
	}

	private static int getTonic(final String tonicText) {
		int returnValue = tonicText.charAt(0) - 'C';
		if(returnValue < 0) {
			returnValue += 7;
		}
		return returnValue;
	}


	static boolean isValid(final int position) {
		return position >= MINIMUM && position <= MAXIMUM;
	}

	public final static int C = 0;
	public final static int D = 1;
	public final static int E = 2;
	public final static int F = 3;
	public final static int G = 4;
	public final static int A = 5;
	public final static int B = 6;

	final static int C1 = position(1, C);
	final static int C2 = position(2, C);
	final static int C3 = position(3, C);
	public final static int C4 = position(4, C);
	public final static int C5 = position(5, C);
	final static int C6 = position(6, C);
	final static int C8 = position(8, C);
	final static int D1 = position(1, D);
	final static int D4 = position(4, D);
	final static int D3 = position(3, D);
	final static int D5 = position(5, D);
	final static int D6 = position(6, D);
	final static int E1 = position(1, E);
	public final static int E4 = position(4, E);
	final static int F1 = position(1, F);
	final static int F2 = position(2, F);
	final static int F3 = position(3, F);
	final static int F4 = position(4, F);
	public final static int F5 = position(5, F);
	final static int G2 = position(2, G);
	final static int G3 = position(3, G);
	final static int G4 = position(4, G);
	public final static int G5 = position(5, G);
	final static int A0 = position(0, A);
	final static int A3 = position(3, A);
	final static int A5 = position(5, A);
	final static int B0 = position(0, B);
	final static int B3 = position(3, B);
	final static int B5 = position(5, B);

	private static final String[] tonicName = {"C", "D", "E", "F", "G", "A", "B"};

	private static class Key {
		final int position;
		final Duration duration;
		final Map<Class<? extends Annotation>, Annotation> annotations;

		private Key(
				final int position,
				final Duration duration,
				final Map<Class<? extends Annotation>, Annotation> annotations) {
			this.position = position;
			this.duration = duration;
			this.annotations = annotations;
		}
	}

	private final static Map<Key, Note> cache = new HashMap<>();

	/**
	 * Note factory
	 *
	 * @param position    E.g. 48 for C4
	 * @param duration    e.g. Duration.quarter
	 * @param annotations Annotations (not null but empty is OK)
	 * @return The Note
	 */
	public static Note get(
			final int position,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		Note returnValue;

		assert position >= MINIMUM && position <= MAXIMUM : "Position out of range";

		final Key key = new Key(position, duration, annotations);
		returnValue = cache.get(key);
		if(returnValue == null) {
			returnValue = new Note(position, duration, annotations);
			cache.put(key, returnValue);
		}

		return returnValue;
	}

	public static String toName(final int position) {
		return tonicName[position % BASE] + position / BASE;
	}

	/**
	 * The octave position of this note valid both for position and position
	 */
	public final int octave;

	/**
	 * The position representing the tonic independently to the
	 * octave, e.g. C = 0, C#, Db = 1, ...
	 */
	public final int tonic;

	/**
	 * The note 'position' is a position between 9 and 96 that represents
	 * the note as a combination of the octave x BASE + note index where
	 * C is 0, C# is 1 etc.
	 */
	public final int slot;

	private Note(
			final int position,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(toName(position), duration, annotations);

		slot = position;
		octave = position / BASE;
		tonic = position % BASE;
	}

	// todo I think I can get rid of the copy constructors
	private Note(final Note toCopy) {
		super(toCopy);
		slot = toCopy.slot;
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
	 * Calculator that given the last note position and a new tonic, calculates
	 * the next position.
	 *
	 * @param lastPosition   The previous note position
	 * @param absoluteOctave Octave override using 'o4'. -1 means no override
	 * @param relativeOctave Octave override 0 = none requested otherwise the
	 *                       position will be the count of +'s or -'s in the miki
	 * @param tonicText      The tonic to move to, e.g. "C"
	 * @return The note position of the next note
	 */
	public static int next(
			final int lastPosition,
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
			final SearchSpace searchSpace = new SearchSpace(lastPosition);
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
