/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

public enum Barline implements Visited {
	singleBar("|"),
	doubleBar("||"),
	beginRepeat("|:"),
	endRepeat(":|"),
	beginEndRepeat(":|:"),
	eos("|||"),
	eosRepeat(":|||");

	public final String miki;

	Barline(final String miki) {
		this.miki = miki;
	}

	public static Barline get(final String miki) {
		final Barline result;
		switch(miki) {
			case "|" -> result = singleBar;
			case "||" -> result = doubleBar;
			case "|:" -> result = beginRepeat;
			case ":|" -> result = endRepeat;
			case ":|:" -> result = beginEndRepeat;
			case "|||" -> result = eos;
			case ":|||" -> result = eosRepeat;
			default -> throw new ImplementMeException(miki);
		}

		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
