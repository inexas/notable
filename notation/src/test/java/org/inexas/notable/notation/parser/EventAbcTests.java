package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventAbcTests {
	private final Map<Class<? extends Annotation>, Annotation> noAnnotations = Map.of();

	@Test
	void testAssertions() {
		AssertionError e = assertThrows(AssertionError.class, () ->
				Note.get(4, null, null));
		assertTrue(e.getMessage().contains("range"));

		e = assertThrows(AssertionError.class, () ->
				Note.get(5, null, null));
		assertTrue(e.getMessage().contains("duration"));

		e = assertThrows(AssertionError.class, () ->
				Note.get(5, Duration.quarter, null));
		assertTrue(e.getMessage().contains("annotations"));

		e = assertThrows(AssertionError.class, () ->
				Note.get(57, Duration.quarter, noAnnotations));
		assertTrue(e.getMessage().contains("range"));
	}
}
