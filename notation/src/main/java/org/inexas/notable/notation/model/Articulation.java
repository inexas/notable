/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * An Articulations modifies to a single note or a whole chord.
 * <p>
 * todo Not sure if they can be mix and matched to, for time being I will assume
 * not.
 */
public class Articulation extends Miki implements Annotation {
	@SuppressWarnings("unused")
	final static Articulation fermata = new Articulation(
			"fermata",
			"The note should be prolonged beyond the normal duration",
			"f");
	@SuppressWarnings("unused")
	final static Articulation glissando = new Articulation(
			"glissando",
			"A glide from one pitch to another",
			"g");
	@SuppressWarnings("unused")
	final static Articulation legato = new Articulation(
			"legato",
			"Indicates musical notes are to be played or sung smoothly and connected",
			"");
	@SuppressWarnings("unused")
	final static Articulation marcato = new Articulation(
			"marcato",
			"Indicates a short note, long chord, or medium passage " +
					"to be played louder or more forcefully than surrounding music",
			"!");
	@SuppressWarnings("unused")
	final static Articulation marcatissimo = new Articulation(
			"marcatissimo",
			"Very marcato",
			"!!");
	@SuppressWarnings("unused")
	final static Articulation staccato = new Articulation(
			"staccato",
			"Signifies a note of shortened duration or detached (not legato)",
			".");
	@SuppressWarnings("unused")
	final static Articulation staccatissimo = new Articulation(
			"staccatissimo",
			"Very staccato",
			"..");
	@SuppressWarnings("unused")
	final static Articulation tenuto = new Articulation(
			"tenuto",
			"Hold the note in question its full length (or longer, " +
					"with slight rubato), or play the note slightly louder",
			"_");
	private final static Map<String, Articulation> lookupName = new HashMap<>();
	private final static Map<String, Articulation> lookupMiki = new HashMap<>();
	final String name;
	final String description;
	public final String miki;
	Articulation(final String name, final String description, final String miki) {
		this.name = name;
		this.description = description;
		this.miki = miki;
		lookupName.put(name, this);
		lookupMiki.put(miki, this);
	}

	@SuppressWarnings("unused")
	static Articulation getByName(final String name) {
		return lookupName.get(name);
	}

	public static Articulation getByMiki(final String miki) {
		return lookupMiki.get(miki);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
