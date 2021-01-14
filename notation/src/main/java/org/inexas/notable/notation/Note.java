/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import java.util.*;

/**
 * A representation of a sound event of a given duration and pitch
 */
public class Note extends Event {
	@SuppressWarnings("unused")
	public final static int OCTAVE_UNDEFINED = 0;
	@SuppressWarnings("unused")
	private final static int count = 12;
	private final static Map<String, Integer> lookup = new HashMap<>();
	private final static String[] names = {
			"C",    // 0
			"C#",   // 1
			"D",    // 2
			"D#",   // 3
			"E",    // 4
			"F",    // 5
			"F#",   // 6
			"G",    // 7
			"G#",   // 8
			"A",    // 9
			"A#",   // 10
			"B",    // 11
	};

	static {
		register(0, "C", "CN");
		register(1, "C#", "DB");
		register(2, "D", "DN");
		register(3, "D#", "EB");
		register(4, "E", "EN");
		register(5, "F");
		register(6, "F#", "GB");
		register(7, "G", "GN");
		register(8, "G#", "AB");
		register(9, "A", "AN");
		register(10, "A#", "BB");
		register(11, "B", "BN");
	}

	public final int index;

	private static void register(final int _index, final String... names) {
		final Integer index = _index;
		for(final String name : names) {
			lookup.put(name, index);
		}
	}

	@SuppressWarnings("unused")
	public static int getIndex(final String noteName) {
		return lookup.get(noteName.toUpperCase());
	}

	@SuppressWarnings("unused")
	public static String getName(final int index) {
		return names[index];
	}
	public final int octave;

	public Note(
			final String name,
			final int octave,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		super(name, duration, annotations);

		assert Character.isUpperCase(name.charAt(0));

		this.index = lookup.get(name.toUpperCase());
		this.octave = octave;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	private Note(final Note toCopy) {
		super(toCopy);
		this.index = toCopy.index;
		this.octave = toCopy.octave;
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new Note(this);
		result.duration = duration;
		return result;
	}
}
