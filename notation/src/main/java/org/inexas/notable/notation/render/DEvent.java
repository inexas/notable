package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

abstract class DEvent extends Drawable {
	final int clicks;

	DEvent(final Event event) {
		clicks = event.duration.clicks;
	}
}
