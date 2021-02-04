package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class DKeySignature extends Drawable {

	private final double[] index = {60, 70, 80, 90, 100, 110, 120};

	DKeySignature(final double originX, final double originY, final Layout layout,
	              final KeySignature key) {
		super(originX, originY);

		if(key.accidentalCount > 0) {
			final List<Drawable> drawables = new ArrayList<>();
			final int[] accidentals = key.getAccidentals(key);
			final Glyph accidental = key.isSharp() ? layout.m.accidentalSharp : layout.m.accidentalFlat;
			double cursorX = originX;

			for(int i = 0; i < key.accidentalCount; i++) {
				drawables.add(new DEvent(cursorX, index[accidentals[i]], accidental, layout));
				cursorX += accidental.rBearing;
			}

			setDrawables(drawables);
		}
	}
}
