package org.inexas.notable.notation.render;

import org.inexas.notable.notation.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RendererTest {

	@Test
	void render() {
		final Score score = Score.fromString("C C G G | A8* B C A G2 | F4* F E E | D D C2");
		final Renderer renderer = new Renderer(score);
		System.out.println(score.toString());
	}
}