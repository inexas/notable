package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

import java.util.*;

// todo Allow for pickup measure
// todo Allow for changes of time
// todo Figure out if rests can be annotated

/**
 * Annotate notes that can be joined with a beam, e.g. two 1/8ths.
 */
public class BeamStylizer {
	private final static int clicksPerQuarter = 8;
	private final int clicksPerMeasure;
	private int measureClicks;
	private Event first;
	private int count;

	BeamStylizer(final int clicksPerMeasure) {
		this.clicksPerMeasure = clicksPerMeasure;
	}

	List<Event> process(final List<Event> events) {

		for(final Event event : events) {
			final int clicks = event.duration.clicks;

			if(event instanceof Rest) {
				terminate();
			} else {
				final int quarterClicks = measureClicks % clicksPerQuarter;
				final int total = quarterClicks + clicks;
				if(total > clicksPerQuarter) {
					terminate();
				} else if(total == clicksPerQuarter) {
					count++;
					terminate();
				} else if(first == null) {
					first = event;
					count = 1;
				} else {
					count++;
				}
			}

			measureClicks = (measureClicks + clicks) % clicksPerMeasure;
		}
		assert measureClicks == 0 : "Score should pad to the end of a measure";

		return events;
	}

	private void terminate() {
		if(count > 1) {
			final Beam beam = Beam.beams[count];
			first.add(beam);
		}
		first = null;
		count = 0;
	}
}
