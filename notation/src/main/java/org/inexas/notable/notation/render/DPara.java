package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class DPara extends Drawable {

	DPara(final Layout layout) {
		final List<Drawable> drawables = new ArrayList<>();

		final LinkedHashMap<String, Part> partMap = layout.score.partMap;

		final Metrics m = layout.m;

		final List<Double> barlines = new ArrayList<>();
		barlines.add(150.0);
		barlines.add(250.0);
		barlines.add(350.0);
		barlines.add(m.paperWidth - m.sideMargin);
		drawables.add(new DStaff(m.sideMargin, 200, layout, barlines, "R.H."));
		drawables.add(new DStaff(m.sideMargin, 300, layout, barlines, "L.H."));

		for(final Part part : partMap.values()) {
			for(final Phrase phrase : part.phraseMap.values()) {

			}
		}

		setDrawables(drawables);
	}
}
