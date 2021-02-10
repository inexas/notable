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
	public final String name;
	public final Score score;
	public final Map<String, Phrase> phraseMap = new LinkedHashMap<>();

	Part(final String name, final Score score) {
		this.name = name;
		this.score = score;
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
		result = phraseMap.get(name);
		if(result == null) {
			result = new Phrase(name, this);
			phraseMap.put(name, result);
		}
		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Phrase phrase : phraseMap.values()) {
			phrase.accept(visitor);
		}
		visitor.exit(this);
	}

	public Phrase getFirstPhrase() {
		return phraseMap.values().iterator().next();
	}
}


