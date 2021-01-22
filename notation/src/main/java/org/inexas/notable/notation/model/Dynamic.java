/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Dynamic extends Element implements Annotation {
	private final static Map<String, Dynamic> lookup = new HashMap<>();

	@SuppressWarnings("unused")
	public final static Dynamic fff = new Dynamic("fff");
	@SuppressWarnings("unused")
	public final static Dynamic ff = new Dynamic("ff");
	@SuppressWarnings("unused")
	public final static Dynamic f = new Dynamic("f");
	@SuppressWarnings("unused")
	public final static Dynamic mf = new Dynamic("mf");
	@SuppressWarnings("unused")
	public final static Dynamic mp = new Dynamic("mp");
	@SuppressWarnings("unused")
	public final static Dynamic p = new Dynamic("p");
	@SuppressWarnings("unused")
	public final static Dynamic pp = new Dynamic("pp");
	@SuppressWarnings("unused")
	public final static Dynamic ppp = new Dynamic("ppp");
	public final String name;

	private Dynamic(final String name) {
		this.name = name;
		lookup.put(name, this);
	}

	/**
	 * @param name Something like fff
	 */
	public static Dynamic getDynamic(final String name) {
		final Dynamic result;

		assert name.length() > 0 : "Zero length key for Dynamic";

		result = lookup.get(name);
		if(result == null) {
			throw new RuntimeException("No such Dynamic: " + name);
		}

		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
