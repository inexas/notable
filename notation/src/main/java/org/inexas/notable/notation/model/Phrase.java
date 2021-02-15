/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

import java.util.*;
import java.util.regex.*;


/**
 * A piano has two Phrases, one for each hand
 */
public class Phrase extends Element implements MappedList.Named {
	private final Messages messages;
	private final Score score;
	public final Part part;
	public final String name;
	// Parsing state variables...
	public final List<Measure> measures = new ArrayList<>();
	private Measure measure;
	// To collect Annotations for the current or next Event
	public Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
	/**
	 * Set by an o8 command
	 */
	private int absoluteOctave = -1;
	private int relativeOctave;
	public int lastNote;
	/**
	 * This is used to save the state of the duration when a Note group is being
	 * processed. For example, in the miki "C4* [t C E G]8 A" the duration is defaulted
	 * to 4 then in the Note group it's set to eighths but without a default '*'
	 * so the trailing A will have a duration of 4. In this case durationStack
	 * will save the 'quarter' duration set with "C4*"
	 */
	private Duration saveDuration;
	private Venue venue;
	private Venue saveVenue;
	public Duration duration;

	Phrase(final String name, final Part part) {
		this.name = name;
		this.part = part;
		score = part.score;
		messages = score.messages;
		newMeasure();
		duration = measure.getEffectiveTimeSignature().getDefaultDuration();
	}

	@Override
	public String getName() {
		return name;
	}

	private void newMeasure() {
		final int count = measures.size();
		final Measure pic = count == 0 ? null : measures.get(count - 1);
		measure = new Measure(this, pic);
		measures.add(measure);
		venue = measure;
	}

	public int getActiveMeasureCount() {
		return measure.isActive ? measure.ordinal + 1 : measure.ordinal;
	}

	private void push(final Venue venue) {
		assert saveVenue == null;
		saveVenue = this.venue;
		this.venue = venue;
		saveDuration = duration;
	}

	private void pop() {
		assert saveVenue != null;
		venue = saveVenue;
		duration = saveDuration;
	}

	public void setRelativeOctave(final int change) {
		if(change > 0) {
			if(absoluteOctave >= 0 || relativeOctave < 0) {
				warn("Possible confusion over octaves?");
			}
		} else { // direction < 0;
			if(absoluteOctave >= 0 || relativeOctave > 0) {
				warn("Possible confusion over octaves?");
			}
		}
		relativeOctave += change;
	}

	public void setAbsoluteOctave(final int absoluteOctave) {
		if(relativeOctave != 0) {
			warn("Possible confusion over octaves?");
			relativeOctave = 0;
		}
		this.absoluteOctave = absoluteOctave;
	}

	public void startChord() {
		push(new Chord(duration));
	}

	public void endChord(final String closeText) {
		final Chord chord = (Chord) venue;
		final Matcher matcher = Notes.noteGroupEndPattern.matcher(closeText);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 2: Articulation...
		final String group1 = matcher.group(1);
		if(group1 != null) {
			final Articulation articulation = Articulation.getByMiki(group1);
			if(annotationMap.put(articulation.getClass(), articulation) != null) {
				warn("Annotation overwrites previous value: " + articulation);
			}
		}

		// Group 2: Duration...
		final String group2 = matcher.group(2);
		final Duration duration;
		if(group2 == null) {
			duration = this.duration;
		} else {
			duration = Duration.getByMiki(group2);
			if(duration.setDefault) {
				this.duration = duration;
			}
		}
		chord.duration = duration;

		venue.add(chord);

		pop();
	}

	public void addNamedChord(final String text) {
		final NamedChord namedChord = NamedChord.parse(text, duration, annotationMap);
		venue.add(namedChord);
	}

	public void startTuplet() {
		final Tuplet tuplet = new Tuplet((duration));
		push(tuplet);
	}

	public void endTuplet(final String text) {
		final Tuplet tuplet = (Tuplet) venue;
		if(text.length() > 1) {
			final Matcher matcher = Notes.noteGroupEndPattern.matcher(text);
			if(!matcher.matches()) {
				throw new RuntimeException("Recognizer/event parser mismatch");
			}

			if(matcher.group(1) != null) {
				warn("Tuplet articulation not supported, ignored: " + text);
			}

			// Group 2: Duration...
			final String group2 = matcher.group(2);
			final Duration duration;
			if(group2 != null) {
				duration = Duration.getByMiki(group2);
				if(duration.setDefault) {
					saveDuration = duration;
				}
			} else {
				// No duration but an erroneous articulation
				duration = this.duration;
			}
			tuplet.duration = duration;
		}
		final int total = measure.clicksSoFar + duration.clicks;
		if(total > measure.getSize()) {
			warn("Tuplet duration to long, reduced");
			final Duration[] durations = Duration.getByClicks(total - measure.getSize());
			duration = durations[durations.length - 1];
		}
		pop();
		venue.add(tuplet);
	}

	public void handle(final Barline barline) {
		final int clicksSoFar = measure.clicksSoFar;
		if(clicksSoFar > 0) {   // Ignore barlines at beginning of measure
			if(!measure.isComplete()) {
				error("Barline before end of measure");
			} else {
				newMeasure();
			}
		}
		//		messages.ctx = ctx;
//		// Leading barline...
//		final boolean barAnnotated = annotationMap.containsKey(Barline.class);
//		if(measure.clicksSoFar == 0) {
//			// We're at the start of a new measure, make sure it's annotated...
//			if(!barAnnotated) {
//				annotate(Barline.bar);
//			}
//		} else if(barAnnotated) {
//			// todo Removing a repeat bar may cause other issues
//			annotationMap.remove(Barline.class);
//			warn("Bar annotation not at start of measure; removed.");
//		}
//	}
//
	}

	/**
	 * Calculator that given the last note slot and a new tonic, calculates
	 * the next slot.
	 *
	 * @param tonicText The tonic to move to, e.g. "C"
	 * @return The note slot of the next note
	 */
	public int next(final String tonicText) {
		final int result;

		final int tonic = Notes.tonic(tonicText);

		if(absoluteOctave >= 0) {
			// Absolute octave has been specified
			assert relativeOctave == 0;
			assert absoluteOctave < 8 || absoluteOctave == 8 && tonic == 0;
			result = absoluteOctave * Notes.BASE + tonic;
		} else {
			final Note.SearchSpace searchSpace = new Note.SearchSpace(lastNote);
			if(relativeOctave != 0) {
				searchSpace.moveAnchor(relativeOctave);
			}
			result = searchSpace.lookup(tonic);
		}

		return result;
	}

	public void handle(final Clef clef) {
		measure.handle(clef);
	}

	public void handle(final TimeSignature timeSignature) {
		measure.handle(timeSignature);
	}

	public void handle(final Cpm cpm) {
		measure.handle(cpm);
	}

	public void handle(final KeySignature key) {
		measure.setKeySignature(key);
	}

	public void annotate(final Annotation annotation) {
		if(annotationMap.put(annotation.getClass(), annotation) != null) {
			warn("Annotation overwrites previous value: " + annotation);
		}
	}

	private void warn(final String message) {
		messages.warn(message);
	}

	private void error(final String message) {
		messages.error(message);
	}

	public void handle(final Event event) {
		measure.add(event);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Measure measure : measures) {
			if(measure.isActive) {
				measure.accept(visitor);
			}
		}
		visitor.exit(this);
	}
}
