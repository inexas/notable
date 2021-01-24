/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class ToMikiVisitor implements Visitor {
	private final static int measuresPerLine = 4;
	private final static char NL = '\n';
	private final StringBuilder sb = new StringBuilder();
	private boolean inRepeat;
	private int measure;
	private Duration currentDuration = Duration.quarter;
	private boolean seenEvent;
	private Duration saveDuration;

	@Override
	public void enter(final Score score) {

		if(score.title != null) {
			writeQuoted("title", score.title);
		}

		if(score.composer != null) {
			writeQuoted("composer", score.composer);
		}
		writeQuoted("header", score.header);

		paragraph();

		final Staff staff = score.staff;
		if(!staff.equals(Staff.trebleC)) {
			visit(staff);
		}

		final KeySignature keySignature = score.key;
		if(keySignature != KeySignature.C) {
			visit(keySignature);
		}

		final TimeSignature timeSignature = score.timeSignature;
		if(timeSignature != TimeSignature.COMMON) {
			visit(timeSignature);
		}

		final PickupMeasure pickupMeasure = score.pickupMeasure;
		if(pickupMeasure != null) {
			visit(pickupMeasure);
		}

		final Tempo tempo = score.tempo;
		if(tempo != Tempo.DEFAULT) {
			visit(tempo);
		}

		paragraph();
	}

	@Override
	public void exit(final Score score) {
		// Nothing on exit
	}

	@Override
	public void enter(final Part part) {
		//noinspection StringEquality
		if(part.name != Part.IMPLICIT) {
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
		currentDuration = Duration.getByDenominator(phrase.part.score.timeSignature.denominator);

		//noinspection StringEquality
		if(phrase.name != Phrase.IMPLICIT) {
			paragraph();
			sb.append("phrase ");
			appendQuoted(phrase.name);
			newline();
		}

		seenEvent = false;
	}

	@Override
	public void exit(final Phrase phrase) {
		// End bar of Phrase is implied
		space();
		if(inRepeat) {
			sb.append(Barline.endRepeat.miki);
			inRepeat = false;
		} else {
			if(seenEvent) {
				sb.append(Barline.doubleBar.miki);
				newline();
			}
		}
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
	public void visit(final Ghost ghost) {
		visitEvent(ghost);
	}

	@Override
	public void visit(final Staff staff) {
		writeUnquoted("staff", staff.type.name());
	}

	@Override
	public void visit(final TimeSignature timeSignature) {
		writeUnquoted("time", timeSignature.name);
	}

	@Override
	public void visit(final PickupMeasure pickupMeasure) {
		writeUnquoted("pickup", pickupMeasure.fraction.toString());
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
		if(measure % measuresPerLine == 0) {
			// At end of line
			if(measure > 0) {
				space();
				add(barline.endOfLineMiki);
				newline();
			}
			add(barline.beginningOfLineMiki);
		} else {
			space();
			add(barline.miki);
		}

		if(barline == Barline.beginRepeat) {
			inRepeat = true;
		} else if(barline == Barline.endRepeat) {
			inRepeat = false;
		}

		measure++;
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
	public String toString() {
		return sb.toString();
	}

	private void visitEvent(final Event event) {
		// Annotations proceed the Event...
		final Map<Class<? extends Annotation>, Annotation> annotations = event.annotations;
		final Articulation articulation = (Articulation) annotations.remove(Articulation.class);
		for(final Annotation annotation : annotations.values()) {
			annotation.accept(this);
		}

		// Now the event itself
		space();
		sb.append(event.getLabel());
		visitDuration(event);

		// Now any Articulation...
		if(articulation != null) {
			articulation.accept(this);
		}
		seenEvent = true;
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
		if(inLine()) {
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
		} // else at start of file
	}

	private boolean inLine() {
		final boolean result;
		if(sb.length() > 0) {
			final char c = sb.charAt(sb.length() - 1);
			result = c != NL && c != '[';
		} else {
			result = false;
		}
		return result;
	}

	private void space() {
		if(inLine()) {
			sb.append(' ');
		}
	}

	private void add(final int i) {
		sb.append(i);
	}

	private void visitDuration(final Event event) {
		final Duration duration = event.duration;
		if(!(currentDuration.equals(duration))) {
			sb.append(duration.denominator);
			if(duration.setDefault) {
				sb.append('*');
				currentDuration = duration;
			}
		}
	}
}
