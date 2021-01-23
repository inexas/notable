package org.inexas.notable.notation.model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
	private final Map<Class<? extends Annotation>, Annotation> noAnnotations = Map.of();

	@Test
	void testParameters() {
		AssertionError e;
		e = assertThrows(AssertionError.class, () ->
				new Note("Z", 4, Duration.quarter, noAnnotations));
		assertTrue(e.getMessage().contains("note"));

		e = assertThrows(AssertionError.class, () ->
				new Note("Z", 4, Duration.quarter, noAnnotations));
		assertTrue(e.getMessage().contains("note"));
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	void testNoteNumberConversion() {
		// Range checks
		assertThrows(AssertionError.class, () -> Note.number(0, -1));
		assertThrows(AssertionError.class, () -> Note.number(0, 12));
		assertThrows(AssertionError.class, () -> Note.number(0, 8));
		assertThrows(AssertionError.class, () -> Note.number(8, 1));
		assertEquals(9, Note.number(0, 9));
		assertEquals(96, Note.number(8, 0));

		// Calculation
		assertEquals(4, Note.octave(48));
		assertEquals(0, Note.tonic(48));
		assertEquals(48, Note.number(4, "C"));
	}

	@Test
	void testTonics() {
		assertEquals(0, new Note("C", 48, Duration.quarter, noAnnotations).tonic);
		assertEquals(1, new Note("C#", 49, Duration.quarter, noAnnotations).tonic);

		assertEquals(0, Note.C);
		assertEquals(1, Note.Cs);
		assertEquals(1, Note.Db);
		assertEquals(2, Note.D);
		assertEquals(3, Note.Ds);
		assertEquals(3, Note.Eb);
		assertEquals(4, Note.E);
		assertEquals(5, Note.F);
		assertEquals(6, Note.Fs);
		assertEquals(6, Note.Gb);
		assertEquals(7, Note.G);
		assertEquals(8, Note.Gs);
		assertEquals(8, Note.Ab);
		assertEquals(9, Note.A);
		assertEquals(10, Note.As);
		assertEquals(10, Note.Bb);
		assertEquals(11, Note.B);
	}

	@Test
	void testNext() {
		// C4 given C is C4
		assertEquals(48, Note.next(48, 4, 0, "C"));
		// Lower limit...
		assertEquals(9, Note.next(9, -1, 0, "A"));
		assertEquals(15, Note.next(9, -1, 0, "D#"));
		assertEquals(10, Note.next(9, -1, 0, "A#"));
		assertEquals(20, Note.next(9, -1, 0, "G#"));
		// Upper limit...
		assertEquals(96, Note.next(96, -1, 0, "C"));
		assertEquals(85, Note.next(96, -1, 0, "C#"));
		assertEquals(95, Note.next(96, -1, 0, "B"));
	}

	@Test
	void testToString() {
		final Note note = new Note("C", 48, Duration.quarter, noAnnotations);
		assertEquals("C", note.toString());
	}

	@Test
	void testIsValid() {
		assertTrue(Note.isValid(9));
		assertTrue(Note.isValid(29));
		assertTrue(Note.isValid(96));

		assertFalse(Note.isValid(8));
		assertFalse(Note.isValid(97));
	}

	@Test
	void testName() {
		assertEquals("C4", Note.name(Note.C4));
		assertEquals("A0", Note.name(9));
		assertEquals("C8", Note.name(8 * 12));
	}
}
