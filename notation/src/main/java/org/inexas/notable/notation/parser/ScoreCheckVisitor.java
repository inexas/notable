package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

public class ScoreCheckVisitor implements Visitor {
	private final Messages messages;
	private int[] timeLine;
	private int partCount, phraseCount, totalPhraseCount, measureCount;
	private String partName, phraseName;

	ScoreCheckVisitor(final Messages messages) {
		this.messages = messages;
	}

	@Override
	public void enter(final Score score) {
		timeLine = score.getTimeLine();
		partCount = score.parts.size();
	}

	@Override
	public void exit(final Score score) {
	}

	@Override
	public void enter(final Part part) {
		partName = part.name.length() == 0 ? "(Anonymous)" : part.name;
		phraseCount = part.phrases.size();
		totalPhraseCount += phraseCount;
	}

	@Override
	public void exit(final Part part) {
	}

	@Override
	public void enter(final Phrase phrase) {
		phraseName = phrase.name.length() == 0 ? "(Anonymous)" : phrase.name;

		for(int i = 0; i < timeLine.length; i++) {
			final int size = timeLine[i];
			final Measure measure = phrase.measures.get(i);
			assert measure.getSize() == timeLine[i] : "Coding error";
			if(!measure.isComplete()) {
				messages.error(partName + '/' + phraseName + '-' + i + ": "
						+ "Measure not complete");
			}
		}
	}

	@Override
	public void exit(final Phrase phrase) {
	}

	@Override
	public void enter(final Chord chord) {

	}

	@Override
	public void exit(final Chord chord) {

	}

	@Override
	public void enter(final Tuplet tuplet) {

	}

	@Override
	public void exit(final Tuplet tuplet) {

	}

	@Override
	public void visit(final Note note) {

	}

	@Override
	public void visit(final Rest rest) {

	}

	@Override
	public void visit(final Staff staff) {

	}

	@Override
	public void visit(final TimeSignature timeSignature) {

	}

	@Override
	public void visit(final KeySignature keySignature) {

	}

	@Override
	public void visit(final Tempo tempo) {

	}

	@Override
	public void visit(final Articulation articulation) {

	}

	@Override
	public void visit(final Dynamic dynamic) {

	}

	@Override
	public void visit(final Fingering fingering) {

	}

	@Override
	public void visit(final Octave octave) {

	}

	@Override
	public void visit(final Bind slur) {

	}

	@Override
	public void visit(final Volta volta) {

	}

	@Override
	public void visit(final Pedal pedal) {

	}

	@Override
	public void visit(final Decrescendo decrescendo) {

	}

	@Override
	public void visit(final Crescendo crescendo) {

	}

	@Override
	public void visit(final BarRest barRest) {

	}

	@Override
	public void visit(final Barline barline) {

	}

	@Override
	public void visit(final TextAnnotation textAnnotation) {

	}

	@Override
	public void visit(final Mode mode) {

	}

	@Override
	public void visit(final NamedChord namedChord) {

	}

	@Override
	public void visit(final Accidental accidental) {

	}

	@Override
	public void visit(final Beam beam) {

	}

	@Override
	public void visit(final Clef clef) {

	}

	@Override
	public void visit(final Measure measure) {
	}

	@Override
	public void visit(final Cpm cpm) {
	}
}
