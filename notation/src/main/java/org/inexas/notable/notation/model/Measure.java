package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Measure extends Element implements Venue {
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
	 * Null or change of key
	 */
	private KeySignature keySignature;
	/**
	 * Null or change of time
	 */
	private TimeSignature timeSignature;
	/**
	 * Size of this measure in clicks. This is set by a time signature
	 * and may be overridden by a CPM. If there is neither a time signature
	 * nor a cpm for this measures then a default is used.
	 */
	public int size;
	/**
	 * The clicksSoFar counts up the clicks used each time an event is
	 * added to the measure.
	 */
	public int clicksSoFar;
	/**
	 * The list of events in this measure
	 */
	public final List<Event> events = new ArrayList<>();

	private int cpm;

	Measure(final Score score, final Measure previous) {
		this.score = score;
		pic = previous;
		ordinal = pic == null ? 0 : pic.ordinal + 1;
		size = score.getDefaultClicks();
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

	public Clef getClef() {
		final Clef result;
		if(clef == null) {
			// No explicit clef, check for a previous definition...
			if(pic == null) {
				// We're first in chain (fic) so check anonymous fic...
				final Measure anonymousFic = score.anonymousFicMeasure;
				if(anonymousFic.clef == null) {
					result = Clef.treble;
				} else {
					result = anonymousFic.clef;
				}
			} else {
				result = pic.getClef();
			}
		} else {
			result = clef;
		}
		return result;
	}

	public void setClef(final Messages messages, final Clef clef) {
		messages.error("Clef already set for this measure");
		if(clicksSoFar > 0) {
			messages.error("Clefs must appear at the beginning of a measure");
		} else {
			this.clef = clef;
		}
	}

	public KeySignature getKeySignature() {
		final KeySignature result;
		if(clef == null) {
			result = pic == null ? KeySignature.C : pic.getKeySignature();
		} else {
			result = keySignature;
		}
		return result;
	}

	public void setKeySignature(final Messages messages, final KeySignature keySignature) {
		if(clicksSoFar > 0) {
			messages.error("Key signatures must appear at the beginning of a measure");
		}
		if(getKeySignature() != null) {
			messages.error("Key signature already set for this measure");
		}
		this.keySignature = keySignature;
	}

	public TimeSignature getTimeSignature() {
		final TimeSignature result;
		if(timeSignature == null) {
			result = pic == null ? TimeSignature.fourFour : pic.getTimeSignature();
		} else {
			result = timeSignature;
		}
		return result;
	}

	public void setTimeSignature(final Messages messages, final TimeSignature timeSignature) {
		if(clicksSoFar > 0) {
			messages.error("Time signatures must appear at the beginning of a measure");
		}
		if(timeSignature != null) {
			messages.error("Time signature already set for this measure");
		}
		this.timeSignature = timeSignature;
		size = timeSignature.getMeasureSize();
	}

	public int getCpm() {
		return cpm;
	}

	public void setCpm(final Messages messages, final int cpm) {
		if(clicksSoFar > 0) {
			messages.error("CPM must appear at start of measure");
		} else if(this.cpm != 0) {
			messages.error("Maximum one CPM per measure");
		} else {
			this.cpm = cpm;
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
		assert clicksSoFar <= size;
		return clicksSoFar == size;
	}
}
