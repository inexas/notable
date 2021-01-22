/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

public class Tempo extends Element implements Annotation {
	private final static Tempo Larghissimo = new Tempo(
			"Larghissimo", "Very, very slow (24 tempo and under)", 22);
	private final static Tempo Adagissimo = new Tempo(
			"Adagissimo", "Very slowly", 24);
	private final static Tempo Grave = new Tempo(
			"Grave", "Very slow (25–45 tempo)", 34);
	private final static Tempo Largo = new Tempo(
			"Largo", "Broadly (40–60 tempo)", 50);
	private final static Tempo Lento = new Tempo(
			"Lento", "Slowly (45–60 tempo)", 52);
	private final static Tempo Larghetto = new Tempo(
			"Larghetto", "Rather broadly (60–66 tempo)", 63);
	private final static Tempo Adagio = new Tempo(
			"Adagio", "Slowly with great expression[8] (66–76 tempo)", 72);
	private final static Tempo Adagietto = new Tempo(
			"Adagietto", "Slower than andante (72–76 tempo) or slightly faster than adagio " +
			"(70–80 tempo)", 75);
	private final static Tempo Andante = new Tempo(
			"Andante", "At a walking pace (76–108 tempo)", 92);
	private final static Tempo Andantino = new Tempo(
			"Andantino", "Slightly faster than andante (although, in some cases, it can be taken to" +
			" mean slightly slower than andante) (80–108 tempo)", 94);
	private final static Tempo Marcia_moderato = new Tempo(
			"Marcia moderato", "Moderately, in the manner of a march[9][10] (83–85 tempo)", 84);
	private final static Tempo Andante_moderato = new Tempo(
			"Andante moderato", "Between andante and moderato (thus the name) (92–112 tempo)", 102);
	private final static Tempo Moderato = new Tempo(
			"Moderato", "At a moderate speed (108–120 tempo)", 114);
	private final static Tempo Allegretto = new Tempo(
			"Allegretto", "By the mid 19th century, moderately fast (112–120 tempo)", 115);
	private final static Tempo Allegro = new Tempo(
			"Allegro moderato", "Close to, but not quite allegro (116–120 tempo)", 118);
	private final static Tempo Allegro_moderato = new Tempo(
			"Allegro", "Fast, quickly, and bright (120–156 tempo) (molto allegro is slightly" +
			" faster than allegro, but always in its range)", 138);
	private final static Tempo Vivace = new Tempo(
			"Vivace", "lively and fast (156–176 tempo)", 166);
	private final static Tempo Vivacissimo = new Tempo(
			"Vivacissimo", "Very fast and lively (172–176 tempo)", 174);
	private final static Tempo Allegrissimo = new Tempo(
			"Allegrissimo", "Very fast (172–176 tempo)", 174);
	private final static Tempo Allegro_vivace = new Tempo(
			"Allegro vivace", "Very fast (172–176 tempo)", 174);
	private final static Tempo Presto = new Tempo(
			"Presto", "Very, very fast (168–200 tempo)", 184);
	private final static Tempo Prestissimo = new Tempo(
			"Prestissimo", "Even faster than presto (200 tempo and over)", 200);
	public final static Tempo DEFAULT = Andante;
	private final static Map<String, Tempo> lookup = new HashMap<>();

	// Todo See https://en.wikipedia.org/wiki/Tempo Additional terms
	static {
		register(Larghissimo);
		register(Larghissimo);
		register(Adagissimo);
		register(Grave);
		register(Largo);
		register(Lento);
		register(Larghetto);
		register(Adagio);
		register(Adagietto);
		register(Andante);
		register(Andantino);
		register(Marcia_moderato);
		register(Andante_moderato);
		register(Moderato);
		register(Allegretto);
		register(Allegro_moderato);
		register(Allegro);
		register(Vivace);
		register(Vivacissimo);
		register(Allegrissimo);
		register(Allegro_vivace);
		register(Presto);
		register(Prestissimo);
	}

	public final String name;
	private final String description;
	public final Duration duration;
	public final int bpm;

	private Tempo(final String name, final String description, final int bpm) {
		this.name = name;
		this.description = description;
		duration = Duration.quarter;
		this.bpm = bpm;
	}

	private Tempo(final Duration duration, final int bpm) {
		this.duration = duration;
		this.bpm = bpm;
		name = null;
		description = null;
	}

	public static Tempo getTempo(final String name) {
		Tempo result = lookup.get(name);
		if(result == null) {
			result = new Tempo(name, name, 0);
		}
		return result;
	}

	public static Tempo getTempo(final Duration duration, final int bpm) {
		return new Tempo(duration, bpm);
	}

	private static void register(final Tempo tempo) {
		lookup.put(tempo.name, tempo);
	}

	@Override
	public void accept(final Visitor visitor) {
		visitor.visit(this);
	}
}
