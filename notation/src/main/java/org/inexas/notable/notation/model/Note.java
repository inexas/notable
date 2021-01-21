/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * A representation of a sound event of a given duration and pitch. A
 * note can be represented either as an octave + tonic e.g. C4 or
 * as a note number: the octave x 12 + tonic where the tonic is a number
 * between 0 and 11 for C..B
 */
public class Note extends Event {
	final static int BASE = 12;
	private final static int MINIMUM = 9;
	final static int MAXIMUM = 96;

	static class SearchSpace {
		private static final int[] lookup = {0, 1, 2, 3, 4, 5, 6, -5, -4, -3, -2, -1};

		private int anchor, anchorTonic;

		SearchSpace(final int noteNumber) {
			setAnchor(noteNumber);
		}

		void setAnchor(final int noteNumber) {
			if(noteNumber < 14) {
				anchor = 14;
			} else if(noteNumber > 90) {
				anchor = 90;
			} else {
				anchor = noteNumber;
			}
			anchorTonic = tonic(anchor);
		}

		int lookup(final int tonic) {
			final int index = (BASE + tonic - anchorTonic) % BASE;
			return anchor + lookup[index];
		}

		void moveAnchor(final int vector) {
			assert vector != 0;
			final boolean up = vector > 0;
			int distance = up ? vector : -vector;
			final int newAnchor = anchor + (up ? 6 : -7);
			distance--;
			setAnchor(newAnchor + (up ? distance * 12 : distance * -12));
		}

		int getAnchor() {
			return anchor;
		}
	}

	/**
	 * Covert an octave and tonic number to note number
	 *
	 * @param octave Octave value 0 to 8
	 * @param tonic  Tonic value  0..11
	 * @return Note number from A0=9, through C4=48, to C8=96
	 */
	public static int number(final int octave, final int tonic) {
		final int returnValue;

		assert tonic >= 0 && tonic < BASE : "tonic out of range: " + tonic;
		assert octave >= 0 & octave < 9 : "octave out of range: " + octave;

		returnValue = octave * BASE + tonic;
		assert returnValue >= MINIMUM && returnValue <= MAXIMUM : "Note number out of range: " + returnValue;

		return returnValue;
	}

	/**
	 * Return the octave given the note number
	 *
	 * @param number Note number, e.g. 21
	 * @return The octave, e.g. 4
	 */
	public static int octave(final int number) {
		assert number >= MINIMUM && number <= MAXIMUM : "Note number out of range: " + number;
		return number / BASE;
	}

	/**
	 * Return the tonic 0..11 given the note number
	 *
	 * @param number Note number, e.g. 21
	 * @return The tonic, e.g. 11
	 */
	public static int tonic(final int number) {
		assert number >= MINIMUM && number <= MAXIMUM : "Note number out of range: " + number;
		return number % BASE;
	}

	public final static int Bs = 0;
	public final static int C = 0;
	public final static int Cs = 1;
	public final static int Db = 1;
	public final static int D = 2;
	public final static int Ds = 3;
	public final static int Eb = 3;
	public final static int E = 4;
	public final static int Fb = 4;
	public final static int Es = 5;
	public final static int F = 5;
	public final static int Fs = 6;
	public final static int Gb = 6;
	public final static int G = 7;
	public final static int Gs = 8;
	public final static int Ab = 8;
	public final static int A = 9;
	public final static int As = 10;
	public final static int Bb = 10;
	public final static int B = 11;
	public final static int Cb = 11;

	final static int C2 = number(2, C);
	final static int C3 = number(3, C);
	public final static int C4 = number(4, C);
	final static int C5 = number(5, C);
	final static int C6 = number(6, C);
	final static int C8 = number(8, C);
	final static int D1 = number(1, D);
	final static int D4 = number(4, D);
	final static int Ds1 = number(1, Ds);
	final static int D3 = number(3, D);
	final static int D5 = number(5, D);
	final static int D6 = number(6, D);
	final static int E1 = number(1, E);
	final static int E4 = number(4, E);
	final static int F1 = number(1, F);
	final static int F2 = number(2, F);
	final static int F3 = number(3, F);
	final static int F4 = number(4, F);
	final static int Fs7 = number(7, Fs);
	final static int G2 = number(2, G);
	final static int G3 = number(3, G);
	final static int Gs1 = number(1, Gs);
	final static int A0 = number(0, A);
	final static int A3 = number(3, A);
	final static int As1 = number(1, As);
	final static int As2 = number(2, As);
	final static int B0 = number(0, B);
	final static int B3 = number(3, B);

	/**
	 * Given a note name like "C#", return the tonic 0..11, e.g. 1
	 */
	private final static Map<String, Integer> lookupTonic = new HashMap<>();

	private static void register(final int tonic, final String... names) {
		for(final String name : names) {
			lookupTonic.put(name, tonic);
		}
	}

	static {
		register(0, "C", "Cn", "B#");
		register(1, "C#", "Db");
		register(2, "D", "Dn");
		register(3, "D#", "Eb");
		register(4, "E", "En");
		register(5, "F", "Fn", "E#");
		register(6, "F#", "Gb");
		register(7, "G", "Gn");
		register(8, "G#", "Ab");
		register(9, "A", "An");
		register(10, "A#", "Bb");
		register(11, "B", "Bn", "Cb");
	}

	String[] names = {
			"C",    // 0
			"C#",   // 1
			"D",    // 2
			"D#",   // 3
			"E",    // 4
			"F",    // 5
			"F#",   // 6
			"G",    // 7
			"G#",   // 8
			"A",    // 9
			"A#",   // 10
			"B",    // 11
	};

	/**
	 * Covert an octave and tonic name to note number
	 *
	 * @param octave Octave value 0 to 8
	 * @param name   E.g. "C", "Bb"
	 * @return Note number from A0, through C4, to C8
	 */
	public static int number(final int octave, final String name) {
		assert octave >= 0 & octave < 9 : "octave out of range: " + octave;

		final Integer tonic = lookupTonic.get(name);
		assert tonic != null : "No such name: " + name;
		return number(octave, tonic);
	}


	public final int tonic;

	/**
	 * The note 'number' is a number between 9 and 97 that represents
	 * the note as a combination of the octave x BASE + note index where
	 * C is 0, C# is 1 etc.
	 */
	public final int number;

	public Note(
			final String name,
			final int number,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(name, duration, annotations);

		assert lookupTonic.containsKey(name) : "No such note name: " + name;
		this.number = number;
		tonic = number % BASE;
	}

	private Note(final Note toCopy) {
		super(toCopy);
		number = toCopy.number;
		tonic = toCopy.tonic;
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
	 * Calculator that given the last note number and a new tonic, calculates
	 * the next number.
	 *
	 * @param lastNote       The previous note number
	 * @param absoluteOctave Octave override using o4. -1 means no override
	 * @param relativeOctave Octave override 0 = none requested otherwise the
	 *                       number will be the count of +'s or -'s in the miki
	 * @param tonicText      The tonic to move to, e.g. "C#"
	 * @return The note number of the next note
	 */
	public static int next(
			final int lastNote,
			final int absoluteOctave,
			final int relativeOctave,
			final String tonicText) {
		final int returnValue;

		final int tonic = lookupTonic.get(tonicText);

		if(absoluteOctave >= 0) {
			// Absolute octave has been specified
			// todo Create exceptions for the following asserts
			assert relativeOctave == 0;
			assert absoluteOctave < 8 || absoluteOctave == 8 && tonic == 1;
			returnValue = absoluteOctave * 12 + tonic;
		} else {
			final SearchSpace ss = new SearchSpace(lastNote);
			if(relativeOctave != 0) {
				ss.moveAnchor(relativeOctave);
			}
			returnValue = ss.lookup(tonic);
		}

		return returnValue;
	}
}
