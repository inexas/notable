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
	public final MappedList<Phrase> phrases = new MappedList<>();

	Part(final String name, final Score score) {
		this.name = name;
		this.score = score;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean isActive() {
		final boolean result;

		if(name.length() == 0) {
			result = phrases.size() > 1 || phrases.getFirst().isActive();
		} else {
			result = true;
		}

		return result;
	}

	/**
	 * Get a Phrase with the given name. If the Phrase does not exist then it will
	 * be created and added ot the parent Part.
	 *
	 * @param name The requested name of the Phrase
	 * @return Non-null Phrase
	 */
	public Phrase getOrCreatePhrase(final String name) {
		Phrase result;
		result = phrases.get(name);
		if(result == null) {
			result = new Phrase(name, this);
			phrases.add(result);
		}
		return result;
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

	public Phrase getFirstPhrase() {
		return phrases.getFirst();
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Part{");
		sb.append(name);
		sb.append('}');
		return sb.toString();
	}
}


