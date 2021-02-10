/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;
import org.inexas.notable.util.*;

import java.util.*;

/**
 * Top level representation of a musical piece that contains a list of partMaps
 */
public class Score extends Element implements Visited {
	public final Map<String, Part> partMap = new LinkedHashMap<>();
	public String title;
	private String subtitle;
	public String composer;
	public String header;
	public Measure anonymousFicMeasure;

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Part part : partMap.values()) {
			part.accept(visitor);
		}
		visitor.exit(this);
	}

	/**
	 * Get a Part with the given name. If the Part does not exist then it will
	 * be created and added ot the Score.
	 *
	 * @param name The requested name of the Part
	 * @return Non-null Part
	 */
	public Part getOrCreatePart(final String name) {
		Part result = partMap.get(name);
		if(result == null) {
			result = new Part(name, this);
			partMap.put(name, result);
		}
		return result;
	}

	public Part getFirstPart() {
		return partMap.values().iterator().next();
	}


	/**
	 * The time line collects the sizes of measures for the duration of the
	 * score. Measure sizes are synchronized across all Phases so, for example
	 * of the piano LH starts with a pickup then all parts: piano RH, voice,
	 * double bass, must also start with a pickup measure of the same length
	 * albeit rest-filled.
	 * <p>
	 * The size for each measure will be greater than 1 so the initialized
	 * value of zero means that the size has not been set.
	 * <p>
	 * Typically it will be the first phrase that sets the values but other
	 * phrases may do so. Values should also be set successively so jumping
	 * indicates that something is wrong.
	 */
	private int[] timeline = new int[64];
	private int cursor = 0;
	private int defaultClicks = TimeSignature.fourFour.getMeasureSize();

	public void accountFor(final Measure measure) {
		final int ordinal = measure.ordinal;
		assert ordinal <= cursor;

		if(ordinal == cursor) {
			// A new measure is born. Check there's room at the inn...
			if(cursor >= timeline.length) {
				final int[] old = timeline;
				timeline = new int[2 * old.length];
				System.arraycopy(old, 0, timeline, 0, old.length);
			}

			// Assume the default
			final int clicks = defaultClicks;

			// Does this measure have a new time signature?
			final TimeSignature timeSignature = measure.getTimeSignature();
			if(timeSignature != null) {
				// A new time signature
				defaultClicks = timeSignature.getMeasureSize();
			}

			// Does this measure have a CPM?
			final int cpm = measure.getCpm();
			if(cpm > 0) {
				timeline[cursor] = cpm;
			}

			timeline[cursor++] = clicks;
		}
	}

	int getDefaultClicks() {
		return defaultClicks;
	}

	//	public void setMeasureSize(final int measureNumber, final int size) {
//		assert size > 0 : "Can't set to zero";
//		assert measureNumber == 0 || timeline[measureNumber - 1] > 0 : "Ordinal issue";
//
//		if(measureNumber >= timeline.length) {
//			final int[] old = timeline;
//			timeline = new int[2 * old.length];
//			System.arraycopy(old, 0, timeline, 0, old.length);
//		}
//		timeline[measureNumber] = size;
//	}
//
	public String getTitle() {
		return title;
	}

	public void setTitle(final Messages messages, final String title) {
		if(this.title != null) {
			messages.warn("Score title already set, ignoring redefinition");
		} else {
			this.title = StringU.stripQuotesTrim(title);
		}
	}

	public void setSubtitle(final Messages messages, final String subtitle) {
		if(this.subtitle != null) {
			messages.warn("Score subtitle already set, ignoring redefinition");
		} else {
			this.subtitle = StringU.stripQuotesTrim(subtitle);
		}
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(final Messages messages, final String composer) {
		if(this.composer != null) {
			messages.warn("Composer already set, ignoring redefinition");
		} else {
			this.composer = StringU.stripQuotesTrim(composer);
		}
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(final Messages messages, final String header) {
		if(this.header != null) {
			messages.warn("Header already set, ignoring redefinition");
		} else {
			this.header = StringU.stripQuotesTrim(header);
		}
	}
}
