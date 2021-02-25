/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

public interface Visitor {
	void enter(Score score);

	void exit(Score score);

	void enter(Part part);

	void exit(Part part);

	void enter(Phrase phrase);

	void exit(Phrase phrase);

	void enter(Chord chord);

	void exit(Chord chord);

	void enter(Tuplet tuplet);

	void exit(Tuplet tuplet);

	void visit(Note note);

	void visit(Rest rest);

	void visit(TimeSignature timeSignature);

	void visit(KeySignature keySignature);

	void visit(Tempo tempo);

	void visit(Articulation articulation);

	void visit(Dynamic dynamic);

	void visit(Fingering fingering);

	void visit(Octave octave);

	void visit(Bind slur);

	void visit(Volta volta);

	void visit(Pedal pedal);

	void visit(Decrescendo decrescendo);

	void visit(Crescendo crescendo);

	void visit(MultimeasureRest multimeasureRest);

	void visit(Barline barline);

	void visit(TextAnnotation textAnnotation);

	void visit(Mode mode);

	void visit(NamedChord namedChord);

	void visit(Accidental accidental);

	void visit(Beam beam);

	void visit(Clef clef);

	void visit(Cpm cpm);

	void visit(Measure measure);
}
