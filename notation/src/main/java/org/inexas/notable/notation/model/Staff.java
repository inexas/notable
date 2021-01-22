/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Staff extends Element {
	private final static Map<String, Staff> lookup = new HashMap<>();
	public static Staff treble = new Staff("treble", Note.E4);
	public static Staff bass = new Staff("bass", Note.G2);
	public static Staff alto = new Staff("alto", Note.F3);
	public static Staff tenor = new Staff("tenor", Note.D3);
	public static Staff grand = new Staff("grand", Note.G2);

	static {
		lookup.put("treble", treble);
		lookup.put("bass", bass);
		lookup.put("alto", alto);
		lookup.put("tenor", tenor);
		lookup.put("grand", grand);
	}

	public final String name;
	final int baseNote;

	public Staff(final String name, final int baseNote) {
		this.name = name;
		this.baseNote = baseNote;
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
	public void accept(@SuppressWarnings("ClassEscapesDefinedScope") final Visitor visitor) {
		visitor.visit(this);
	}
}
