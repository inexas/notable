package org.inexas.notable.notation.model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {
	private final Map<Class<? extends Annotation>, Annotation> noAnnotations = Map.of();

	//	@Test
//	void testParameters() {
//		AssertionError e;
//		e = assertThrows(AssertionError.class, () ->
//				Note.get(4, Duration.quarter, noAnnotations));
//		assertTrue(e.getMessage().contains("range"));
//
//		e = assertThrows(AssertionError.class, () ->
//				Note.get(4, Duration.quarter, noAnnotations));
//		assertTrue(e.getMessage().contains("range"));
//	}
//
	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	void testNoteNumberConversion() {
		// Range checks
		assertThrows(AssertionError.class, () -> Notes.slot(0, -1));
		assertThrows(AssertionError.class, () -> Notes.slot(0, 8));
		assertThrows(AssertionError.class, () -> Notes.slot(0, 4));
		assertThrows(AssertionError.class, () -> Notes.slot(8, 1));
		assertEquals(5, Notes.slot(0, 5));
		assertEquals(56, Notes.slot(8, 0));

		// Calculation
		assertEquals(4, Notes.octave(28));
		assertEquals(28, Notes.slot(4, "C"));
	}

//	@Test
//	void testTonics() {
//		assertEquals(0, Note.get(28, Duration.quarter, noAnnotations).tonic);
//		assertEquals(1, Note.get(29, Duration.quarter, noAnnotations).tonic);
//
//		assertEquals(0, Note.C);
//		assertEquals(1, Note.D);
//		assertEquals(2, Note.E);
//		assertEquals(3, Note.F);
//		assertEquals(4, Note.G);
//		assertEquals(5, Note.A);
//		assertEquals(6, Note.B);
//	}

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

	//	@Test
//	void testToString() {
//		final Note note = Note.get(28, Duration.quarter, noAnnotations);
//		assertEquals("C4", note.name);
//	}
//
	@Test
	void testIsValid() {
		assertTrue(Notes.isValid(5));
		assertTrue(Notes.isValid(28));
		assertTrue(Notes.isValid(56));

		assertFalse(Notes.isValid(4));
		assertFalse(Notes.isValid(57));
	}

	@Test
	void testName() {
		assertEquals("C4", Notes.name(Notes.C4));
		assertEquals("A0", Notes.name(5));
		assertEquals("C8", Notes.name(8 * Notes.BASE));
	}
}
