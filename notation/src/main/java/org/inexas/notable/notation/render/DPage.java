package org.inexas.notable.notation.render;

import javafx.scene.canvas.*;
import org.inexas.notable.util.*;

import java.util.*;

class DPage extends Drawable {
	final List<DPart> parts = new ArrayList<>();

	DPage(final Layout layout) {
	}

	@Override
	void draw(final GraphicsContext gc) {
		throw new ImplementMeException();
	}
}
