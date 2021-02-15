package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

public class Clef extends Modifier {
	// todo Either covert to simple class or enum
	public enum Type {
		treble15a(0, "gClef15ma", 0, Notes.E6),
		treble8a(1, "gClef8va", 0, Notes.E5),
		treble(2, "gClef", 0, Notes.E4),
		treble8b(3, "gClef8vb", 0, Notes.E3),
		treble15b(4, "gClef15mb", 0, Notes.E2),

		bass15a(5, "fClef15ma", 0, Notes.G4),
		bass8a(6, "fClef8va", 0, Notes.G3),
		bass(7, "fClef", 0, Notes.G2),
		bass8b(8, "fClef8vb", 0, Notes.G1),
		bass15b(9, "fClef15mb", 0, Notes.G0),

		soprano(10, "cClef", 4, Notes.C4),
		mezzoSoprano(11, "cClef", 2, Notes.A3),
		alto(12, "cClef", 0, Notes.F3),
		tenor(13, "cClef", -2, Notes.D3),
		baritone(14, "cClef", -4, Notes.B3),

		tab(15, "6stringTabClef", 0, 0),
		percussion(16, "unpitchedPercussionClef1", 0, 0);

		public final int index;
		public final String smufl;
		public final int staffSlot;
		public final int lowSlot, highSlot;

		Type(
				final int id,
				final String smufl,
				final int staffSlot,
				final int lowSlot) {
			index = id;
			this.smufl = smufl;
			this.staffSlot = staffSlot;
			this.lowSlot = lowSlot;
			highSlot = lowSlot + 8;
		}
	}

	public static Clef treble = new Clef(Type.treble);
	public static Clef bass = new Clef(Type.bass);
	public final Type type;

	public Clef(final Type type) {
		this.type = type;
	}

	public Clef(final String name) {
		type = Clef.Type.valueOf(name);
	}

	int countLedgerLines(final int c6) {
		throw new ImplementMeException();
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
		final boolean result;

		if(this == object) {
			result = true;
		} else {
			if(object == null || getClass() != object.getClass()) {
				result = false;
			} else {
				final Clef rhs = (Clef) object;
				result = type.equals(rhs.type);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return type.name();
	}
}
