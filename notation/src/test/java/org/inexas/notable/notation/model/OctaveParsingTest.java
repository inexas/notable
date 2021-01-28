package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OctaveParsingTest {
	private List<Note> getNotes(final String miki) {
		final List<Note> returnValue = new ArrayList<>();

		final Score score = MikiParser.fromString(miki).score;
		score.getFirstPart().getFirstPhrase().events.forEach(event -> {
			if(event instanceof Note) {
				returnValue.add((Note) event);
			}
		});

		return returnValue;
	}

	@Test
	void autoOctaveSelection() {
		assertEquals(Note.F2, getNotes("C -- F").get(1).slot);

		final List<Note> notes = getNotes("C C");
		assertEquals(Note.C4, notes.get(0).slot);
		assertEquals(Note.C4, notes.get(1).slot);

		assertEquals(Note.D4, getNotes("C D").get(1).slot);
		assertEquals(Note.E4, getNotes("C E").get(1).slot);
		assertEquals(Note.F4, getNotes("C F").get(1).slot);
		assertEquals(Note.G3, getNotes("C G").get(1).slot);

		assertEquals(Note.B3, getNotes("C B").get(1).slot);
		assertEquals(Note.A3, getNotes("C A").get(1).slot);
		assertEquals(Note.G3, getNotes("C G").get(1).slot);
		assertEquals(Note.F4, getNotes("C F").get(1).slot);

		assertEquals(Note.C5, getNotes("C + C").get(1).slot);
		assertEquals(Note.C6, getNotes("C ++ C").get(1).slot);
		assertEquals(Note.C6, getNotes("C + + C").get(1).slot);

		assertEquals(Note.D4, getNotes("C + D").get(1).slot);
		assertEquals(Note.D5, getNotes("C ++ D").get(1).slot);
		assertEquals(Note.D6, getNotes("C +++ D").get(1).slot);

		assertEquals(Note.C3, getNotes("C - C").get(1).slot);
		assertEquals(Note.C2, getNotes("C -- C").get(1).slot);
		assertEquals(Note.C2, getNotes("C - - C").get(1).slot);

		assertEquals(Note.F3, getNotes("C - F").get(1).slot);
		assertEquals(Note.F2, getNotes("C -- F").get(1).slot);
		assertEquals(Note.F1, getNotes("C --- F").get(1).slot);
	}

	@Test
	void subsequentNotes() {
		final List<Note> notes = getNotes("C + C C");
		assertEquals(Note.C4, notes.get(0).slot);
		assertEquals(Note.C5, notes.get(1).slot);
		assertEquals(Note.C5, notes.get(2).slot);
	}

	@Test
	void testSearchSpace() {
		final Note.SearchSpace ss = new Note.SearchSpace(Note.E1);
		assertEquals(Note.B0, ss.lookup(Note.B));
		assertEquals(Note.E1, ss.lookup(Note.E));
		assertEquals(Note.F1, ss.lookup(Note.F));

		ss.setAnchor(Note.A0);
		assertEquals(Note.D1, ss.getAnchor());
		assertEquals(Note.D1, ss.lookup(Note.D));
		assertEquals(Note.A0, ss.lookup(Note.A));

		ss.setAnchor(Note.C8);

		ss.setAnchor(Note.E1);
		assertEquals(Note.E1, ss.getAnchor());
		ss.moveAnchor(1);

		ss.setAnchor(Note.E1);
		ss.moveAnchor(-1);
		assertEquals(Note.D1, ss.getAnchor());
		ss.moveAnchor(-1);
		assertEquals(Note.D1, ss.getAnchor());

		ss.setAnchor(Note.E1);
		ss.moveAnchor(2);
	}
}
