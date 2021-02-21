/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

/**
 * Top level representation of a musical piece that contains a list of partMaps
 */
public class Score extends Element implements Visited {
	final Messages messages;
	public final Timeline timeline;
	public MappedList<Part> parts = new MappedList<>();
	public String title;
	public String subtitle;
	public String composer;
	public String header;
	public String copyright;
	// Score defaults...
	public Clef defaultClef;
	private KeySignature defaultKeySignature;
	private TimeSignature defaultTimeSignature;

	public Score(final Messages messages) {
		this.messages = messages;
		timeline = new Timeline(this);
	}

	/**
	 * Get a Part with the given name. If the Part does not exist then it will
	 * be created and added ot the Score.
	 *
	 * @param name The requested name of the Part
	 * @return Non-null Part
	 */
	public Part newPart(final String name) {
		final Part result = new Part(name, this);
		parts.add(result);
		return result;
	}

	// T I M E L I N E . . .

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		if(this.title != null) {
			warn("Title already set, ignoring redefinition");
		} else {
			final String string = StringU.stripQuotesTrim(title);
			if(string.length() > 0) {
				this.title = string;
			}
		}
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final String subtitle) {
		if(this.subtitle != null) {
			warn("Subtitle already set, ignoring redefinition");
		} else {
			final String string = StringU.stripQuotesTrim(subtitle);
			if(string.length() > 0) {
				this.subtitle = string;
			}
		}
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(final String composer) {
		if(this.composer != null) {
			warn("Composer already set, ignoring redefinition");
		} else {
			final String string = StringU.stripQuotesTrim(composer);
			if(string.length() > 0) {
				this.composer = string;
			}
		}
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(final String header) {
		if(this.header != null) {
			warn("Header already set, ignoring redefinition");
		} else {
			final String string = StringU.stripQuotesTrim(header);
			if(string.length() > 0) {
				this.header = string;
			}
		}
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(final String copyright) {
		if(this.copyright != null) {
			warn("Copyright already set, ignoring redefinition");
		} else {
			final String string = StringU.stripQuotesTrim(copyright);
			if(string.length() > 0) {
				this.copyright = string;
			}
		}
	}

	Clef getDefaultClef() {
		return defaultClef == null ? Clef.treble : defaultClef;
	}

	/**
	 * Set the default clef for the score. The starting default
	 * is 'treble' but calling this overrides it.
	 *
	 * @param clef The default clef for the score
	 */
	public void setDefaultClef(final Clef clef) {
		if(defaultClef != null) {
			error("Default clef already set for the score");
		} else if(clef.equals(Clef.treble)) {
			warn("The default clef is already 'treble', you can omit 'clef treble'");
		} else {
			defaultClef = clef;
		}
	}

	public KeySignature getDefaultKeySignature() {
		return defaultKeySignature == null ? KeySignature.C : defaultKeySignature;
	}

	/**
	 * Set the default key signature for the score. The starting default
	 * is C but calling this overrides it.
	 *
	 * @param keySignature The default key signature for the score
	 */
	public void setDefaultKeySignature(final KeySignature keySignature) {
		if(defaultKeySignature != null) {
			error("Default key signature already set for this score");
		} else if(keySignature.equals(KeySignature.C)) {
			warn("The default key signature is already C, you can omit 'key C'");
		}
		defaultKeySignature = keySignature;
	}

	public TimeSignature getDefaultTimeSignature() {
		return defaultTimeSignature == null ? TimeSignature.fourFour : defaultTimeSignature;
	}

	/**
	 * Set the starting time signature for the score. The starting default
	 * is 4/4 but calling this overrides it.
	 *
	 * @param timeSignature The default time signature for the score
	 */
	public void setDefaultTimeSignature(final TimeSignature timeSignature) {
		if(defaultTimeSignature != null) {
			error("Default time signature already set for this score");
		} else if(timeSignature.equals(TimeSignature.fourFour)) {
			warn("The default time signature is already 4/4");
		}
		defaultTimeSignature = timeSignature;
	}

	private void warn(final String message) {
		messages.warn(message);
	}

	private void error(final String message) {
		messages.error(message);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		final int size = parts.size();
		for(int i = 0; i < size; i++) {
			parts.get(i).accept(visitor);
		}
		visitor.exit(this);
	}

	public void prune() {
		final MappedList<Part> pruned = new MappedList<>();
		for(final Part part : parts) {
			if(part.prune()) {
				pruned.add(part);
			}
		}
		parts = pruned;

		int measureCount = 0;
		for(final Part part : parts) {
			measureCount = part.countMeasures(measureCount);
		}

		timeline.setLength(measureCount);
	}
}
