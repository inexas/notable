/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

import java.util.*;
import java.util.regex.*;

import static org.inexas.notable.notation.model.KeySignature.State.*;

public class KeySignature extends Modifier {
	public enum State {
		sharp(0),
		flat(1),
		natural(2);

		public final int index;

		State(final int index) {
			this.index = index;
		}
	}

	private final State state;

	/**
	 * This allows the KeySignature to be looked up by the key
	 * name, e.g. C, Cm, C#, Bbm
	 */
	private final static Map<String, KeySignature> map = new HashMap<>();

	public static KeySignature C = new KeySignature(natural, 0, "C");
	private static final KeySignature S7 = new KeySignature(sharp, 7, "C#");
	private static final KeySignature F5 = new KeySignature(flat, 5, "Db");
	private static final KeySignature S2 = new KeySignature(sharp, 2, "D");
	private static final KeySignature F3 = new KeySignature(flat, 3, "Eb");
	private static final KeySignature S4 = new KeySignature(sharp, 4, "E");
	private static final KeySignature F1 = new KeySignature(flat, 1, "F");
	private static final KeySignature S6 = new KeySignature(sharp, 6, "F#");
	private static final KeySignature F6 = new KeySignature(flat, 6, "Gb");
	private static final KeySignature S1 = new KeySignature(sharp, 1, "G");
	private static final KeySignature F4 = new KeySignature(flat, 4, "Ab");
	private static final KeySignature S3 = new KeySignature(sharp, 3, "A");
	private static final KeySignature F2 = new KeySignature(flat, 2, "Bb");
	private static final KeySignature S5 = new KeySignature(sharp, 5, "B");
	private static final KeySignature F7 = new KeySignature(flat, 7, "Cb");

	// todo Figure out how to handle A# and the like
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

	private final int[][][] accidentals = {{
			// alto
			{Notes.F4, Notes.C4, Notes.G4, Notes.D4, Notes.A3, Notes.E4, Notes.B3},
			{Notes.B3, Notes.E4, Notes.A3, Notes.D4, Notes.G3, Notes.C4, Notes.F3}
	}, {    // bass
			{Notes.F3, Notes.C3, Notes.G3, Notes.D3, Notes.A2, Notes.E3, Notes.B2},
			{Notes.B2, Notes.E3, Notes.A2, Notes.D3, Notes.G2, Notes.C3, Notes.F2}
	}, {    // tenor
			{Notes.F3, Notes.C4, Notes.G3, Notes.D4, Notes.A3, Notes.E4, Notes.B3},
			{Notes.B3, Notes.E4, Notes.A3, Notes.D4, Notes.G3, Notes.C4, Notes.F3}
	}, {    // treble
			{Notes.F5, Notes.C5, Notes.G5, Notes.D5, Notes.A4, Notes.E5, Notes.B4},
			{Notes.B4, Notes.E5, Notes.A4, Notes.D5, Notes.G4, Notes.C5, Notes.F4}
	}};

	/**
	 * @param clef The clef of the staff
	 * @return The list of slots that should be marked with
	 * an accidental for a given clef
	 */
	public int[] getAccidentals(final Clef clef) {
		throw new ImplementMeException();
//		return Arrays.copyOfRange(
//				accidentals[clef.index][state.index],
//				0,
//				accidentalCount);
	}

	public static KeySignature get(final String name) {
		return map.get(name);
	}

	public final String name;
	public final int accidentalCount;

	private KeySignature(
			final State keyState,
			final int accidentalCount,
			final String name) {
		state = keyState;
		this.accidentalCount = accidentalCount;
		this.name = name;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	public boolean isFlat() {
		return state == flat;
	}

	public boolean isNatural() {
		return state == natural;
	}

	public boolean isSharp() {
		return state == sharp;
	}

	private final static Pattern keySignaturePattern = Pattern.compile("" +
			"([A-G])" +         // Tonic
			"([b#n])?" +        // Accidental
			"([Mm])?");         // Major/minor

	public static KeySignature parseKeySignature(final String input) {
		final KeySignature result;

		final Matcher matcher = keySignaturePattern.matcher(input);
		String tonic;
		@SuppressWarnings("unused")
		String mode = null;
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 1: Upper case tonic...
		final String group1 = matcher.group(1);
		tonic = group1.substring(0, 1);

		// Group 2: Accidental...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			switch(group2.charAt(0)) {
				case '-', 'b', 'B' -> tonic += "b";
				case '+', '#' -> tonic += "#";
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 3: Mode...
		final String group3 = matcher.group(3);
		if(group3 != null) {
			//noinspection UnusedAssignment
			mode = group3;
		}

		result = KeySignature.get(tonic);

		return result;
	}
}
