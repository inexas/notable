package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BarlineTests extends ParserTestAbc {

	@Test
	void noEvents() {
		assertEquals("", toMiki(""));
	}

	@Test
	void miki() {
		// More to stop compiler warnings than anything else...
		assertEquals("|", Barline.singleBar.miki);
		assertEquals("||", Barline.doubleBar.miki);
		assertEquals("|:", Barline.beginRepeat.miki);
		assertEquals(":|:", Barline.beginEndRepeat.miki);
		assertEquals(":|", Barline.endRepeat.miki);
		assertEquals("|||", Barline.eos.miki);
		assertEquals("", Barline.next.miki);
	}

	@Test
	void finalBarline() {
		errorExpected("Missing", "CCCC");
		assertEquals("C C C C |||\n", toMiki("CCCC|||"));
		// todo The next two errros should be picked up in the post processing
//		errorExpected("Incorrect", "CCCC|");
//		errorExpected("Incorrect", "CCCC||");
	}
}
