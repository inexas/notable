/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

import static org.inexas.notable.notation.model.KeySignature.State.*;

public class KeySignature extends Element implements Annotation {
	public enum State {natural, sharp, flat}

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

	static {
		map.put("C", C);

		map.put("G", S1);
		map.put("D", S2);
		map.put("A", S3);
		map.put("E", S4);
		map.put("B", S5);
		map.put("F#", S6);
		map.put("C#", S7);

		// Figure out how to handle A# and the like
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

	public final String name;
	public final int accidentalCount;
	private final State state;

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
}
