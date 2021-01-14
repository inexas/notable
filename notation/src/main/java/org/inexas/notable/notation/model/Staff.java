/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Staff extends Miki {
	private final static Map<String, Staff> lookup = new HashMap<>();
	public static Staff treble = new Staff("treble");
	public static Staff alto = new Staff("alto");
	public static Staff tenor = new Staff("tenor");
	public static Staff bass = new Staff("bass");
	public static Staff grand = new Staff("grand");
	public final static Staff DEFAULT = grand;

	static {
		lookup.put("treble", treble);
		lookup.put("alto", alto);
		lookup.put("tenor", tenor);
		lookup.put("bass", bass);
		lookup.put("grand", grand);
	}

	public final String name;

	private Staff(final String name) {
		this.name = name;
	}

	/**
	 * @param name Something like 'bass'
	 */
	public static Staff getStaff(final String name) {
		final Staff result;

		result = lookup.get(name);
		if(result == null) {
			throw new RuntimeException("No such Staff: " + name);
		}

		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
