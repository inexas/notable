/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;
import java.util.regex.*;

public class NamedChord extends Event {
	private static final String string = "" +
			"\\[" +             // Start chord name
			"([A-G])" +         // 1: Tonic
			"([b#])?" +         // 2: Accidental
			"([Mm])?" +         // 3: Mode, major/minor
			"([-/+])?" +        // 4: (Half) diminished, augmented
			"(7)?" +            // 5: 7th
			"(add[0-9]+)?" +    // 6: add2
			"(sus[0-9]+)?" +    // 7: sus4
			"(i[0-9])?" +       // 8: Inversion, i1
			"]" +               // End chord name
			"([0-9]+,*\\*?)?" + // 9: Duration
			"([._!fg]+)?";      // 10: Articulation

	private final static Pattern pattern = Pattern.compile(string);
	public final char tonic;
	public final char mode;
	public final char dimAug;
	public final boolean seventh;
	public final int add;
	public final int sus;
	public final int inversion;
	private NamedChord(final NamedChord toCopy) {
		super(toCopy);
		tonic = toCopy.tonic;
		mode = toCopy.mode;
		dimAug = toCopy.dimAug;
		seventh = toCopy.seventh;
		add = toCopy.add;
		sus = toCopy.sus;
		inversion = toCopy.inversion;
	}

	private NamedChord(
			final String name,
			final Duration duration,
			final Map<Class<? extends Annotation>, Annotation> annotations,
			final char tonic,
			final char mode,
			final char dimAug,
			final boolean seventh,
			final int add,
			final int sus,
			final int inversion) {
		super(name, duration, annotations);
		this.tonic = tonic;
		this.mode = mode;
		this.dimAug = dimAug;
		this.seventh = seventh;
		this.add = add;
		this.sus = sus;
		this.inversion = inversion;
	}

	public static NamedChord parse(
			final String input,
			final Duration defaultDuration,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		final NamedChord result;

		final Matcher matcher = pattern.matcher(input);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch: " + input);
		}

		final StringBuilder sb = new StringBuilder();
		sb.append('[');

		// Group 1: Upper case tonic...
		String group = matcher.group(1);
		final char tonic = group.charAt(0);
		sb.append(tonic);

		// Group 2: Accidental...
		group = matcher.group(2);
		if(group != null) {
			switch(group.charAt(0)) {
				case 'b' -> sb.append('b');
				case '#' -> sb.append('#');
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 3: Mode, [Mm]...
		group = matcher.group(3);
		final char mode;
		if(group == null) {
			mode = 0;
		} else {
			mode = group.charAt(0);
			sb.append(mode);
		}

		// Group 4: Diminished, half diminished or augmented...
		group = matcher.group(4);
		final char dimAug;
		if(group == null) {
			dimAug = 0;
		} else {
			dimAug = group.charAt(0);
			sb.append(dimAug);
		}

		// Group 5: Seventh...
		group = matcher.group(5);
		final boolean seventh;
		if(group == null) {
			seventh = false;
		} else {
			seventh = true;
			sb.append('7');
		}

		// Group 6: add...
		final int add;
		group = matcher.group(6);
		if(group == null) {
			add = 0;
		} else {
			add = Integer.parseInt(group.substring(3));
			sb.append(group);
		}

		// Group 7: sus...
		final int sus;
		group = matcher.group(7);
		if(group == null) {
			sus = 0;
		} else {
			sus = Integer.parseInt(group.substring(3));
			sb.append(group);
		}

		// Group 8: Inversion...
		final int inversion;
		group = matcher.group(8);
		if(group == null) {
			inversion = 0;
		} else {
			inversion = Integer.parseInt(group.substring(1));
			sb.append(group);
		}

		sb.append(']');

		// Group 9: Duration...
		final Duration duration;
		group = matcher.group(9);
		if(group == null) {
			duration = defaultDuration;
		} else {
			duration = Duration.getByMiki(group);
		}

		// Group 10: Articulation...
		group = matcher.group(10);
		if(group != null) {
			final Articulation articulation = Articulation.getByMiki(group);
			annotations.put(Articulation.class, articulation);
		}

		result = new NamedChord(
				sb.toString(), duration, annotations,
				tonic, mode, dimAug, seventh, add, sus, inversion);

		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Event copy(final Duration duration) {
		final Event result = new NamedChord(this);
		result.duration = duration;
		return result;
	}
}
