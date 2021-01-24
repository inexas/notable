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
				Note.get(4, Duration.quarter, noAnnotations));
		assertTrue(e.getMessage().contains("range"));

		e = assertThrows(AssertionError.class, () ->
				Note.get(4, Duration.quarter, noAnnotations));
		assertTrue(e.getMessage().contains("range"));
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	void testNoteNumberConversion() {
		// Range checks
		assertThrows(AssertionError.class, () -> Note.position(0, -1));
		assertThrows(AssertionError.class, () -> Note.position(0, 8));
		assertThrows(AssertionError.class, () -> Note.position(0, 4));
		assertThrows(AssertionError.class, () -> Note.position(8, 1));
		assertEquals(5, Note.position(0, 5));
		assertEquals(56, Note.position(8, 0));

		// Calculation
		assertEquals(4, Note.octave(28));
		assertEquals(28, Note.position(4, "C"));
	}

	@Test
	void testTonics() {
		assertEquals(0, Note.get(28, Duration.quarter, noAnnotations).tonic);
		assertEquals(1, Note.get(29, Duration.quarter, noAnnotations).tonic);

		assertEquals(0, Note.C);
		assertEquals(1, Note.D);
		assertEquals(2, Note.E);
		assertEquals(3, Note.F);
		assertEquals(4, Note.G);
		assertEquals(5, Note.A);
		assertEquals(6, Note.B);
	}

	@Test
	void testNext() {
		assertEquals(28, Note.next(28, 4, 0, "C"));
		// Lower limit...
		assertEquals(5, Note.next(5, -1, 0, "A"));
		assertEquals(8, Note.next(5, -1, 0, "D"));
		assertEquals(11, Note.next(5, -1, 0, "G"));
		// Upper limit...
		assertEquals(56, Note.next(56, -1, 0, "C"));
		assertEquals(50, Note.next(56, -1, 0, "D"));
		assertEquals(55, Note.next(56, -1, 0, "B"));
	}

	@Test
	void testToString() {
		final Note note = Note.get(28, Duration.quarter, noAnnotations);
		assertEquals("C4", note.name);
	}

	@Test
	void testIsValid() {
		assertTrue(Note.isValid(5));
		assertTrue(Note.isValid(28));
		assertTrue(Note.isValid(56));

		assertFalse(Note.isValid(4));
		assertFalse(Note.isValid(57));
	}

	@Test
	void testName() {
		assertEquals("C4", Note.name(Note.C4));
		assertEquals("A0", Note.name(5));
		assertEquals("C8", Note.name(8 * Note.BASE));
	}
}
