/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

class Barline extends Miki implements Annotation {
	final static Map<String, Barline> lookup = new HashMap<>();
	// Note simple bars don't get printed at begging on line
	final static Barline bar = new Barline(
			"Bar",
			"|", "|", "");
	final static Barline beginRepeat = new Barline(
			"Begin repeat",
			"|:", "|", "|:");
	final static Barline endRepeat = new Barline(
			"End repeat",
			":|", ":|", "|");
	@SuppressWarnings("unused")
	final static Barline beginEndRepeat = new Barline(
			"Begin-end repeat",
			":|:", ":|", "|:");
	final static Barline doubleBar = new Barline(
			"Double",
			"||", "||", "||");
	@SuppressWarnings("unused")
	final static Barline thinThick = new Barline(
			"Double",
			"-|", "-|", "-|");
	@SuppressWarnings("unused")
	final static Barline thickThin = new Barline(
			"Double",
			"|-", "|-", "|-");
	final String name;
	final String miki;
	final String endOfLineMiki;
	final String beginningOfLineMiki;

	static Barline getBarline(final String notation) {
		return lookup.get(notation);
	}

	private Barline(
			final String name,
			final String miki,
			final String endOfLineMiki,
			final String beginningOfLineMiki) {
		this.name = name;
		this.miki = miki;
		this.endOfLineMiki = endOfLineMiki;
		this.beginningOfLineMiki = beginningOfLineMiki;
		lookup.put(miki, this);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
