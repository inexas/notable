/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * An Articulations modifies to a single note or a whole chord.
 */
public enum Articulation implements Annotation {
	fermata("f", "The note should be prolonged beyond the normal duration"),
	glissando("g", "A glide from one pitch to another"),
	marcato("!", "Indicates a short note, long chord, or medium passage to be played louder or more forcefully than surrounding music"),
	marcatissimo("!!", "Very marcato"),
	staccato(".", "Signifies a note of shortened duration or detached (not legato)"),
	staccatissimo("..", "Very staccato"),
	tenuto("_", "Hold the note in question its full length (or longer, with slight rubato), or play the note slightly louder");

	public final String miki;
	public final String description;

	Articulation(final String miki, final String description) {
		this.miki = miki;
		this.description = description;
	}

	public static Articulation get(final String miki) {
		return switch(miki) {
			case "f" -> fermata;
			case "g" -> glissando;
			case "!" -> marcato;
			case "!!" -> marcatissimo;
			case "." -> staccato;
			case ".." -> staccatissimo;
			case "_" -> tenuto;
			default -> {
				throw new RuntimeException("Coding error: " + miki);
			}
		};
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}

