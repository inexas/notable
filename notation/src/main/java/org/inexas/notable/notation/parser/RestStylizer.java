package org.inexas.notable.notation.parser;

import org.inexas.notable.notation.model.*;

import java.util.*;

// todo Allow for pickup measure
// todo Allow for changes of time
// todo Figure out if rests can be annotated

public class RestStylizer {
	private final static int clicksPerQuarter = 8;
	private final static Map<Class<? extends Annotation>, Annotation> mtAnnotations = Map.of();
	private final int clicksPerMeasure;
	private int restClicksSoFar;
	private int measureClicksSoFar;
	private final List<Event> eventSink = new ArrayList<>();
	private int startedAt;

	RestStylizer(final int clicksPerMeasure) {
		this.clicksPerMeasure = clicksPerMeasure;
	}

	List<Event> process(final List<Event> eventSource) {
		for(final Event event : eventSource) {
			final int clicks = event.duration.clicks;

			if(event instanceof Rest) {
				if(restClicksSoFar == 0) {
					startedAt = measureClicksSoFar;
				}
				restClicksSoFar += clicks;
			} else {
				processRestClicks();
				eventSink.add(event);
			}

			measureClicksSoFar += clicks;

			// End of measure...
			if(measureClicksSoFar == clicksPerMeasure) {
				processRestClicks();
				measureClicksSoFar = 0;
			}
		}

		assert restClicksSoFar == 0 : "Score should pad to the end of a measure";

		return eventSink;
	}

	private void processRestClicks() {
		if(restClicksSoFar > 0) {
			// We've seen at least one Rest...

			// Generate Rests up until the closest end of quarter...
			int nowAt = startedAt;
			final int toNextQuarter = (clicksPerMeasure - nowAt) % clicksPerQuarter;
			if(toNextQuarter > 0) {
				final Duration[] durations = Duration.getByClicks(toNextQuarter);
				for(final Duration duration : durations) {
					// todo Are there annotated rests?
					final Rest rest = new Rest(duration, mtAnnotations);
					eventSink.add(rest);
					nowAt += duration.clicks;
				}
			}

			int remaining = restClicksSoFar - toNextQuarter;
			while(remaining > 0) {
				final Duration duration;
				if(nowAt == 0 && remaining == 32) {
					duration = Duration.whole;
				} else if((nowAt == 0 && remaining >= 16) ||
						(nowAt == 16 && remaining == 16)) {
					duration = Duration.half;
				} else if(remaining >= 8) {
					duration = Duration.quarter;
				} else {
					// todo If we support 1/32 this will fail
					duration = Duration.getByClicks(remaining)[0];
				}

				final Rest rest = new Rest(duration, mtAnnotations);
				eventSink.add(rest);

				remaining -= duration.clicks;
				nowAt += duration.clicks;
			}
		}
		restClicksSoFar = 0;
	}
}
