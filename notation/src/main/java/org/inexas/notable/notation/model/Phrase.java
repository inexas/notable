/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;


/**
 * A piano has two Phrases, one for each hand
 */
public class Phrase extends Miki {
	public final static String IMPLICIT = "#IMPLICIT#";
	public final Score score;
	public final List<Event> events = new ArrayList<>();
	public final String name;
	public final Part part;

	Phrase(final String name, final Part part) {
		this.name = name;
		this.part = part;
		score = part.score;
	}

	Phrase(final String name, final Phrase toCopy) {
		this.name = name;
		// todo This is a bit strange if there have already been events. It is likely
		// that it should be a new Phrase
		events.addAll(toCopy.events);
		part = toCopy.part;
		score = toCopy.score;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Event event : events) {
			event.accept(visitor);
		}
		visitor.exit(this);
	}
}
