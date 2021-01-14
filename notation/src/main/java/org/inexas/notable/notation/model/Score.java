/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * Top level representation of a musical piece that contains a list of partMaps
 */
public class Score extends Miki implements Visited {
	// Set up with default values
	public final static String defaultTitle = "Untitled";
	public final static String defaultComposer = "Unknown composer";
	public final List<String> messages = new ArrayList<>();
	final LinkedHashMap<String, Part> partMap = new LinkedHashMap<>();
	public String title = defaultTitle;
	public String composer = defaultComposer;
	public String header;
	public PickupMeasure pickupMeasure;
	public Staff staff = Staff.grand;
	public KeySignature keySignature = KeySignature.DEFAULT;
	/**
	 * The default and time signature for the Score. This also controls the
	 * default Duration, so a time signature of 1/4 will set the starting
	 * Duration to quarters
	 */
	public TimeSignature timeSignature = TimeSignature.DEFAULT;
	public Tempo tempo = Tempo.DEFAULT;
	public Score() {
		// Set up implicit Parts and Phrases which will be replaced
		// if explicit parts/phrases are defined afterwards
		final Part implicit = getPart(Part.IMPLICIT);
		implicit.getPhrase(Phrase.IMPLICIT);
	}

	public static Score fromString(final String string) {
		final CharStream cs = CharStreams.fromString(string);
		final MusicLexer lexer = new MusicLexer(cs);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final MusicParser parser = new MusicParser(tokens);
		final MusicParser.ScoreContext tree = parser.score();
		final ToScoreListener listener = new ToScoreListener(string);
		final ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);
		return listener.score;
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
