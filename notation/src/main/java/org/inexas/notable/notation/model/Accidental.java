package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class Accidental extends Annotation {
	public final static Accidental flat = new Accidental("b");
	public final static Accidental sharp = new Accidental("#");
	public final static Accidental natural = new Accidental("n");
	public final String miki;

	public Accidental(final String miki) {
		this.miki = miki;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return miki;
	}
}
