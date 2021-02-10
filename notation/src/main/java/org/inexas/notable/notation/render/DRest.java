package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class DRest extends DEvent {
	// todo Merge this with DNote
	DRest(
			final double originX,
			final double originY,
			final Layout layout,
			final Event event) {
		super(originX, originY);

		final Metrics m = layout.m;
		final List<Drawable> drawables = new ArrayList<>();

		glyph = m.glyphFactory.getItemGlyph(event);
		drawables.add(new DGlyph(originX, originY, glyph));
		// Dots
		setDrawables(drawables);
	}
}
