/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.antlr.v4.runtime.*;
import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

import java.util.*;
import java.util.regex.*;


/**
 * A piano has two Phrases, one for each hand
 */
public class Phrase extends Element {
	private final Messages messages;
	public final Part part;
	public final String name;
	// Parsing state variables...
	public final List<Measure> measures = new ArrayList<>();
	private Measure measure;
	private Clef clef;
	// To collect Annotations for the current or next Event
	private Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
	private static final Map<Class<? extends Annotation>, Annotation> mtAnnotationMap = Map.of();
	/**
	 * Set by an o8 command
	 */
	private int absoluteOctave = -1;
	private int relativeOctave;
	private int lastNote;
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
	private Duration duration;

	Phrase(final Messages messages, final String name, final Part part) {
		this.messages = messages;
		this.name = name;
		this.part = part;
		measure = new Measure(this, null);
		measures.add(measure);
		venue = measure;
		final int denominator = part.score.getTimeSignature().denominator;
		duration = Duration.getByDenominator(denominator);
	}

	public Measure getOpenMeasure() {
		// todo Is this check necessary
		if(measure.isComplete()) {
			measure = new Measure(this, measure);
			measures.add(measure);
		}
		return measure;
	}

	private Measure newMeasure() {
		final Measure result = new Measure(this, measures.get(measures.size() - 1));
		measures.add(result);
		return result;
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

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Measure measure : measures) {
			measure.accept(visitor);
		}
		visitor.exit(this);
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
		if(total > measure.size) {
			warn("Tuplet duration to long, reduced");
			final Duration[] durations = Duration.getByClicks(total - measure.size);
			duration = durations[durations.length - 1];
		}
		pop();
		venue.add(tuplet);
	}

	public void buildEvent(final ParserRuleContext ctx) {
		final Event event;

		final String input = ctx.stop.getText();
		// Split up the Event into its parts..
		final Matcher matcher = Notes.notePattern.matcher(input);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch: '" + input + '\'');
		}

		// Group 1: Tonic...
		final String tonic = matcher.group(1);

		// Group 2: Duration...
		final Duration duration;
		final String group2 = matcher.group(2);
		if(group2 == null) {
			duration = this.duration;
		} else {
			duration = Duration.getByMiki(group2);
			if(duration.setDefault) {
				this.duration = duration;
			}
		}

		// Group 2: Accidental...
		final String group3 = matcher.group(3);
		if(group3 != null) {
			switch(group3.charAt(0)) {
				case 'b' -> annotate(Accidental.flat);
				case 'n' -> annotate(Accidental.natural);
				case '#' -> annotate(Accidental.sharp);
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 4: Articulation...
		final String group5 = matcher.group(4);
		if(group5 != null) {
			final Articulation articulation = Articulation.getByMiki(group5);
			annotate(articulation);
		}

		final char c = tonic.charAt(0);
		final Map<Class<? extends Annotation>, Annotation> annotations;
		if(annotationMap.isEmpty()) {
			annotations = mtAnnotationMap;
		} else {
			annotations = annotationMap;
			annotationMap = new HashMap<>();
		}
		if(c == 'R') {
			event = new Rest(duration, annotations);
		} else {
			final int number = Note.next(
					lastNote,
					absoluteOctave, relativeOctave,
					tonic);
			event = new Note(number, duration, false, annotations);
		}

		if(event instanceof Note) {
			lastNote = event.slot;
		}

		venue.add(event);

		// Ready for next
		relativeOctave = 0;
		absoluteOctave = -1;
	}

	public void handle(final Barline barline) {
		final int clicksSoFar = measure.clicksSoFar;
		if(clicksSoFar > 0) {   // Ignore barlines at beginning of measure
			if(!measure.isComplete()) {
				error("Barline before end of measure");
			} else {
				part.score.accountFor(measure);
				measure = newMeasure();
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

	public void handle(final KeySignature key) {
		measure.setKeySignature(messages, key);
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


	public void handle(final Clef clef) {
		throw new ImplementMeException();
	}

	public void handle(final TimeSignature timeSignature) {
		// todo measure??
	}

	public void handle(final Cpm cpm) {
		measure.setCpm(messages, cpm.clicks);
	}
}
