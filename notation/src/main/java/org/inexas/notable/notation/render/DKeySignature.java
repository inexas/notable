package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class DKeySignature extends Drawable {

	private final double[] index = {160, 170, 180, 190, 200, 210, 220};

	DKeySignature(final double originX, final double originY, final Layout layout,
	              final KeySignature key) {
		super(originX, originY);
		final Metrics m = layout.m;

		if(key.accidentalCount > 0) {
			final List<Drawable> drawables = new ArrayList<>();
			final int[] accidentals = key.getAccidentals(Clef.treble);

			final GlyphFactory glyphFactory = m.glyphFactory;
			final Glyph accidental = key.isSharp() ?
					glyphFactory.accidentalSharp : glyphFactory.accidentalFlat;
			double cursorX = originX;

			for(int i = 0; i < key.accidentalCount; i++) {
				final int x = accidentals[i] % 7;
				drawables.add(new DEvent(cursorX, index[x], layout, accidental));
				cursorX += accidental.rBearing;
			}

			setDrawables(drawables);
		}
	}
}
