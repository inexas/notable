/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Top level representation of a musical piece that contains a list of partMaps
 */
public class Score extends Element implements Visited {
	public final LinkedHashMap<String, Part> partMap = new LinkedHashMap<>();
	public String title;
	public String composer;
	public String header;
	public PickupMeasure pickupMeasure;
	public KeySignature key = KeySignature.C;
	public Staff staff = new Staff(Staff.Type.treble);
	/**
	 * The default and time signature for the Score. This also controls the
	 * default Duration, so a time signature of 1/4 will set the starting
	 * Duration to quarters
	 */
	public TimeSignature timeSignature = TimeSignature.COMMON;
	public Tempo tempo = Tempo.DEFAULT;

	public Score() {
		// Set up implicit Parts and Phrases which will be replaced
		// if explicit parts/phrases are defined afterwards
		final Part implicit = getPart(Part.IMPLICIT);
		implicit.getPhrase(Phrase.IMPLICIT);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Part part : partMap.values()) {
			part.accept(visitor);
		}
		visitor.exit(this);
	}

	/**
	 * Get a Part with the given name. If the Part does not exist then it will
	 * be created and added ot the Score.
	 *
	 * @param name The requested name of the Part
	 * @return Non-null Part
	 */
	public Part getPart(final String name) {
		Part result = null;

		if(partMap.size() == 1) {
			// Was the only Part defined implicitly?
			final Part part = partMap.values().iterator().next();
			//noinspection StringEquality
			if(part.name == Part.IMPLICIT) {
				result = new Part(name, part);
				partMap.clear();
				partMap.put(name, result);
			}
		}

		if(result == null) {
			result = partMap.get(name);
			if(result == null) {
				result = new Part(name, this);
				partMap.put(name, result);
			}
		}

		return result;
	}

	public Part getFirstPart() {
		return partMap.values().iterator().next();
	}
}
