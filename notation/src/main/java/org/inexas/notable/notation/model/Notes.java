package org.inexas.notable.notation.model;

import java.util.regex.*;

@SuppressWarnings({"PointlessArithmeticExpression", "unused"})
public interface Notes {
	int BASE = 7;
	int MINIMUM = 5;    // A0
	int MAXIMUM = 56;   // C8
	Pattern notePattern = Pattern.compile("" +
			"([A-GRX])" +       // Tonic, including rest & ghost
			"([0-9]+.*\\*?)?" + // Duration
			"([b#n])?" +        // Accidental
			"([._!fg]+)?");     // Articulation
	Pattern noteGroupEndPattern = Pattern.compile("" +
			"]" +               // Closing ]
			"([._!fg]+)?" +     // Articulation
			"([0-9]+.*\\*?)?"); // Duration

	static int tonic(final int slot) {
		return slot % BASE;
	}

	/**
	 * Covert an octave and tonic to a slot
	 *
	 * @param octave Octave value 0 to 8
	 * @param tonic  Tonic value  0..6
	 * @return Note slot from A0=5, through C4=28, to C8=56
	 */
	static int slot(final int octave, final int tonic) {
		final int result;

		assert tonic >= 0 && tonic < Notes.BASE : "Tonic out of range: " + tonic;
		assert octave >= 0 & octave < 9 : "Octave out of range: " + octave;

		result = octave * BASE + tonic;
		assert result >= MINIMUM && result <= MAXIMUM : "Note slot out of range: " + result;

		return result;
	}

	/**
	 * Return the octave given the note slot
	 *
	 * @param slot Note slot, e.g. 28
	 * @return The octave, e.g. 4
	 */
	static int octave(final int slot) {
		assert slot >= MINIMUM && slot <= MAXIMUM : "Slot out of range: " + slot;
		return slot / BASE;
	}

	/**
	 * Given a slot, return the name.
	 *
	 * @param slot The slot to resolve, e.g. 48
	 * @return The corresponding name, e.g. C4
	 */
	static String name(final int slot) {
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
		return slot(octave, tonic(name));
	}

	static int tonic(final String tonicText) {
		int result = tonicText.charAt(0) - 'C';
		if(result < 0) {
			result += 7;
		}
		return result;
	}

	int C = 0;
	int D = 1;
	int E = 2;
	int F = 3;
	int G = 4;
	int A = 5;
	int B = 6;

	String[] tonicName = {"C", "D", "E", "F", "G", "A", "B"};

	static String toName(final int slot) {
		return tonicName[slot % BASE] + slot / BASE;
	}

	static boolean isValid(final int slot) {
		return slot >= MINIMUM && slot <= MAXIMUM;
	}

	int C0 = 0 * BASE + C;
	int D0 = 0 * BASE + D;
	int E0 = 0 * BASE + E;
	int F0 = 0 * BASE + F;
	int G0 = 0 * BASE + G;
	int A0 = 0 * BASE + A;
	int B0 = 0 * BASE + B;

	int C1 = 1 * BASE + C;
	int D1 = 1 * BASE + D;
	int E1 = 1 * BASE + E;
	int F1 = 1 * BASE + F;
	int G1 = 1 * BASE + G;
	int A1 = 1 * BASE + A;
	int B1 = 1 * BASE + B;

	int C2 = 2 * BASE + C;
	int D2 = 2 * BASE + D;
	int E2 = 2 * BASE + E;
	int F2 = 2 * BASE + F;
	int G2 = 2 * BASE + G;
	int A2 = 2 * BASE + A;
	int B2 = 2 * BASE + B;

	int C3 = 3 * BASE + C;
	int D3 = 3 * BASE + D;
	int E3 = 3 * BASE + E;
	int F3 = 3 * BASE + F;
	int G3 = 3 * BASE + G;
	int A3 = 3 * BASE + A;
	int B3 = 3 * BASE + B;

	int C4 = 4 * BASE + C;
	int D4 = 4 * BASE + D;
	int E4 = 4 * BASE + E;
	int F4 = 4 * BASE + F;
	int G4 = 4 * BASE + G;
	int A4 = 4 * BASE + A;
	int B4 = 4 * BASE + B;

	int C5 = 5 * BASE + C;
	int D5 = 5 * BASE + D;
	int E5 = 5 * BASE + E;
	int F5 = 5 * BASE + F;
	int G5 = 5 * BASE + G;
	int A5 = 5 * BASE + A;
	int B5 = 5 * BASE + B;

	int C6 = 6 * BASE + C;
	int D6 = 6 * BASE + D;
	int E6 = 6 * BASE + E;
	int F6 = 6 * BASE + F;
	int G6 = 6 * BASE + G;
	int A6 = 6 * BASE + A;
	int B6 = 6 * BASE + B;

	int C7 = 7 * BASE + C;
	int D7 = 7 * BASE + D;
	int E7 = 7 * BASE + E;
	int F7 = 7 * BASE + F;
	int G7 = 7 * BASE + G;
	int A7 = 7 * BASE + A;
	int B7 = 7 * BASE + B;

	int C8 = 8 * BASE + C;
}
