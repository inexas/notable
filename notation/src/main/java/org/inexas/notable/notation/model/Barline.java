/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

public enum Barline implements Visited {
	singleBar("|", false),
	doubleBar("||", false),
	beginRepeat("|:", false),
	endRepeat(":|", false),
	beginEndRepeat(":|:", false),
	eos("|||", true),
	eosRepeat(":|||", true),
	/**
	 * Used to support multimeasure rests. It means look at the
	 * last measure in the series' barline when
	 */
	next("", false);

	public final String miki;
	/**
	 * true means that this barline terminates the piece
	 */
	public final boolean terminates;

	Barline(final String miki, final boolean terminates) {
		this.miki = miki;
		this.terminates = terminates;
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
