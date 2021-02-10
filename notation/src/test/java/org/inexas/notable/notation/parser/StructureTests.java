package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StructureTests {

	@Test
	void testAnonymous() {
		final Score score = MikiParser.fromString("A").score;
		final Map<String, Part> map = score.partMap;
		assertEquals(1, map.size());
	}
}
