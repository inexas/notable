/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

import static org.inexas.notable.notation.model.KeySignature.State.*;

public class KeySignature extends Element implements Annotation {
	public enum State {chromatic, natural, sharp, flat}

	private final State keyState;
	private final State[] tonicState = new State[12];

	//  0  1  2  3  4  5  6  7  8  9  10 11
	//  C  C# D  D# E  F  F# G  G# A  A# B
	//                                   F  C  G  D  A  E  B
	private static final int[] sharps = {5, 0, 7, 2, 9, 4, 11};
	//                                  B   E  A  D  G  C  F
	private static final int[] flats = {11, 4, 9, 2, 7, 0, 5};

	private final boolean[] isNatural = {
			// C         D            E     F            G            A            B
			true, false, true, false, true, true, false, true, false, true, false, true
	};

	/**
	 * This allows the KeySignature to be looked up by the key
	 * name, e.g. C, Cm, C#, Bbm
	 */
	private final static Map<String, KeySignature> map = new HashMap<>();

	public final String name;
	public final int accidentalCount;
	public static KeySignature C = new KeySignature(natural, 0, "C", 0);
	private static final KeySignature S7 = new KeySignature(sharp, 7, "C#", 1);
	private static final KeySignature F5 = new KeySignature(flat, 5, "Db", 1);
	private static final KeySignature S2 = new KeySignature(sharp, 2, "D", 2);
	private static final KeySignature F3 = new KeySignature(flat, 3, "Eb", 3);
	private static final KeySignature S4 = new KeySignature(sharp, 4, "E", 4);
	private static final KeySignature F1 = new KeySignature(flat, 1, "F", 5);
	private static final KeySignature S6 = new KeySignature(sharp, 6, "F#", 6);
	private static final KeySignature F6 = new KeySignature(flat, 6, "Gb", 6);
	private static final KeySignature S1 = new KeySignature(sharp, 1, "G", 7);
	private static final KeySignature F4 = new KeySignature(flat, 4, "Ab", 8);
	private static final KeySignature S3 = new KeySignature(sharp, 3, "A", 9);
	private static final KeySignature F2 = new KeySignature(flat, 2, "Bb", 10);
	private static final KeySignature S5 = new KeySignature(sharp, 5, "B", 11);
	private static final KeySignature F7 = new KeySignature(flat, 7, "Cb", 11);

	static {
		map.put("C", C);

		map.put("G", S1);
		map.put("D", S2);
		map.put("A", S3);
		map.put("E", S4);
		map.put("B", S5);
		map.put("F#", S6);
		map.put("C#", S7);

		map.put("F", F1);
		map.put("Bb", F2);
		map.put("Eb", F3);
		map.put("Ab", F4);
		map.put("Db", F5);
		map.put("Gb", F6);
		map.put("Cb", F7);

		map.put("Am", C);

		map.put("Em", S1);
		map.put("Bm", S2);
		map.put("F#m", S3);
		map.put("C#m", S4);
		map.put("G#m", S5);
		map.put("D#m", S6);
		map.put("A#m", S7);

		map.put("Dm", F1);
		map.put("Gm", F2);
		map.put("Cm", F3);
		map.put("Fm", F4);
		map.put("Bbm", F5);
		map.put("Ebm", F6);
		map.put("Abm", F7);
	}

	public static KeySignature get(final String name) {
		return map.get(name);
	}

	private KeySignature(
			final State keyState,
			final int accidentalCount,
			final String name,
			final int offset) {
		this.keyState = keyState;
		this.accidentalCount = accidentalCount;
		this.name = name;

		// Mark all the chromatic (non-diatonic) and diatonic notes...
		for(int i = 0; i < 12; i++) {
			final int index = (offset + i) % 12;
			//  0  1  2  3  4  5  6  7  8  9  10 11
			//  C  C# D  D# E  F  F# G  G# A  A# B
			//  T     T     T  T     T     T     T
			switch(i) {
				case 0, 2, 4, 5, 7, 9, 11 -> tonicState[index] = natural;
				default -> tonicState[index] = chromatic;
			}
		}

		// Now correct all the sharps/flats
		final int[] altered = keyState == sharp ? sharps : flats;
		for(int i = 0; i < accidentalCount; i++) {
			final int index = (altered[i] + offset) % 12;
			tonicState[index] = keyState;
		}
	}

	private State getTonicState(final int number) {
		assert Note.isValid(number);
		return tonicState[number % 12];
	}

	/**
	 * @return true if this key is sharp, e.g. C#
	 */
	public boolean isSharp() {
		return keyState == sharp;
	}

	/**
	 * @return true if this key is flat, e.g. Cb
	 */
	public boolean isFlat() {
		return keyState == flat;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Generate a lookup table that, given a position, can be used look up which
	 * y coordinate the note should appear on the canvas. The global staff's
	 * position 0 is A0 and each white note on the piano keyboard occupies the
	 * next position.
	 * <p>
	 * The table maps A0..C8, i.e. 7 octaves plus one. The notes C0..G#0 are
	 * never used as they are outside the range of a piano but we create them
	 * anyway to make the table easier to use - it can be entered
	 * directly with the note position
	 *
	 * @param staff    The destination staff of the notes
	 * @param baseline Canvas y position of the bottom line of the staff
	 * @param spacing  Half the line spacing: line to space, to line, ...
	 * @return A table to covert position to canvas y coordinate
	 */
	public double[] yLookup(final Staff staff, final double baseline, final double spacing) {
		final double[] returnValue = new double[8 * 7 + 1];

		for(int i = 0; i <= Note.MAXIMUM; i++) {
			returnValue[i] = (staff.lowLinePosition - i) * spacing + baseline;
		}
		return returnValue;
	}
}
