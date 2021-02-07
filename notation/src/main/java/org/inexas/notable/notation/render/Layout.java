package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

import java.util.*;

public class Layout {
	enum Format {continuous, a4, letter}

	enum Style {linear, aesthetic}

	final Score score;
	final Format format;
	final Style style;
	final String title;
	final String header;
	final String composer;
	Metrics m;
	DClef clef;
	KeySignature key;
	TimeSignature time;
	int staffHigh;
	int staffLow;

	final List<DPage> pages = new ArrayList<>();

	Layout(
			final Score score,
			final Format format,
			final Style style,
			final double scale) {
		this.score = score;
		this.format = format;
		this.style = style;
		m = new Metrics(scale);

		title = StringU.nullOrText(score.title);
		composer = StringU.nullOrText(score.composer);
		header = StringU.nullOrText(score.header);

		firstPass();
	}

	/**
	 * The first pass creates a Page/Part/Phrase hierarchy and a list
	 * of Drawables in the right order. At this point the paper is considered
	 * to be infinitely wide and the events are compacted as tightly as possible.
	 */
	private void firstPass() {
		final DFirstPage firstPage = new DFirstPage(this);
		pages.add(firstPage);
		int partIndex = 0;
		for(final Part part : score.partMap.values()) {
			final DPart dPart = new DPart(this, part, score.partMap.size() == 1);
			firstPage.parts[partIndex++] = dPart;
		}
	}
}
