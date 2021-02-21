package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OctaveParsingTest {
	private List<Note> getNotes(final String miki) {
		final List<Note> result = new ArrayList<>();

		final Score score = MikiParser.fromString(miki).score;
		score.parts.getFirst().phrases.getFirst().measures.get(0).events.forEach(event -> {
			if(event instanceof Note) {
				result.add((Note) event);
			}
		});

		return result;
	}

	@Test
	void autoOctaveSelection() {
		assertEquals(Notes.F3, getNotes("C -- F").get(1).slot);

		final List<Note> notes = getNotes("C C");
		assertEquals(Notes.C5, notes.get(0).slot);
		assertEquals(Notes.C5, notes.get(1).slot);

		assertEquals(Notes.C5, getNotes("BC").get(1).slot);
		assertEquals(Notes.D5, getNotes("BD").get(1).slot);
		assertEquals(Notes.E5, getNotes("BE").get(1).slot);
		assertEquals(Notes.F4, getNotes("BF").get(1).slot);

		assertEquals(Notes.A4, getNotes("BA").get(1).slot);
		assertEquals(Notes.G4, getNotes("BG").get(1).slot);
		assertEquals(Notes.F4, getNotes("BF").get(1).slot);
		assertEquals(Notes.E5, getNotes("BE").get(1).slot);

		assertEquals(Notes.B5, getNotes("B + B").get(1).slot);
		assertEquals(Notes.B6, getNotes("B ++ B").get(1).slot);
		assertEquals(Notes.B6, getNotes("B + + B").get(1).slot);
	}

	@Test
	void subsequentNotes() {
		final List<Note> notes = getNotes("C + C C");
		assertEquals(Notes.C5, notes.get(0).slot);
		assertEquals(Notes.C6, notes.get(1).slot);
		assertEquals(Notes.C6, notes.get(2).slot);
	}

	@Test
	void testSearchSpace() {
		final Note.SearchSpace ss = new Note.SearchSpace(Notes.E1, 0);
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

		Note.SearchSpace ss1 = new Note.SearchSpace(Notes.E4, 0);
		ss1.setAnchor(Notes.E4);
		ss1 = new Note.SearchSpace(Notes.E1, 2);
		ss1.setAnchor(Notes.E5);
	}
}
