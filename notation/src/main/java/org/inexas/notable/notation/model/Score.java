/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

import java.util.*;

/**
 * Top level representation of a musical piece that contains a list of partMaps
 */
public class Score extends Element implements Visited {
	final Messages messages;
	public final MappedList<Part> parts = new MappedList<>();
	public String title;
	private String subtitle;
	public String composer;
	public String header;
	// Score defaults...
	private Clef defaultClef;
	private KeySignature defaultKeySignature;
	private TimeSignature defaultTimeSignature;

	public Score(final Messages messages) {
		this.messages = messages;
		getOrCreatePart("");
	}

	/**
	 * Get a Part with the given name. If the Part does not exist then it will
	 * be created and added ot the Score.
	 *
	 * @param name The requested name of the Part
	 * @return Non-null Part
	 */
	public Part getOrCreatePart(final String name) {
		Part result = parts.get(name);
		if(result == null) {
			result = new Part(name, this);
			parts.add(result);
		}
		return result;
	}

	/**
	 * The time line collects the sizes of measures for the duration of the
	 * score. Measure sizes are synchronized across all Phases so, for example
	 * of the piano LH starts with a pickup then all parts: piano RH, voice,
	 * double bass, must also start with a pickup measure of the same length
	 * albeit rest-filled.
	 * <p>
	 * The size for each measure will be greater than 1 so the initialized
	 * value of zero means that the size has not been set.
	 * <p>
	 * Typically it will be the first phrase that sets the values but other
	 * phrases may do so. Values should also be set successively so jumping
	 * indicates that something is wrong.
	 */
	static class Time {
		TimeSignature timeSignature;
		int cpm;
		int actual;
	}

	// T I M E L I N E . . .

	private final List<Time> timeline = new ArrayList<>();

	public int[] getTimeLine() {
		final int size = timeline.size();
		final int[] result = new int[size];
		for(int i = 0; i < size; i++) {
			final Time time = timeline.get(i);
			result[i] = time.actual;
		}
		return result;
	}

	int report(final int ordinal, final Cpm cpm) {
		// We can only change the latest measure...
		final int last = timeline.size() - 1;
		final Time time = timeline.get(ordinal);
		if(ordinal == last) {
			if(time.cpm == 0) {
				time.actual = time.cpm = cpm.clicks;
			} else {
				error("Attempt to redefine CPM");
			}
		} else {
			error("Can't change size of measure once it has been fixed elsewhere");
		}
		return time.actual;
	}

	int report(final int ordinal, final TimeSignature timeSignature) {
		// We can only change the latest measure...
		final int last = timeline.size() - 1;
		final Time time = timeline.get(ordinal);
		if(ordinal == last) {
			if(time.timeSignature == null) {
				time.timeSignature = timeSignature;
				if(time.cpm == 0) {
					time.actual = timeSignature.getMeasureSize();
				}
			} else {
				error("Attempt to redefine time signature");
			}
		} else {
			error("Can't change size of measure once it has been fixed elsewhere");
		}
		return time.actual;
	}

	/**
	 * Get the size of the measure at the given ordinal. If the ordinal is
	 * beyond the end of the time line a new record is created.
	 *
	 * @param ordinal The ith measure that we're interested in
	 * @return The size of the measure at the given ordinal
	 */
	int getMeasureSize(final int ordinal) {
		final int result;

		assert ordinal >= 0 && ordinal <= timeline.size() : "Ordinal: " + ordinal;

		final int count = timeline.size();
		if(ordinal == count) {
			// New measure...
			int clicks = 0;
			for(int i = count - 1; i >= 0; i--) {
				final Time time = timeline.get(i);
				if(time.timeSignature != null) {
					clicks = time.timeSignature.getMeasureSize();
					break;
				}
			}
			if(clicks == 0) {
				// Time signature has not been explicitly set...
				clicks = defaultTimeSignature == null ?
						TimeSignature.fourFour.getMeasureSize()
						: defaultTimeSignature.getMeasureSize();
			}
			final Time time = new Time();
			result = time.actual = clicks;
			timeline.add(time);
		} else {
			// Existing measure
			result = timeline.get(ordinal).actual;
		}
		return result;
	}

	public TimeSignature getTimeSignature(final int ordinal) {
		assert ordinal >= 0 && timeline.size() > ordinal;
		return timeline.get(ordinal).timeSignature;
	}

	TimeSignature getEffectiveTimeSignature(final int ordinal) {
		TimeSignature result = null;

		for(int i = ordinal; i >= 0; i--) {
			final Time time = timeline.get(i);
			if(time.timeSignature != null) {
				result = time.timeSignature;
				break;
			}
		}
		if(result == null) {
			result = getDefaultTimeSignature();
		}

		return result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		if(this.title != null) {
			warn("Score title already set, ignoring redefinition");
		} else {
			this.title = StringU.stripQuotesTrim(title);
		}
	}

	public void setSubtitle(final String subtitle) {
		if(this.subtitle != null) {
			warn("Score subtitle already set, ignoring redefinition");
		} else {
			this.subtitle = StringU.stripQuotesTrim(subtitle);
		}
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(final String composer) {
		if(this.composer != null) {
			warn("Composer already set, ignoring redefinition");
		} else {
			this.composer = StringU.stripQuotesTrim(composer);
		}
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(final String header) {
		if(this.header != null) {
			warn("Header already set, ignoring redefinition");
		} else {
			this.header = StringU.stripQuotesTrim(header);
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

	KeySignature getDefaultKeySignature() {
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

	public void setDefaultTimeSignature(final TimeSignature timeSignature) {
		if(defaultTimeSignature != null) {
			error("Default time signature already set for this score");
		} else {
			defaultTimeSignature = timeSignature;
		}
	}

	private TimeSignature getDefaultTimeSignature() {
		return defaultTimeSignature == null ? TimeSignature.fourFour : defaultTimeSignature;
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
		for(int i = 0; i < parts.size(); i++) {
			parts.get(i).accept(visitor);
		}
		visitor.exit(this);
	}
}
