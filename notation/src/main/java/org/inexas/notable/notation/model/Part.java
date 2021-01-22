/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * The part played by one instrument
 */
public class Part extends Element implements Visited {
	public final static String IMPLICIT = "#IMPLICIT#";
	public final String name;
	public final Score score;
	public final LinkedHashMap<String, Phrase> phraseMap = new LinkedHashMap<>();
	public Staff staff;

	Part(final String name, final Score score) {
		this.name = name;
		this.score = score;
	}

	Part(final String name, final Part toCopy) {
		// todo This is a bit strange if there have already been events. It is likely
		// that it should be a new Part
		this.name = name;
		score = toCopy.score;
		phraseMap.putAll(toCopy.phraseMap);
		staff = toCopy.staff;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Phrase phrase : phraseMap.values()) {
			phrase.accept(visitor);
		}
		visitor.exit(this);
	}

	/**
	 * Get a Phrase with the given name. If the Phrase does not exist then it will
	 * be created and added ot the parent Part.
	 *
	 * @param name The requested name of the Phrase
	 * @return Non-null Phrase
	 */
	public Phrase getPhrase(final String name) {
		Phrase result = null;

		if(phraseMap.size() == 1) {
			// Was the only Phrase defined implicitly?
			final Phrase phrase = phraseMap.values().iterator().next();
			//noinspection StringEquality
			if(phrase.name == Phrase.IMPLICIT) {
				result = new Phrase(name, phrase);
				phraseMap.clear();
				phraseMap.put(name, result);
			}
		}

		if(result == null) {
			result = phraseMap.get(name);
			if(result == null) {
				result = new Phrase(name, this);
				phraseMap.put(name, result);
			}
		}

		return result;
	}


	public Phrase getFirstPhrase() {
		return phraseMap.values().iterator().next();
	}
}


