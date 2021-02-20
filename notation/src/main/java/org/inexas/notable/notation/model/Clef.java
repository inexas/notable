package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public enum Clef implements Visited {
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

	Clef(
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

	/**
	 * Return the number of leger lines need to display this given note
	 *
	 * @param slot The note slot we're interested in
	 * @return 0 = no leger lines needed, -ve number of leger lines below and +ve number
	 * of leger lines above
	 */
	int countLedgerLines(final int slot) {
		final int result;
		if(slot > highSlot) {
			result = (slot - highSlot) / 2;
		} else if(slot < lowSlot) {
			result = (slot - lowSlot) / 2;
		} else {
			result = 0;
		}
		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
