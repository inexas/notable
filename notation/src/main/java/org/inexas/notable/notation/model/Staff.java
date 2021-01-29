/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Staff extends Element {
	public enum Type {
		alto(Note.F3, Note.G4,
				new int[]{Note.F4, Note.C4, Note.G4, Note.D4, Note.A3, Note.E4, Note.B3},
				new int[]{Note.B3, Note.E4, Note.A3, Note.D4, Note.G3, Note.C4, Note.F3}),
		bass(Note.G2, Note.A3,
				new int[]{Note.F3, Note.C3, Note.G3, Note.D3, Note.A2, Note.E3, Note.B2},
				new int[]{Note.B2, Note.E3, Note.A2, Note.D3, Note.G2, Note.C3, Note.F2}),
		tenor(Note.D3, Note.E4,
				new int[]{Note.F3, Note.C4, Note.G3, Note.D4, Note.A3, Note.E4, Note.B3},
				new int[]{Note.B3, Note.E4, Note.A3, Note.D4, Note.G3, Note.C4, Note.F3}),
		treble(Note.E4, Note.F5,
				new int[]{Note.F5, Note.C5, Note.G5, Note.D5, Note.A4, Note.E5, Note.B4},
				new int[]{Note.B4, Note.E5, Note.A4, Note.D5, Note.G4, Note.C5, Note.F4});
		/**
		 * Slot number of the lowest line, A0 is 0
		 */
		int lowSlot;
		/**
		 * Slot number of the highest line, A0 is 0
		 */
		int highSlot;
		int[] sharps;
		int[] flats;

		Type(final int lowSlot, final int highSlot, final int[] sharps, final int[] flats) {
			this.lowSlot = lowSlot;
			this.highSlot = highSlot;
			this.sharps = sharps;
			this.flats = flats;
		}
	}

	public final Type type;

	/**
	 * Note slot on bottom line. For a treble staff this would be E4
	 */
	public final int lowSlot;
	/**
	 * Note slot on top line. For a treble staff this would be F5
	 */
	final int highSlot;

	/**
	 * Lowest note encountered, -1 means no notes so far
	 */
	public int lowestNote = -1;
	/**
	 * Highest note encountered, -1 means no notes so far
	 */
	int highestNote = -1;

	public Staff(final String name) throws IllegalArgumentException {
		this(Type.valueOf(name));
	}

	public Staff(final Type type) {
		this.type = type;
		lowSlot = type.lowSlot;
		highSlot = type.highSlot;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		final boolean returnValue;

		if(this == object) {
			returnValue = true;
		} else {
			if(object == null || getClass() != object.getClass()) {
				returnValue = false;
			} else {
				final Staff rhs = (Staff) object;
				returnValue = type.equals(rhs.type)
						&& lowestNote == rhs.lowestNote
						&& highestNote == rhs.highestNote;
			}
		}

		return returnValue;
	}

	/**
	 * This is fed all the notes in a section so that we can calculate
	 * the lowest and highest notes
	 *
	 * @param slot A note to account for
	 */
	public void accountFor(final int slot) {
		assert Note.isValid(slot);

		if(lowestNote == -1) {
			lowestNote = highestNote = slot;
		} else {
			if(slot < lowestNote) {
				lowestNote = slot;
			} else if(slot > highestNote) {
				highestNote = slot;
			}
		}
	}

	/**
	 * Given a note count the number of leger lines that will be needed
	 * to render it.
	 * <p>
	 * For a chord, call the method for each note. It's no big deal if
	 * the same leger line is rendered more than once.
	 *
	 * @param note The slot of the note to test
	 * @return The count of leger lines. Either 0 - no leger lines needed,
	 * a slot number then a count of lines above the staff or a negative
	 * number - a count of lines below the staff
	 */
	public int countLedgerLines(final int note) {
		final int returnValue;

		if(lowestNote == -1) {  // No notes accounted for yet
			returnValue = 0;
		} else {
			if(note > highSlot) {
				returnValue = (note - highSlot) / 2;
			} else if(note < lowSlot) {
				returnValue = -(lowSlot - note) / 2;
			} else {
				returnValue = 0;
			}
		}

		return returnValue;
	}

	/**
	 * @return Space needed to accommodate notes above the staff in slots, ie.
	 * A5 needs 2 slots
	 */
	public int slotsAbove() {
		return highestNote == -1 ? 0 : Math.max(highestNote - highSlot, 0);
	}

	/**
	 * @return Space needed to accommodate notes below the staff in slots, ie.
	 * C4 needs 2 slots
	 */
	public int slotsBelow() {
		return lowestNote == -1 ? 0 : Math.max(lowSlot - lowestNote, 0);
	}

	/**
	 * @param key The key
	 * @return The list of slots that should be marked with an accidental
	 * * for a give key
	 */
	public int[] getAccidentals(final KeySignature key) {
		return Arrays.copyOfRange(
				key.isFlat() ? type.flats : type.sharps,
				0,
				key.accidentalCount);
	}
}
