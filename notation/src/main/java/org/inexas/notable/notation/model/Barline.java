/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Barline extends Element implements Annotation {
	private final static Map<String, Barline> lookup = new HashMap<>();
	// Note simple bars don't get printed at begging on line
	public final static Barline bar = new Barline(
			"Bar",
			"|", "|", "");
	public final static Barline beginRepeat = new Barline(
			"Begin repeat",
			"|:", "|", "|:");
	public final static Barline endRepeat = new Barline(
			"End repeat",
			":|", ":|", "|");
	@SuppressWarnings("unused")
	public final static Barline beginEndRepeat = new Barline(
			"Begin-end repeat",
			":|:", ":|", "|:");
	public final static Barline doubleBar = new Barline(
			"Double",
			"||", "||", "||");
	@SuppressWarnings("unused")
	public final static Barline thinThick = new Barline(
			"Double",
			"-|", "-|", "-|");
	@SuppressWarnings("unused")
	public final static Barline thickThin = new Barline(
			"Double",
			"|-", "|-", "|-");
	private final String name;
	public final String miki;
	public final String endOfLineMiki;
	public final String beginningOfLineMiki;

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

	public static Barline getBarline(final String notation) {
		return lookup.get(notation);
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
