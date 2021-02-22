package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicsTests extends ParserTestAbc {

	@Test
	void enums() {
		Assertions.assertEquals("fff", Dynamic.fff.name);
		assertEquals("ff", Dynamic.ff.name);
		assertEquals("f", Dynamic.f.name);
		assertEquals("mf", Dynamic.mf.name);
		assertEquals("mp", Dynamic.mp.name);
		assertEquals("p", Dynamic.p.name);
		assertEquals("pp", Dynamic.pp.name);
		assertEquals("ppp", Dynamic.ppp.name);
	}

	@Test
	void inSitu() {
		assertEquals("fff C R R R |||\n", toMiki("fff C RRR|||"));
	}
}
