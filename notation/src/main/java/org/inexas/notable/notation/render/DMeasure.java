package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

import java.util.*;

class DMeasure {
	final List<DEvent> events = new ArrayList<>();
	private final Layout layout;
	private final Measure measure;
	private int clickSoFar, maxClicks;
	/**
	 * Keeps a total of how much extra space is needed for clefs,
	 * time signatures, and key signatures
	 */
	private double xCursor;
	private double lastRBearing;

	DMeasure(final Layout layout, final Measure measure) {
		this.layout = layout;
		this.measure = measure;
	}

	void add(final DEvent event) {
		events.add(event);
		clickSoFar += event.clicks;

		// Calculate the width
		final Glyph glyph = event.glyph;
		xCursor += Math.max(lastRBearing, glyph.lBearing);
		event.originX = xCursor;
		xCursor += glyph.width;
		lastRBearing = glyph.rBearing;

		assert clickSoFar <= maxClicks;
	}

	public boolean isComplete() {
		return clickSoFar == maxClicks;
	}

	public double slotsAbove() {
		throw new ImplementMeException();
	}

	public Clef getClef() {
		// Either chain back or initialize every measure
		throw new ImplementMeException();
	}
}
