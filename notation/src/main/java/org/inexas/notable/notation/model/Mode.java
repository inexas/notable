/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Mode implements Annotation {
	final static Mode Ionian = new Mode("Ionian", "M", "I", "i");
	private final static Map<String, Mode> lookup = new HashMap<>();
	@SuppressWarnings("unused")
	private final static Mode Dorian = new Mode("Dorian", "D", "d");
	@SuppressWarnings("unused")
	private final static Mode Phrygian = new Mode("Phrygian", "P", "p");
	@SuppressWarnings("unused")
	private final static Mode Lydian = new Mode("Lydian", "Y", "y");
	@SuppressWarnings("unused")
	private final static Mode Mixolydian = new Mode("Mixolydian", "X", "X");
	@SuppressWarnings("unused")
	private final static Mode Aeolian = new Mode("Aeolian", "m", "A", "a");
	@SuppressWarnings("unused")
	private final static Mode Locrian = new Mode("Locrian", "L", "l");

	private final String name;
	public final String miki;

	private Mode(final String name, final String... mikis) {
		this.name = name;
		lookup.put(name, this);
		for(final String miki : mikis) {
			lookup.put(miki, this);
		}
		miki = mikis[0];
	}

	@SuppressWarnings("unused")
	static Mode getMode(final String miki) {
		return lookup.get(miki);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
