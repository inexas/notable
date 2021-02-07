package org.inexas.notable.notation.model;

public enum Clef {
	alto(0),
	bass(1),
	tenor(2),
	treble(3);

	/**
	 * This used to look up the accidentals
	 *
	 * @see KeySignature
	 */
	public final int index;

	private Clef(final int index) {
		this.index = index;
	}
}
