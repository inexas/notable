package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.notation.parser.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class LayoutTest {
	private Layout toLayout(final String string) {
		final Score score = MikiParser.fromString(string).score;
		return new Layout(score, Layout.Format.a4, Layout.Style.linear, Metrics.M);
	}

	@Test
	void simple() {
		final Layout layout = toLayout("C");
		assertEquals(Layout.Format.a4, layout.format);
		assertEquals(Layout.Style.linear, layout.style);

		assertEquals(1, layout.pages.size());

		final DFirstPage page = (DFirstPage) layout.pages.get(0);
		assertNull(layout.title);
		assertNull(layout.header);
		assertNull(layout.composer);
		assertEquals(1, page.parts.length);

		final DPart part = page.parts[0];
		assertNull(part.name);
		assertEquals(1, part.phrases.length);

		final DPhrase phrase = part.phrases[0];
		assertNull(phrase.name);

		assertEquals(3, phrase.events.length);
		final DSingleEvent event = (DSingleEvent) phrase.events[0];
		assertEquals(8, event.clicks);
	}

	@Test
	void frame() {
		final Layout layout = toLayout("title \" Title\" header \"\" composer \"c\"");
		assertEquals("Title", layout.title);
		assertNull(layout.header);
		assertEquals("c", layout.composer);
	}
}
