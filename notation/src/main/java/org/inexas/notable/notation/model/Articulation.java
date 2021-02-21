/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * An Articulations modifies to a single note or a whole chord.
 * <p>
 * todo Not sure if they can be mix and matched
 */
public enum Articulation implements Annotation {
	fermata("The note should be prolonged beyond the normal duration", "f"),
	glissando("A glide from one pitch to another",
			"g"),
	legato("Indicates musical notes are to be played or sung smoothly and connected", ""),
	marcato("Indicates a short note, long chord, or medium passage to be played louder or more forcefully than surrounding music", "!"),
	marcatissimo("Very marcato", "!!"),
	staccato("Signifies a note of shortened duration or detached (not legato)", "."),
	staccatissimo("Very staccato", ".."),
	tenuto("Hold the note in question its full length (or longer, with slight rubato), or play the note slightly louder", "_");

	private final static Map<String, Articulation> mikiArticulation = new HashMap<>();
	public final String description;
	public final String miki;

	Articulation(final String description, final String miki) {
		this.description = description;
		this.miki = miki;
	}

	public static Articulation get(final String miki) {
		Articulation result = null;
		for(final Articulation articulation : Articulation.values()) {
			if(articulation.miki.equals(miki)) {
				result = articulation;
				break;
			}
		}
		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}

