package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import org.inexas.notable.notation.model.*;

public class DTimeSignature extends Drawable {
	private final Glyph numerator;
	private final Glyph denominator;
	private final double up;

	DTimeSignature(final double originX, final double originY, final Layout layout,
	               final TimeSignature timeSignature) {
		super(originX, originY);

		final Metrics m = layout.m;
		up = m.staffSpaceHeight * -2.0;

		// Time signature...
		final GlyphFactory glyphFactory = m.glyphFactory;
		numerator = glyphFactory.timeSignatures[timeSignature.numerator];
		denominator = glyphFactory.timeSignatures[timeSignature.denominator];
	}

	@Override
	void draw(final GraphicsContext gc) {
		gc.fillText(numerator.c, originX, originY + up);
		gc.fillText(denominator.c, originX, originY);
	}
}
