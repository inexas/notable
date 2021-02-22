/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class ToMikiVisitor implements Visitor {
	public Timeline timeline;
	private final static char NL = '\n';
	private final StringBuilder sb = new StringBuilder();
	private Duration currentDuration = Duration.quarter;
	private Duration saveDuration;
	private int beamCount;

	@Override
	public void enter(final Score score) {
		timeline = score.timeline;

		writeQuoted("title", score.title);
		writeQuoted("subtitle", score.subtitle);
		writeQuoted("composer", score.composer);
		writeQuoted("header", score.header);
		writeQuoted("copyright", score.copyright);

		paragraph();

		final Clef clef = score.defaultClef;
		if(clef != null && !Clef.treble.equals(clef)) {
			visit(clef);
		}
		final KeySignature keySignature = score.getDefaultKeySignature();
		if(keySignature != null && !KeySignature.C.equals(keySignature)) {
			visit(keySignature);
		}
		final TimeSignature timeSignature = score.getDefaultTimeSignature();
		if(timeSignature != null && !TimeSignature.fourFour.equals(timeSignature)) {
			visit(timeSignature);
		}

		paragraph();
	}

	@Override
	public void exit(final Score score) {
		// Trim trailing whitespace
		int length = sb.length();
		while(length > 0 && Character.isWhitespace(sb.charAt(length - 1))) {
			length--;
		}
		sb.setLength(length);
		if(length > 0) {
			sb.append('\n');
		}
	}

	@Override
	public void enter(final Part part) {
		if(part.name.length() > 0) {
			paragraph();
			writeQuoted("part", part.name);
		}
	}

	@Override
	public void exit(final Part part) {
		// Nothing on exit
	}

	@Override
	public void enter(final Phrase phrase) {
		// fixme
		currentDuration = Duration.getByDenominator(TimeSignature.fourFour.denominator);
		if(phrase.name.length() > 0) {
			paragraph();
			sb.append("phrase ");
			appendQuoted(phrase.name);
			newline();
		}
	}

	@Override
	public void exit(final Phrase phrase) {
		newline();
	}

	@Override
	public void enter(final Chord chord) {
		space();
		sb.append('[');
	}

	@Override
	public void exit(final Chord chord) {
		add(']');
		visitDuration(chord);

		final Articulation articulation = (Articulation) chord.annotations.get(Articulation.class);
		if(articulation != null) {
			sb.append(articulation.miki);
		}
	}

	@Override
	public void enter(final Tuplet tuplet) {
		space();
		add("[t");
		saveDuration = currentDuration;
	}

	@Override
	public void exit(final Tuplet tuplet) {
		add(']');
		currentDuration = saveDuration;
		visitDuration(tuplet);
	}

	@Override
	public void visit(final Note note) {
		visitEvent(note);
	}

	@Override
	public void visit(final Rest rest) {
		visitEvent(rest);
	}

	@Override
	public void visit(final TimeSignature timeSignature) {
		if(timeSignature != null) {
			writeUnquoted("time", timeSignature.name);
		}
	}

	@Override
	public void visit(final Cpm cpm) {
		writeUnquoted("cpm", Integer.toString(cpm.clicks));
	}

	@Override
	public void visit(final KeySignature keySignature) {
		if(keySignature.accidentalCount > 0) {
			space();
			sb.append("key ");
			sb.append(keySignature.name);
		}
	}

	@Override
	public void visit(final Clef clef) {
		writeUnquoted("clef", clef.name());
	}

	@Override
	public void visit(final Tempo tempo) {
		if(tempo.name == null) {
			sb.append("tempo 1/");
			sb.append(tempo.duration.denominator);
			sb.append('=');
			sb.append(tempo.bpm);
		} else {
			writeQuoted("tempo", tempo.name);
		}
	}

	@Override
	public void visit(final Articulation articulation) {
		add(articulation.miki);
	}

	@Override
	public void visit(final Dynamic dynamic) {
		space();
		add(dynamic.name);
	}

	@Override
	public void visit(final Fingering fingering) {
		space();
		add('~');
		add(fingering.label);
	}

	@Override
	public void visit(final Octave octave) {
		space();
		add("{o");
		add(octave.noteCount);
		add(' ');
		add(octave.bars);
		if(octave.beats > 0) {
			add('.');
			add(octave.beats);
		}
		add('}');
	}

	@Override
	public void visit(final Bind bind) {
		space();
		add("{b ");
		add(bind.getNoteCount());
		add('}');
	}

	@Override
	public void visit(final Volta volta) {
		space();
		add("{v");
		add(volta.ordinal);
		add(' ');
		add(volta.bars);
		if(volta.beats > 0) {
			add('.');
			add(volta.beats);
		}
		add('}');
	}

	@Override
	public void visit(final Pedal pedal) {
		line(pedal, "{p");
	}

	@Override
	public void visit(final Decrescendo decrescendo) {
		line(decrescendo, "{d");
	}

	@Override
	public void visit(final Crescendo crescendo) {
		line(crescendo, "{c");
	}

	@Override
	public void visit(final BarRest barRest) {
		line(barRest, "{r");
	}

	@Override
	public void visit(final Barline barline) {
		space();
		add(barline.name());
	}

	@Override
	public void visit(final TextAnnotation textAnnotation) {
		space();
		add('"');
		add(textAnnotation.text);
		add('"');
	}

	@Override
	public void visit(final Mode mode) {
		sb.append(mode.miki);
	}

	@Override
	public void visit(final NamedChord namedChord) {
		visitEvent(namedChord);
	}

	@Override
	public void visit(final Accidental accidental) {
		sb.append(accidental.miki);
	}

	@Override
	public void visit(final Beam beam) {
		space();
		sb.append('(');
		beamCount = beam.count;
	}

	@Override
	public void visit(final Measure measure) {
		space();
		final List<Event> events = measure.events;
		final Cpm cpm = timeline.getFrame(measure.ordinal).cpm;
		if(cpm != null) {
			visit(cpm);
		}
		for(final Event event : events) {
			event.accept(this);
			space();
		}
		sb.append(measure.frame.barline.miki);
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	private Map<Class<? extends Annotation>, Annotation> annotations;

	private void visitEvent(final Event event) {
		space();

		// Deal with annotations that proceed the Event...
		annotations = event.annotations;
		annotate(Dynamic.class);
		annotate(Bind.class);
		annotate(Pedal.class);
		annotate(Octave.class);
		annotate(Crescendo.class);
		annotate(Decrescendo.class);
		annotate(Volta.class);
		annotate(BarRest.class);
		annotate(Fingering.class);
		annotate(TextAnnotation.class);
		annotate(Beam.class);

		// Now the event itself
		sb.append(event.getLabel());
		final Accidental accidental = (Accidental) event.annotations.get(Accidental.class);
		if(accidental != null) {
			visit(accidental);
		}
		visitDuration(event);

		// Now any Articulation...
		final Articulation articulation = (Articulation) annotations.get(Articulation.class);
		if(articulation != null) {
			articulation.accept(this);
		}

		// End beam
		if(beamCount > 0) {
			if(--beamCount == 0) {
				sb.append(')');
			}
		}
	}

	private void annotate(final Class<? extends Annotation> clazz) {
		boolean space = true;
		final Annotation annotation = annotations.get(clazz);
		if(annotation != null) {
			if(Beam.class.equals(clazz)) {
				visit((Beam) annotation);
				space = false;
			} else if(Dynamic.class.equals(clazz)) {
				visit((Dynamic) annotation);
			} else if(Bind.class.equals(clazz)) {
				visit((Bind) annotation);
			} else if(Pedal.class.equals(clazz)) {
				visit((Pedal) annotation);
			} else if(Octave.class.equals(clazz)) {
				visit((Octave) annotation);
			} else if(Crescendo.class.equals(clazz)) {
				visit((Crescendo) annotation);
			} else if(Decrescendo.class.equals(clazz)) {
				visit((Decrescendo) annotation);
			} else if(Volta.class.equals(clazz)) {
				visit((Volta) annotation);
			} else if(BarRest.class.equals(clazz)) {
				visit((BarRest) annotation);
			} else if(Fingering.class.equals(clazz)) {
				visit((Fingering) annotation);
			} else if(TextAnnotation.class.equals(clazz)) {
				visit((TextAnnotation) annotation);
			}
			if(space) {
				space();
			}
		}
	}

	private void line(final Line line, final String leader) {
		space();
		add(leader);
		add(' ');
		add(line.bars);
		if(line.beats > 0) {
			add('.');
			add(line.beats);
		}
		add('}');
	}

	private void appendQuoted(final String string) {
		sb.append('"');
		sb.append(string);
		sb.append('"');
	}

	private void writeUnquoted(final String keyword, final String value) {
		if(value != null) {
			newline();
			sb.append(keyword);
			sb.append(' ');
			sb.append(value);
			newline();
		}
	}

	private void writeQuoted(final String keyword, final String value) {
		if(value != null) {
			newline();
			sb.append(keyword);
			sb.append(" \"");
			sb.append(value);
			sb.append("\"\n");
			newline();
		}
	}

	private void add(final String s) {
		sb.append(s);
	}

	private void add(final char c) {
		sb.append(c);
	}

	/**
	 * Write a new line if the last written character was not a newline
	 */
	private void newline() {
		final int length = sb.length();
		if(length > 0 && sb.charAt(length - 1) != NL) {
			sb.append(NL);
		}
	}

	/**
	 * Ensure the last two characters written were newlines or that we are at the
	 * beginning
	 */
	private void paragraph() {
		final int length = sb.length();
		if(length > 0) {
			if(sb.charAt(length - 1) == NL) {
				if(length > 1 && sb.charAt(length - 2) != NL) {
					sb.append(NL);
				}
			} else {
				sb.append("\n\n");
			}
		}
	}

	private void space() {
		final int length = sb.length();
		if(length > 0) {
			final char lastC = sb.charAt(length - 1);
			if(lastC != NL && lastC != ' ' && lastC != '[' && lastC != '(') {
				sb.append(' ');
			}
		}
	}

	private void add(final int i) {
		sb.append(i);
	}

	private void visitDuration(final Event event) {
		final Duration duration = event.duration;
		// No need to write anything if it's the default
		if(!(currentDuration.equals(duration))) {
			sb.append(duration.miki);
			if(duration.setDefault) {
				currentDuration = duration;
			}
		}
	}
}
