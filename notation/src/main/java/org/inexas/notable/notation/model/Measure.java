package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Measure extends Element implements Venue {
	private final Messages messages;
	private final Score score;
	/**
	 * Linked list: previous in chain
	 */
	private final Measure pic;
	/**
	 * The first Measure is 0, next 1, ...
	 */
	public final int ordinal;
	/**
	 * For most Measures the clef will be null os if it's not
	 * null then there is a change of clef from the default
	 * or from a previous definition.
	 */
	private Clef clef;
	/**
	 * The clicksSoFar counts up the clicks used each time an event is
	 * added to the measure.
	 */
	int clicksSoFar;
	/**
	 * The key signature for this measure. Will probably be null unless there's
	 * a command to set it. It is effective for this and all subsequent
	 * measures until another is encountered.
	 */
	private KeySignature keySignature;
	/**
	 * The list of events in this measure
	 */
	public final List<Event> events = new ArrayList<>();

	Measure(final Phrase phrase, final Measure pic) {
		score = phrase.part.score;
		messages = score.messages;
		this.pic = pic;
		if(pic == null) {
			// This is the first measure for the parent Phrase
			ordinal = 0;
		} else {
			// Non-first
			ordinal = pic.ordinal + 1;
		}
		// Force Score to register the new Measure
		score.getMeasureSize(0);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void add(final Event event) {
		events.add(event);
		clicksSoFar += event.duration.clicks;
	}

	public int getSize() {
		return score.getMeasureSize(ordinal);
	}

	public void setClef(final Clef clef) {
		if(this.clef != null) {
			error("Clef already set for this measure");
		} else {
			this.clef = clef;
		}
	}

	/**
	 * @return The clef for this measure or null if one has not been set
	 * @see #getEffectiveClef()
	 */
	private Clef getClef() {
		return clef;
	}

	/**
	 * @return The non-null clef that is effective for this measure
	 * @see #getClef()
	 */
	private Clef getEffectiveClef() {
		return pic == null ? score.getDefaultClef() : pic.getEffectiveClef();
	}

	void setKeySignature(final KeySignature keySignature) {
		if(this.keySignature != null) {
			error("Key signature already set for this measure");
		} else if(clicksSoFar > 0) {
			warn("It is good practice to set the key signature at the start of a measure");
		} else if(getEffectiveKeySignature().equals(keySignature)) {
			warn("Key signature is already " + keySignature);
		} else {
			this.keySignature = keySignature;
		}
	}

	/**
	 * @return The key signature set for this measure or null if none has been set
	 * @see #getEffectiveKeySignature()
	 */
	public KeySignature getKeySignature() {
		return keySignature;
	}

	/**
	 * @return The effective key signature for this measure
	 */
	private KeySignature getEffectiveKeySignature() {
		final KeySignature result;
		if(keySignature == null) {
			result = pic == null ? score.getDefaultKeySignature() : pic.getKeySignature();
		} else {
			result = keySignature;
		}
		return result;
	}

	/**
	 * @return The time signature for this measure or null if no time signature
	 * has been explicitly set
	 */
	public TimeSignature getTimeSignature() {
		return score.getTimeSignature(ordinal);
	}

	void handle(final TimeSignature timeSignature) {
		if(clicksSoFar > 0) {
			error("Time signatures must appear at the beginning of a measure");
		}
		score.report(ordinal, timeSignature);
	}

	void handle(final Cpm cpm) {
		final int clicks = cpm.clicks;
		if(clicksSoFar > 0) {
			error("CPM must appear at start of measure");
		} else if(clicks == getSize()) {
			warn("CPM does not change measure size?");
		} else {
			score.report(ordinal, cpm);
		}
	}

	//	private void padToEnd() {
//		if(clicksSoFar > 0) {
//			final int padSize = size - clicksSoFar;
//			final Duration[] durations = Duration.getByClicks(padSize);
//			for(final Duration duration : durations) {
//				events.add(new Rest(duration, MikiParser.mtAnnotationMap));
//			}
//		}
//	}
//
	public boolean isComplete() {
		assert clicksSoFar <= getSize();
		return clicksSoFar == getSize();
	}

	private void warn(final String message) {
		messages.warn(message);
	}

	private void error(final String message) {
		messages.error(message);
	}

	void handle(final Clef clef) {
		if(clicksSoFar > 0) {
			error("Cleft should be set at beginning of a measure");
		} else if(this.clef != null) {
			error("Clef has already been set for this measure");
		} else {
			this.clef = clef;
		}
	}

	TimeSignature getEffectiveTimeSignature() {
		return score.getEffectiveTimeSignature(ordinal);
	}
}
