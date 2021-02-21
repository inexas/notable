package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

public class ScoreCheckVisitor implements Visitor {
	private Timeline timeline;
	private final Messages messages;
	private int measureCount;
	private String partName, phraseName;

	ScoreCheckVisitor(final Messages messages) {
		this.messages = messages;
	}

	@Override
	public void enter(final Score score) {
		timeline = score.timeline;
		measureCount = timeline.size();
	}

	@Override
	public void exit(final Score score) {
	}

	@Override
	public void enter(final Part part) {
		partName = part.name.length() == 0 ? "(Anonymous)" : part.name;
	}

	@Override
	public void exit(final Part part) {
	}

	@Override
	public void enter(final Phrase phrase) {
		phraseName = phrase.name.length() == 0 ? "(Anonymous)" : phrase.name;

		for(int i = 0; i < measureCount; i++) {
			final Measure measure = phrase.measures.get(i);
			assert measure.getSize() == timeline.getFrame(i).actualSize : "Coding error";
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
		final Timeline.Frame frame = measure.frame;
		if(frame.isLast()) {
			final Barline is = frame.barline;
			final Barline shouldBe = frame.isRepeat() ? Barline.eosRepeat : Barline.eos;
			if(is == null) {
				messages.error("Missing barline at end of measure");
			} else if(is != shouldBe) {
				messages.error("Incorrect barline at end of phrase, should be " + shouldBe.miki);
				// Todo add measure/phrase to message
			}
			frame.barline = shouldBe;
		} else {
			final Barline is = frame.barline;
			final Barline shouldBe;
			if(frame.isRepeat()) {
				shouldBe = frame.nic.isRepeat() ? Barline.beginEndRepeat : Barline.endRepeat;
			} else {
				if(frame.nic.isRepeat()) {
					shouldBe = Barline.beginRepeat;
				} else {
					// No repeats
					if(is == Barline.singleBar || is == Barline.doubleBar) {
						shouldBe = is;
					} else {
						shouldBe = Barline.singleBar;
					}
				}
			}
			if(is != shouldBe) {
				messages.error("Barline incorrect at end of phrase, should be " + shouldBe.miki);
			}
		}
	}

	@Override
	public void visit(final Cpm cpm) {
	}
}
