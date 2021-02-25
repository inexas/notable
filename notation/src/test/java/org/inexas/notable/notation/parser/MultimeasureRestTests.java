package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MultimeasureRestTests extends ParserTestAbc {
	private static MultimeasureRest construct(final int measureCount) {
		return new MultimeasureRest(Notes.noAnnotations, measureCount);
	}

	@Test
	void construction() {
		final MultimeasureRest mmr;

		mmr = new MultimeasureRest(Notes.noAnnotations, 2);
		assertEquals(-1, mmr.slot);
		//todo Not sure if this is the right approach, duration should probably
		// be in clicks equal to current measure size
		assertEquals(Duration.whole, mmr.duration);
		assertEquals(0, mmr.annotations.size());
		assertEquals("M", mmr.name);
		assertEquals(2, mmr.measureCount);

		assertThrows(AssertionError.class, () -> construct(0));
		assertThrows(AssertionError.class, () -> construct(100));
	}

	@Test
	void parsing() {
		assertEquals("{r 2} |||\n", toMiki("{r 2} |||"));
		assertEquals("{r 3} |||\n", toMiki("{rest 3} |||"));
	}

	@Test
	void fill() {
		assertEquals("""
						part "P"

						phrase "a"
						{r 2} | A A A A |||

						phrase "b"
						B B B B | B B B B | B B B B |||
												""",
				toMiki("""
						part "P" phrase "a"
						{r 2} | AAAA |||
						phrase "b"
						BBBB|BBBB|BBBB|||
						"""));
	}
}
