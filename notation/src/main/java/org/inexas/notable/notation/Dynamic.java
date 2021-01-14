/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

class Dynamic extends Miki implements Annotation {
	private final static Map<String, Dynamic> lookup = new HashMap<>();

	@SuppressWarnings("unused")
	final static Dynamic fff = new Dynamic("fff");
	@SuppressWarnings("unused")
	final static Dynamic ff = new Dynamic("ff");
	@SuppressWarnings("unused")
	final static Dynamic f = new Dynamic("f");
	@SuppressWarnings("unused")
	final static Dynamic mf = new Dynamic("mf");
	@SuppressWarnings("unused")
	final static Dynamic mp = new Dynamic("mp");
	@SuppressWarnings("unused")
	final static Dynamic p = new Dynamic("p");
	@SuppressWarnings("unused")
	final static Dynamic pp = new Dynamic("pp");
	@SuppressWarnings("unused")
	final static Dynamic ppp = new Dynamic("ppp");

	/**
	 * @param name Something like fff
	 */
	static Dynamic getDynamic(final String name) {
		final Dynamic result;

		assert name.length() > 0 : "Zero length key for Dynamic";

		result = lookup.get(name);
		if(result == null) {
			throw new RuntimeException("No such Dynamic: " + name);
		}

		return result;
	}

	final String name;

	private Dynamic(final String name) {
		this.name = name;
		lookup.put(name, this);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
