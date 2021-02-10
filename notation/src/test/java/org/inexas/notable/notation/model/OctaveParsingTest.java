package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OctaveParsingTest {
	private List<Note> getNotes(final String miki) {
		final List<Note> result = new ArrayList<>();

		final Score score = MikiParser.fromString(miki).score;
		score.getFirstPart().getFirstPhrase().measures.get(0).events.forEach(event -> {
			if(event instanceof Note) {
				result.add((Note) event);
			}
		});

		return result;
	}

	@Test
	void autoOctaveSelection() {
		assertEquals(Notes.F2, getNotes("C -- F").get(1).slot);

		final List<Note> notes = getNotes("C C");
		assertEquals(Notes.C4, notes.get(0).slot);
		assertEquals(Notes.C4, notes.get(1).slot);

		assertEquals(Notes.D4, getNotes("C D").get(1).slot);
		assertEquals(Notes.E4, getNotes("C E").get(1).slot);
		assertEquals(Notes.F4, getNotes("C F").get(1).slot);
		assertEquals(Notes.G3, getNotes("C G").get(1).slot);

		assertEquals(Notes.B3, getNotes("C B").get(1).slot);
		assertEquals(Notes.A3, getNotes("C A").get(1).slot);
		assertEquals(Notes.G3, getNotes("C G").get(1).slot);
		assertEquals(Notes.F4, getNotes("C F").get(1).slot);

		assertEquals(Notes.C5, getNotes("C + C").get(1).slot);
		assertEquals(Notes.C6, getNotes("C ++ C").get(1).slot);
		assertEquals(Notes.C6, getNotes("C + + C").get(1).slot);

		assertEquals(Notes.D4, getNotes("C + D").get(1).slot);
		assertEquals(Notes.D5, getNotes("C ++ D").get(1).slot);
		assertEquals(Notes.D6, getNotes("C +++ D").get(1).slot);

		assertEquals(Notes.C3, getNotes("C - C").get(1).slot);
		assertEquals(Notes.C2, getNotes("C -- C").get(1).slot);
		assertEquals(Notes.C2, getNotes("C - - C").get(1).slot);

		assertEquals(Notes.F3, getNotes("C - F").get(1).slot);
		assertEquals(Notes.F2, getNotes("C -- F").get(1).slot);
		assertEquals(Notes.F1, getNotes("C --- F").get(1).slot);
	}

	@Test
	void subsequentNotes() {
		final List<Note> notes = getNotes("C + C C");
		assertEquals(Notes.C4, notes.get(0).slot);
		assertEquals(Notes.C5, notes.get(1).slot);
		assertEquals(Notes.C5, notes.get(2).slot);
	}

	@Test
	void testSearchSpace() {
		final Note.SearchSpace ss = new Note.SearchSpace(Notes.E1);
		assertEquals(Notes.B0, ss.lookup(Notes.B));
		assertEquals(Notes.E1, ss.lookup(Notes.E));
		assertEquals(Notes.F1, ss.lookup(Notes.F));

		ss.setAnchor(Notes.A0);
		assertEquals(Notes.D1, ss.getAnchor());
		assertEquals(Notes.D1, ss.lookup(Notes.D));
		assertEquals(Notes.A0, ss.lookup(Notes.A));

		ss.setAnchor(Notes.C8);

		ss.setAnchor(Notes.E1);
		assertEquals(Notes.E1, ss.getAnchor());
		ss.moveAnchor(1);

		ss.setAnchor(Notes.E1);
		ss.moveAnchor(-1);
		assertEquals(Notes.D1, ss.getAnchor());
		ss.moveAnchor(-1);
		assertEquals(Notes.D1, ss.getAnchor());

		ss.setAnchor(Notes.E1);
		ss.moveAnchor(2);
	}
}
