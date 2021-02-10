package org.inexas.notable.notation.render;

public class DClef extends DEvent {
	String name;

	DClef(final double originX, final double originY) {
		super(originX, originY);
	}

	DClef(final Glyph glyph) {
		this.glyph = glyph;
	}
}
