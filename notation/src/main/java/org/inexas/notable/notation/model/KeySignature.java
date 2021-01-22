/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class KeySignature extends Element implements Annotation {
	private final static int FLAT = -1;     // Double flat would be -2, ...
	private final static int NATURAL = 0;
	private final static int SHARP = 1;
	/**
	 * Key is flat, natural, or sharp, e.g. Cb, C or C#
	 */
	private final int accidental;
	/**
	 * See if a note in this key is FLAT, NATURAL or SHARP, enter with tonic
	 */
	private final int[] isAccidental = new int[12];

	/**
	 * This allows the KeySignature to be looked up by the key
	 * name, e.g. C, Cm, C#, Bbm
	 */
	public final static Map<String, KeySignature> map = new HashMap<>();

	public final String name;
	public final int accidentalCount;
	public static KeySignature C = new KeySignature(NATURAL, 0, "C", 0);
	private static final KeySignature S7 = new KeySignature(SHARP, 7, "C#", 1);
	private static final KeySignature F5 = new KeySignature(FLAT, 5, "Db", 1);
	private static final KeySignature S2 = new KeySignature(SHARP, 2, "D", 2);
	private static final KeySignature F3 = new KeySignature(FLAT, 3, "Eb", 3);
	private static final KeySignature S4 = new KeySignature(SHARP, 4, "E", 4);
	private static final KeySignature F1 = new KeySignature(FLAT, 1, "F", 5);
	private static final KeySignature S6 = new KeySignature(SHARP, 6, "F#", 6);
	private static final KeySignature F6 = new KeySignature(FLAT, 6, "Gb", 6);
	private static final KeySignature S1 = new KeySignature(SHARP, 1, "G", 7);
	private static final KeySignature F4 = new KeySignature(FLAT, 4, "Ab", 8);
	private static final KeySignature S3 = new KeySignature(SHARP, 3, "A", 9);
	private static final KeySignature F2 = new KeySignature(FLAT, 2, "Bb", 10);
	private static final KeySignature S5 = new KeySignature(SHARP, 5, "B", 11);
	private static final KeySignature F7 = new KeySignature(FLAT, 7, "Cb", 11);

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

	//  0  1  2  3  4  5  6  7  8  9  10 11
	//  C  C# D  D# E  F  F# G  G# A  A# B
	//                                   F  C  G  D  A  E  B
	private static final int[] sharps = {5, 0, 7, 2, 9, 4, 11};
	//                                  B   E  A  D  G  C  F
	private static final int[] flats = {11, 4, 9, 2, 7, 0, 5};

	private KeySignature(
			final int accidental,
			final int accidentalCount,
			final String name,
			final int offset) {
		this.accidental = accidental;
		this.accidentalCount = accidentalCount;
		this.name = name;

		for(int i = 0; i < 12; i++) {
			//  0  1  2  3  4  5  6  7  8  9  10 11
			//  C  C# D  D# E  F  F# G  G# A  A# B
			//  T     T     T  T     T     T     T
			final int j = (i + offset) % 12;
			switch(i) {
				case 0, 2, 4, 5, 7, 9, 11 -> {
					isDiatonic[j] = true;
					isAccidental[j] = accidental;
				}
				default -> isDiatonic[j] = false;
			}
		}
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Given a note tonic 0..11 test if the note is in the key
	 */
	public final boolean[] isDiatonic = new boolean[12];

	/**
	 * @return true if this key is sharp, e.g. C#
	 */
	public boolean isSharp() {
		return accidental == SHARP;
	}

	/**
	 * @return true if this key is flat, e.g. Cb
	 */
	public boolean isFlat() {
		return accidental == FLAT;
	}

	/**
	 * Generate a lookup table that, given a note, we can look up which position
	 * the note should appear on the 'global' staff. The global staff's position
	 * 0 is A0 and each white note on the piano keyboard occupies the next note.
	 * <p>
	 * The table will go from A0..C8, i.e. 7 octaves plus one. In fact the notes
	 * C0..G#0 are never used as they are outside the range of a piano but we'll
	 * create them anyway to make the table easier to use - it can be entered
	 * directly with the note number
	 *
	 * @param staff    The destination staff of the notes
	 * @param baseline Canvas y position of the bottom line of the staff
	 * @param spacing  Half the line spacing: line to space, to line, ...
	 * @return A position lookup table
	 */
	public double[] yLookup(final Staff staff, final double baseline, final double spacing) {
		final double[] returnValue = new double[8 * 12 + 1];

		int position = isDiatonic[0] ? -1 : 0;
		for(int i = 0; i <= Note.MAXIMUM; i++) {
			if(isDiatonic[i % 12]) {
				position++;
			}
			// There are 56 possible positions
			returnValue[i] = 56 - position;
		}

		// Correct for staff base line
		final double correction = returnValue[staff.baseNote];
		for(int i = 0; i <= Note.MAXIMUM; i++) {
			returnValue[i] =
					(returnValue[i] - correction)
							* spacing
							+ baseline;
		}

		return returnValue;
	}
}
