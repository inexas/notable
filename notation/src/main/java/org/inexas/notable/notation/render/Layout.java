package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class Layout {
	public enum Style {linear, aesthetic}

	private final Style style;
	private final Metrics m;

	static class Item {
		enum Type {Note, Ghost, Rest}
	}

	private final List<Item> items = new ArrayList<>();


	Layout(final Style style, final double scale) {
		this.style = style;
		m = new Metrics(scale);
	}

	void process(final List<Event> events) {
		if(style == Style.aesthetic) {
			//calculateAestheticOffsets(events);
		} else {
			calculateLinearOffsets(events);
		}
	}

	private void calculateLinearOffsets(final List<Event> events) {
		//final int maxItemsPerLine = m.width / m.getNoteHeadGlyph();
	}
}
