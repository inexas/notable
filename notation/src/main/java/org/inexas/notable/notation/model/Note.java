/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * A representation of a sound event of a given duration and pitch. A
 * note can be represented either as an octave + tonic e.g. C4 or
 * as a note slot: the octave x 12 + tonic where the tonic is a slot
 * between 0 and 11 for C..B
 */
public class Note extends Event {

	public static class SearchSpace {
		private static final int[] lookup = {0, 1, 2, 3, -3, -2, -1};
		private int anchor, anchorTonic;

		public SearchSpace(final int slot, final int relativeOctave) {
			setAnchor(slot);

			if(relativeOctave != 0) {
				final boolean up = relativeOctave > 0;
				int distance = up ? relativeOctave : -relativeOctave;
				final int newAnchor = anchor + (up ? 4 : -5);
				distance--;
				setAnchor(newAnchor + (up ? distance * Notes.BASE : distance * -Notes.BASE));
			}
		}

		private void setAnchor(final int slot) {
			if(slot < 8) {
				anchor = 8;
			} else if(slot > 53) {
				anchor = 53;
			} else {
				anchor = slot;
			}
			anchorTonic = Notes.tonic(anchor);
		}

		public int lookup(final int tonic) {
			return anchor + lookup[(Notes.BASE + tonic - anchorTonic) % Notes.BASE];
		}
	}

	private static final String[] labels = {"C", "D", "E", "F", "G", "A", "B"};

	/**
	 * The octave slot of this note valid both for slot and slot
	 */
	public final int octave;

	/**
	 * The slot representing the tonic independently to the
	 * octave, e.g. C = 0, C#, Db = 1, ...
	 */
	public final int tonic;

	public boolean isGhost;

	public Note(
			final int slot,
			final Duration duration,
			final boolean isGhost,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(Notes.toName(slot), slot, duration, annotations);

		this.isGhost = isGhost;

		octave = slot / Notes.BASE;
		tonic = slot % Notes.BASE;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getLabel() {
		return labels[tonic];
	}
}
