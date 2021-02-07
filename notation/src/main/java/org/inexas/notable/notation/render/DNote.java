package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;

import java.util.*;

public class DNote extends Drawable {

	DNote(final double originX, final double originY, final Layout layout, final Note note) {
		super(originX, originY);

		final Metrics m = layout.m;
		final int clicks = note.duration.clicks;
		final List<Drawable> drawables = new ArrayList<>();

		// todo Calc these properly
		final boolean stemUp = note.tonic == 0;

		final GlyphFactory.NoteKit noteKit = m.glyphFactory.getNoteHeadGlyph(note, stemUp);
		final int slot = note.slot;

		// Draw leger lines...
		final double thickness = m.legerLineThickness;
		final double x1 = originX - m.legerLineExtension;
		final double x2 = x1 + m.legerLineLength;
		final double y2 = originX + m.legerLineLength;
		for(int i = layout.staffLow - 2; i >= slot; i -= 2) {
			drawables.add(new DLine(x1, i, x1, i, thickness));
		}
		for(int i = layout.staffHigh + 2; i <= slot; i += 2) {
			drawables.add(new DLine(x1, i, x1, i, thickness));
		}

		// Draw note head...
		drawables.add(new DGlyph(originX, originY, noteKit.noteHead));

		// Stem...
		if(clicks < 32) {
			final double y1 = originY + (stemUp ? 3.5 : -3.5);
			drawables.add(new DLine(originX, originY, originX, originY, m.stemThickness));
		}
		// Flag?
		if(clicks < 8) {
			drawables.add(new DGlyph(originX, originY, noteKit.flag));
		}

		setDrawables(drawables);
	}
}
