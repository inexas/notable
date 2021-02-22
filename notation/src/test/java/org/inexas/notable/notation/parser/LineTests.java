package org.inexas.notable.notation.parser;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LineTests extends ParserTestAbc {
	
	@Test
	void testLines() {
		assertEquals("{b 2} C E G R |||\n", toMiki("{b 2} C E G R|||"));
		assertEquals("{b 2} C E G R |||\n", toMiki("{bind 2} C E G R|||"));
		assertEquals("{c 1.2} C E G R |||\n", toMiki("{cre 1.2} C E G R|||"));
		assertEquals("{d 1.2} C E G R |||\n", toMiki("{decrescendo 1.2} C E G R|||"));
		assertEquals("{o-8 2} C R R2 |||\n", toMiki("{o-8 2} C R R2 |||"));
		assertEquals("{o8 2} C R R2 |||\n", toMiki("{o8 2} C R R2 |||"));
		assertEquals("{p 2.2} C D A R |||\n", toMiki("{pedal 2.2} C D A R|||"));
		assertEquals("{p 2.2} C D A R |||\n", toMiki("{pedal 2.2} C D A R|||"));
		assertEquals("{r 2} C R R2 |||\n", toMiki("{rest 2} C R R2 |||"));
		assertEquals("{v1 2} C R R2 |||\n", toMiki("{volta1 2} C R R2 |||"));
	}
}
