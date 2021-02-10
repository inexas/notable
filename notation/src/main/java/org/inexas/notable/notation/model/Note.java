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

	static class SearchSpace {
		private static final int[] lookup = {0, 1, 2, 3, -3, -2, -1};
		private int anchor, anchorTonic;

		SearchSpace(final int slot) {
			setAnchor(slot);
		}

		void setAnchor(final int slot) {
			if(slot < 8) {
				anchor = 8;
			} else if(slot > 53) {
				anchor = 53;
			} else {
				anchor = slot;
			}
			anchorTonic = Notes.tonic(anchor);
		}

		int lookup(final int tonic) {
			return anchor + lookup[(Notes.BASE + tonic - anchorTonic) % Notes.BASE];
		}

		/**
		 * Move the SearchSpace up or down by a given amount
		 *
		 * @param vector E.g. '++' gives a vector of +2
		 */
		void moveAnchor(final int vector) {
			assert vector != 0;

			final boolean up = vector > 0;
			int distance = up ? vector : -vector;
			final int newAnchor = anchor + (up ? 4 : -5);
			distance--;
			setAnchor(newAnchor + (up ? distance * Notes.BASE : distance * -Notes.BASE));
		}

		int getAnchor() {
			return anchor;
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

	/**
	 * Calculator that given the last note slot and a new tonic, calculates
	 * the next slot.
	 *
	 * @param lastSlot       The previous note slot
	 * @param absoluteOctave Octave override using 'o4'. -1 means no override
	 * @param relativeOctave Octave override 0 = none requested otherwise the
	 *                       slot will be the count of +'s or -'s in the miki
	 * @param tonicText      The tonic to move to, e.g. "C"
	 * @return The note slot of the next note
	 */
	public static int next(
			final int lastSlot,
			final int absoluteOctave,
			final int relativeOctave,
			final String tonicText) {
		final int result;

		final int tonic = Notes.tonic(tonicText);

		if(absoluteOctave >= 0) {
			// Absolute octave has been specified
			assert relativeOctave == 0;
			assert absoluteOctave < 8 || absoluteOctave == 8 && tonic == 0;
			result = absoluteOctave * Notes.BASE + tonic;
		} else {
			final SearchSpace searchSpace = new SearchSpace(lastSlot);
			if(relativeOctave != 0) {
				searchSpace.moveAnchor(relativeOctave);
			}
			result = searchSpace.lookup(tonic);
		}

		return result;
	}

	@Override
	public String getLabel() {
		return labels[tonic];
	}
}
