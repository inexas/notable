package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

import java.util.*;

public class DPara extends Drawable {
	final List<DPart> parts = new ArrayList<>();
	int phraseCount;
	int measureCount;

	DPara(final Layout layout) {
		final List<Drawable> drawables = new ArrayList<>();

		final MappedList<Part> parts = layout.score.parts;

		final Metrics m = layout.m;

		final List<Double> barlines = new ArrayList<>();
		barlines.add(150.0);
		barlines.add(250.0);
		barlines.add(350.0);
		barlines.add(m.paperWidth - m.sideMargin);
		drawables.add(new DStaff(m.sideMargin, 200, layout, barlines, "R.H."));
		drawables.add(new DStaff(m.sideMargin, 300, layout, barlines, "L.H."));

		setDrawables(drawables);
	}

	void add(final DPart part) {
		parts.add(part);
		phraseCount += part.phrases.size();
		measureCount = part.phrases.get(0).measures.size();
	}
}
