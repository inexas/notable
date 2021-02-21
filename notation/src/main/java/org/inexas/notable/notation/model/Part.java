/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

/**
 * The part played by one instrument
 */
public class Part extends Element implements Visited, MappedList.Named {
	public final String name;
	public final Score score;
	// Preserve the oder
	public MappedList<Phrase> phrases = new MappedList<>();

	Part(final String name, final Score score) {
		this.name = name;
		this.score = score;
	}

	@Override
	public String getName() {
		return name;
	}

	public Phrase newPhrase(final String name) {
		final Phrase result = new Phrase(name, this);
		phrases.add(result);
		return result;
	}

	public Phrase getFirstPhrase() {
		return phrases.getFirst();
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		final int count = phrases.size();
		for(int i = 0; i < count; i++) {
			phrases.get(i).accept(visitor);
		}
		visitor.exit(this);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		final boolean result;

		if(this == object) {
			result = true;
		} else {
			if(object == null || getClass() != object.getClass()) {
				result = false;
			} else {
				final Part rhs = (Part) object;
				result = name.equals(rhs.name);
			}
		}

		return result;
	}

	boolean prune() {
		final MappedList<Phrase> pruned = new MappedList<>();
		for(final Phrase phrase : phrases) {
			if(phrase.prune()) {
				pruned.add(phrase);
			}
		}
		phrases = pruned;
		return phrases.size() > 0;
	}

	int countMeasures(final int currentMaximum) {
		int result = 0;
		for(final Phrase phrase : phrases) {
			result = phrase.countMeasures(result);
		}
		return result;
	}
}


