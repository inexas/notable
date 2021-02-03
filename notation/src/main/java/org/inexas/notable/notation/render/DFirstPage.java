package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

class DFirstPage extends DPage {
	DPart[] parts;

	DFirstPage(final Layout layout) {
		final Score score = layout.score;
		parts = new DPart[score.partMap.size()];
		int index = 0;
		final boolean singleton = score.partMap.values().size() == 1;
		for(final Part part : score.partMap.values()) {
			final DPart dPart = new DPart(layout, part, singleton);
			parts[index++] = dPart;
		}
	}

	@Override
	void draw() {
		throw new ImplementMeException();
	}
}
