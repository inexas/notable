package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

public enum Accidental implements Annotation {
	flat("b"),
	doubleFlat("bb"),
	sharp("#"),
	doubleSharp("##"),
	natural("n");

	public final String miki;

	Accidental(final String miki) {
		this.miki = miki;
	}

	public static Accidental get(final String miki) {
		return switch(miki) {
			case "b" -> flat;
			case "bb" -> doubleFlat;
			case "#" -> sharp;
			case "##" -> doubleSharp;
			case "n" -> natural;
			default -> {
				throw new RuntimeException("Coding error: " + miki);
			}
		};
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
