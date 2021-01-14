/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

public class KeySignature extends Miki implements Annotation {
	final static KeySignature DEFAULT;
	private final static Map<String, KeySignature> lookup = new HashMap<>();

	static {
		DEFAULT = new KeySignature("C");
		new KeySignature("C#");
		new KeySignature("Db");
		new KeySignature("D");
		new KeySignature("Eb");
		new KeySignature("E");
		new KeySignature("F");
		new KeySignature("Gb");
		new KeySignature("G");
		new KeySignature("Ab");
		new KeySignature("A");
		new KeySignature("Bb");
		new KeySignature("B");
	}

	final String tonic;
	final Mode mode;

	static KeySignature getKeySignature(final String tonicMiki, final String modeMiki) {
		final KeySignature result = lookup.get(tonicMiki);
		if(result == null) {
			throw new RuntimeException("No such key: " + tonicMiki);
		}
		final Mode mode = modeMiki == null ? Mode.Ionian : Mode.getMode(modeMiki);
		return new KeySignature(result, mode);
	}

	private KeySignature(final String tonic) {
		this.tonic = tonic;
		this.mode = null;
		lookup.put(tonic, this);
	}

	private KeySignature(final KeySignature toCopy, final Mode mode) {
		tonic = toCopy.tonic;
		this.mode = mode;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
