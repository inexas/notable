package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import org.inexas.notable.notation.model.*;

class DFirstPage extends DPage {
	DPart[] parts;
	private DPara para;

	DFirstPage(final Layout layout) {
		final Score score = layout.score;
		parts = new DPart[score.partMap.size()];
		int index = 0;
		final boolean singleton = score.partMap.values().size() == 1;
		for(final Part part : score.partMap.values()) {
			final DPart dPart = new DPart(layout, part, singleton);
			parts[index++] = dPart;
		}
		para = new DPara(layout);
	}

	@Override
	void draw(final GraphicsContext gc) {
		para.draw(gc);
	}
}
