/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class Staff extends Element {

	public enum Type {
		alto(Note.C3, Note.G4),
		bass(Note.G2, Note.A3),
		grand(Note.G2, Note.F5),
		tenor(Note.D3, Note.E4),
		treble(Note.E4, Note.F5);
		/**
		 * Note position of the lowest line in the key of C
		 */
		int cLow;
		/**
		 * Note position of the highest line in the key of C
		 */
		int cHigh;

		Type(final int cLow, final int cHigh) {
			this.cLow = cLow;
			this.cHigh = cHigh;
		}
	}

	public static final Staff trebleC = new Staff(Type.treble, KeySignature.C);
	public final Type type;
	private final KeySignature key;

	/**
	 * Note position on bottom line adjusted for key
	 */
	public final int lowLinePosition;
	/**
	 * Note position on top line adjusted for key
	 */
	public final int highLinePosition;

	/**
	 * Lowest note encountered, -1 means no notes so far
	 */
	public int lowestNote = -1;
	/**
	 * Highest note encountered, -1 means no notes so far
	 */
	public int highestNote = -1;

	public Staff(final String name, final KeySignature key) throws IllegalArgumentException {
		this(Type.valueOf(name), key);
	}

	public Staff(final Type type, final KeySignature key) {
		this.type = type;
		this.key = key;
		lowLinePosition = type.cLow;
		highLinePosition = type.cHigh;
	}

	@Override
	public void accept(@SuppressWarnings("ClassEscapesDefinedScope") final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		return type.hashCode() ^ key.hashCode();
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
				final Staff staff = (Staff) object;
				returnValue = type.equals(staff.type) && key.equals(staff.key);
			}
		}

		return returnValue;
	}

	/**
	 * This is fed all the notes in a section so that we can calculate
	 * the lowest and highest notes
	 *
	 * @param position A note to account for
	 */
	public void accountFor(final int position) {
		assert Note.isValid(position);

		if(lowestNote == -1) {
			lowestNote = highestNote = position;
		} else {
			if(position < lowestNote) {
				lowestNote = position;
			} else if(position > highestNote) {
				highestNote = position;
			}
		}
	}
}
