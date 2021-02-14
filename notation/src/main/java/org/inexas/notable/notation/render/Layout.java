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
	KeySignature key;
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

		final DPara para = firstPass();
		if(para.phraseCount > 1) {
			secondPass(para);
		}
	}

	/**
	 * The job of the first pass is to assemble a single Para as wide as it
	 * needs to be to contain all Parts, Phrases, Staffs, Measures, Events,
	 * and Annotations.
	 * <p>
	 * Each of the objects that make up the Para are wrapped in Drawable
	 * that will add on: originX, originY, and glyph (simple or compound). The
	 * Drawables also take care of quirks like the C Key signature having
	 * to change depending on what, if any, key came before.
	 * <p>
	 * In this pass, x, y, height, and width are measured ems (1 em is a staff
	 * height). Clicks are measured in clicks of course.
	 */
	private DPara firstPass() {
		final DPara result = new DPara(this);
		for(final Part part : score.parts) {
			final DPart dPart = new DPart(this, part);
			result.add(dPart);

			for(final Phrase phrase : part.phrases) {
				final DPhrase dPhrase = new DPhrase(this, phrase);
				dPart.add(dPhrase);

				for(final Measure measure : phrase.measures) {
					final DMeasure dMeasure = new DMeasure(this, measure);
					dPhrase.add(dMeasure);
				}
			}
		}
		return result;
	}

	// The job of the second pass is to vertically align the events
	// two or more Phrases. After the first pass widths are set to
	// minimum width independently of other Phrases.
	private void secondPass(final DPara para) {
		// Collect all the measures...
		final int measureCount = para.measureCount;
		final int phraseCount = para.phraseCount;
		final DMeasure[][] measureTable = new DMeasure[measureCount][phraseCount];

		// Build a table so we can access the a measure from each phrase
		// easily...
		for(final DPart part : para.parts) {
			int p = 0;
			for(final DPhrase phrase : part.phrases) {
				for(int m = 0; m < measureCount; m++) {
					measureTable[m][p] = phrase.measures.get(m);
				}
			}
			p++;
		}

		// Run through measure by measure...
		for(int m = 0; m < measureCount; m++) {
			final DMeasure[] measures = new DMeasure[phraseCount];

			// Measures start with 0..3 zero-click events before the Notes
			// and rests. Build an index pointing at the first click event
			final int[] indexes = new int[measureCount];
			for(int i = 0; i < measures.length; i++) {
				final DMeasure measure = measures[i];
				final List<DEvent> events = measure.events;
				final int eventCount = events.size();
				for(int j = 0; j < eventCount; j++) {
					if(events.get(j).clicks > 0) {
						indexes[i] = j;
						break;
					}
				}
			}
		}
	}


	private DEvent toDrawable(final Event event) {
		final DEvent result;

		if(event instanceof Note) {
			final Note note = (Note) event;

			final Glyph glyph = m.glyphFactory.getNoteHeadGlyph(note, true).noteHead;
			result = new DNote(
					0.0, note.slot,
					this,
					note,
					glyph);
		} else if(event instanceof Rest) {
			result = new DRest(
					0.0, 4,// Slot fixed on staff middle line
					this,
					event);
		} else {
			throw new ImplementMeException();
		}

		return result;
	}
}
