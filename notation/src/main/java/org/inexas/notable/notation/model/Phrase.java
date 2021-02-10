/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;


/**
 * A piano has two Phrases, one for each hand
 */
public class Phrase extends Element {
	public final Score score;
	public final List<Measure> measures = new ArrayList<>();
	public final String name;
	public final Part part;

	Phrase(final String name, final Part part) {
		this.name = name;
		this.part = part;
		score = part.score;
		final Measure measure = new Measure(score, null);
		measures.add(measure);
	}

	public Measure getOpenMeasure() {
		Measure result;
		result = measures.get(measures.size() - 1);
		if(result.isComplete()) {
			result = new Measure(score, result);
			measures.add(result);
		}
		return result;
	}

	public Measure newMeasure() {
		final Measure result = new Measure(score, measures.get(measures.size() - 1));
		measures.add(result);
		return result;
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.enter(this);
		for(final Measure measure : measures) {
			measure.accept(visitor);
		}
		visitor.exit(this);
	}
}
