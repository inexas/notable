package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public class Beam extends Element implements Annotation {
	private static final Beam beam1 = new Beam(1);
	private static final Beam beam2 = new Beam(2);
	private static final Beam beam3 = new Beam(3);
	private static final Beam beam4 = new Beam(4);
	public static Beam[] beams = {null, beam1, beam2, beam3, beam4};

	public final int count;

	private Beam(final int count) {
		this.count = count;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
