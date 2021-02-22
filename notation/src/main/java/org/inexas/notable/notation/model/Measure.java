package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Measure extends Element implements Venue {
	public final Timeline.Frame frame;
	public final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
	private final Messages messages;
	private final Score score;
	private final Phrase phrase;
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
		this.phrase = phrase;
		this.pic = pic;

		score = phrase.part.score;
		messages = score.messages;
		if(pic == null) {
			// This is the first measure for the parent Phrase
			ordinal = 0;
		} else {
			// Non-first
			ordinal = pic.ordinal + 1;
		}
		frame = score.timeline.report(this);
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
		return frame.actualSize;
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
	public Clef getClef() {
		return clef;
	}

	/**
	 * @return The non-null clef that is effective for this measure
	 * @see #getClef()
	 */
	public Clef getEffectiveClef() {
		final Clef result;
		if(clef != null) {
			result = clef;
		} else if(pic == null) {
			result = score.getDefaultClef();
		} else {
			result = pic.getEffectiveClef();
		}
		return result;
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
	public KeySignature getEffectiveKeySignature() {
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
	@SuppressWarnings("unused")
	public TimeSignature getAppliedTimeSignature() {
		return frame.getAppliedTimeSignature();
	}

	void handle(final TimeSignature timeSignature) {
		if(clicksSoFar > 0) {
			error("Time signatures must appear at the beginning of a measure");
		}
		frame.report(timeSignature);
	}

	void handle(final Cpm cpm) {
		final int clicks = cpm.clicks;
		if(clicksSoFar > 0) {
			error("CPM must appear at start of measure");
		} else if(clicks == getSize()) {
			warn("CPM does not change measure size?");
		} else {
			frame.report(cpm);
		}
	}

	public boolean isComplete() {
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
		return frame.getEffectiveTimeSignature();
	}

	Measure handle(final Barline barline) {
		final Measure result;
		if(clicksSoFar == frame.actualSize) {
		} else if(clicksSoFar < frame.actualSize) {
			error("Measure underflow: measure is not full");
		} else if(clicksSoFar > frame.actualSize) {
			error("Measure overflow: " + clicksSoFar + "/" + frame.actualSize);
		}
		frame.report(barline);
		result = barline.terminates ? null : new Measure(phrase, this);
		return result;
	}

	boolean isActivated() {
		return events.size() > 0
				|| clef != null
				|| keySignature != null;
	}
}
