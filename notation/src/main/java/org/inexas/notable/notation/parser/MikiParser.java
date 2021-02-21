/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

import java.util.*;
import java.util.regex.*;

/**
 * This class has the following jobs
 * 1. Building the structure of the score
 * 2. Complete parsing so that all text is parsed into model objects and hand them
 * to the structure
 * 3. Structure wide verification and style check
 */
public class MikiParser extends MusicBaseListener {
	@SuppressWarnings("FieldCanBeLocal")
	private final boolean DEBUG = false;
	public final Messages messages;
	private static final Map<Class<? extends Annotation>, Annotation> mtAnnotationMap = Map.of();
	/**
	 * This is true until we encounter the first part, phrase or event
	 */
	private boolean settingDefaults = true;
	public Score score;
	/**
	 * The current part. May be null.
	 */
	private Part part;
	/**
	 * The current phrase. May be null.
	 */
	private Phrase phrase;
	private boolean inChord;

	private MikiParser(final String string) {
		messages = new Messages(false, string);
	}

	public static MikiParser fromString(final String string) {
		final CharStream cs = CharStreams.fromString(string);
		final MusicLexer musicLexer = new MusicLexer(cs);
		final CommonTokenStream tokens = new CommonTokenStream(musicLexer);
		final MusicParser musicParser = new MusicParser(tokens);
		final MusicParser.ScoreContext tree = musicParser.score();
		final MikiParser parser = new MikiParser(string);
		final ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(parser, tree);
		return parser;
	}

	// S T R U C T U R E . . .

	@Override
	public void enterScore(final MusicParser.ScoreContext ctx) {
		messages.ctx = ctx;
		// Set up an anonymous Part and Phrase
		score = new Score(messages);
		part = score.newPart("");
		phrase = part.newPhrase("");
	}

	@Override
	public void exitScore(final MusicParser.ScoreContext ctx) {
		messages.ctx = ctx;
		score.prune();
		final ScoreCheckVisitor scoreCheckVisitor = new ScoreCheckVisitor(messages);
		score.accept(scoreCheckVisitor);
		// todo Terminate all the Phrases
		// todo Check everything is the same length
//		final BeamStylizer stylizer = new BeamStylizer(measure.size);
//		currentPhrase.events = stylizer.process(currentPhrase.events);
//		final RestStylizer restStylizer = new RestStylizer();
//		currentPhrase.events = restStylizer.process(currentPhrase.events);
	}

	@Override
	public void enterPart(final MusicParser.PartContext ctx) {
		messages.ctx = ctx;
		// Get existing or create a new Part...
		final String name = StringU.stripQuotesTrim(ctx.getStop().getText());
		if(part.name.equals(name)) {
			messages.warn("Part already selected: " + name);
		} else {
			part = score.parts.get(name);
			if(part == null) {
				part = score.newPart(name);
			}
			phrase = null;
		}
		settingDefaults = false;
	}

	@Override
	public void enterPhrase(final MusicParser.PhraseContext ctx) {
		messages.ctx = ctx;
		// Get existing or create a new Phrase...
		final String name = StringU.stripQuotesTrim(ctx.getStop().getText());
		/*
		todo I would have like to be able to select the phrase without having to
		select the part first say when changing from guitar to (piano) RH but
		this doesn't work. We couldn't tell if the user was trying to create
		a new phrase or select an existing phrase in a different part.
		 */
		phrase = part.phrases.get(name);
		if(phrase == null) {
			phrase = part.newPhrase(name);
		}
		settingDefaults = false;
	}

	@Override
	public void enterBarline(final MusicParser.BarlineContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Barline barline = Barline.get(text);
		phrase.handle(barline);
	}

	// S C O R E   D E T A I L S . . .

	@Override
	public void enterHeader(final MusicParser.HeaderContext ctx) {
		messages.ctx = ctx;
		score.setHeader(ctx.getStop().getText());
	}

	@Override
	public void enterTitle(final MusicParser.TitleContext ctx) {
		messages.ctx = ctx;
		score.setTitle(ctx.getStop().getText());
	}

	@Override
	public void enterSubtitle(final MusicParser.SubtitleContext ctx) {
		messages.ctx = ctx;
		score.setSubtitle(ctx.getStop().getText());
	}

	@Override
	public void enterComposer(final MusicParser.ComposerContext ctx) {
		messages.ctx = ctx;
		score.setComposer(ctx.getStop().getText());
	}

	@Override
	public void enterCopyright(final MusicParser.CopyrightContext ctx) {
		messages.ctx = ctx;
		score.setCopyright(ctx.getStop().getText());
	}

	@Override
	public void enterTempo(final MusicParser.TempoContext ctx) {
		messages.ctx = ctx;
		final Tempo tempo;
		if(ctx.getChildCount() == 2) {
			final String text = ctx.getStop().getText();
			final String noQuotes = text.charAt(0) == '"' ? StringU.stripQuotes(text) : text;
			tempo = Tempo.getTempo(noQuotes);
		} else {
			// tempo 1/4 = 120
			// 0     1  2 3 4
			assert ctx.getChildCount() == 5;
			final int denominator = Integer.parseInt(ctx.getChild(2).getText());
			final Duration duration = Duration.getByDenominator(denominator);
			final int bpm = Integer.parseInt(ctx.getChild(4).getText());
			tempo = Tempo.getTempo(duration, bpm);
		}
		// todo this should annotate a time signature?
		phrase.annotate(tempo);
	}

	// M O D I F I E R S . . .

	@Override
	public void enterClef(final MusicParser.ClefContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Clef clef = Clef.valueOf(text);
		if(settingDefaults) {
			score.setDefaultClef(clef);
		} else {
			phrase.handle(clef);
		}
	}

	@Override
	public void enterKey(final MusicParser.KeyContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final KeySignature keySignature = KeySignature.parseKeySignature(text);
		if(settingDefaults) {
			score.setDefaultKeySignature(keySignature);
		} else {
			phrase.handle(keySignature);
		}
	}

	@Override
	public void enterTime(final MusicParser.TimeContext ctx) {
		messages.ctx = ctx;
		// time: TIME ( FRACTION | COMMON | CUT ) ;
		final int count = ctx.getChildCount();
		final TimeSignature timeSignature;
		if(count == 2) {
			timeSignature = new TimeSignature(new Fraction(ctx.getChild(1).getText()));
		} else {
			timeSignature = ctx.getStop().getType() == MusicParser.CUT ?
					TimeSignature.CUT : TimeSignature.COMMON;
		}
		if(settingDefaults) {
			score.setDefaultTimeSignature(timeSignature);
		} else {
			phrase.handle(timeSignature);
		}
	}

	@Override
	public void enterCpm(final MusicParser.CpmContext ctx) {
		messages.ctx = ctx;
		// e.g. cpm 15 ;
		final int count = Integer.parseInt(ctx.getChild(1).getText());
		final Cpm cpm = new Cpm(messages, count);
		phrase.handle(cpm);
	}

	// E V E N T S . . .

	@Override
	public void enterEvent(final MusicParser.EventContext ctx) {
		settingDefaults = false;
	}

	@Override
	public void enterNote(final MusicParser.NoteContext ctx) {
		messages.ctx = ctx;
		final String input = ctx.stop.getText();
		// Split up the Event into its parts..
		final Matcher matcher = Notes.notePattern.matcher(input);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch: '" + input + '\'');
		}

		// Group 1: Tonic...
		final String tonic = matcher.group(1);

		// Group 2: Duration...
		final Duration duration;
		final String group2 = matcher.group(2);
		if(group2 == null) {
			duration = phrase.duration;
		} else {
			if(inChord) {
				messages.error("Durations are not permitted in chord groups: " + group2);
				duration = phrase.duration;
			} else {
				duration = Duration.getByMiki(group2);
				if(duration.setDefault) {
					phrase.duration = duration;
				}
			}
		}

		// Group 2: Accidental...
		final String group3 = matcher.group(3);
		if(group3 != null) {
			switch(group3.charAt(0)) {
				case 'b' -> phrase.annotate(Accidental.flat);
				case 'n' -> phrase.annotate(Accidental.natural);
				case '#' -> phrase.annotate(Accidental.sharp);
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 4: Articulation...
		final String group4 = matcher.group(4);
		if(group4 != null) {
			if(inChord) {
				messages.error("Articulations are not permitted in chord groups: " + group4);
			} else {
				final Articulation articulation = Articulation.get(group4);
				phrase.annotate(articulation);
			}
		}

		final char c = tonic.charAt(0);
		final Map<Class<? extends Annotation>, Annotation> annotations;
		if(phrase.annotationMap.isEmpty()) {
			annotations = mtAnnotationMap;
		} else {
			annotations = phrase.annotationMap;
			phrase.annotationMap = new HashMap<>();
		}

		final Event event;
		if(c == 'R') {
			if(inChord) {
				messages.error("Rests are not permitted in chord groups: " + group4);
				event = null;
			} else {
				event = new Rest(duration, annotations);
			}
		} else {
			final int number = phrase.next(tonic);
			event = new Note(number, duration, false, annotations);
			phrase.lastNote = event.slot;
		}

		if(event != null) {
			phrase.handle(event);
		}
	}

	@Override
	public void enterOctave(final MusicParser.OctaveContext ctx) {
		messages.ctx = ctx;
		// E.g. o[1-8] or + or -
		final String text = ctx.getStop().getText();
		if(ctx.children.size() == 1) {
			// Relative octaves
			// We have ('+'+ | '-'+), may also be "+ + +"
			final int change = (text.charAt(0) == '+' ? 1 : -1) * text.length();
			phrase.setRelativeOctave(change);
		} else {
			// Absolute octave
			phrase.setAbsoluteOctave(Integer.parseInt(text));
		}
	}

	@Override
	public void enterChord(final MusicParser.ChordContext ctx) {
		phrase.startChord();
		inChord = true;
	}

	@Override
	public void exitChord(final MusicParser.ChordContext ctx) {
		messages.ctx = ctx;
		phrase.endChord(ctx.stop.getText());
		inChord = false;
	}

	@Override
	public void enterNamedChord(final MusicParser.NamedChordContext ctx) {
		messages.ctx = ctx;
		phrase.addNamedChord(ctx.getStop().getText());
	}

	@Override
	public void enterTuplet(final MusicParser.TupletContext ctx) {
		messages.ctx = ctx;
		phrase.startTuplet();
	}

	@Override
	public void exitTuplet(final MusicParser.TupletContext ctx) {
		messages.ctx = ctx;
		phrase.endTuplet(ctx.getStop().getText());
	}

	// A N N O T A T I O N S . . .

	@Override
	public void enterLine(final MusicParser.LineContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final LineParser parser = new LineParser(text);
		phrase.annotate(parser.line);
	}

	@Override
	public void enterDynamic(final MusicParser.DynamicContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Dynamic dynamic = Dynamic.getDynamic(text);
		phrase.annotate(dynamic);
	}

	@Override
	public void enterText(final MusicParser.TextContext ctx) {
		messages.ctx = ctx;
		final String text = StringU.stripQuotes(ctx.getStop().getText());
		final TextAnnotation textAnnotation = new TextAnnotation(text);
		phrase.annotate(textAnnotation);
	}

	@Override
	public void enterFingering(final MusicParser.FingeringContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Fingering fingering = new Fingering(text.substring(1));
		phrase.annotate(fingering);
	}

	@Override
	public void enterBind(final MusicParser.BindContext ctx) {
		messages.ctx = ctx;
		// todo There's a stack of work to do here, different types of bind,
		// how far they can extend, ...
		final Beam beam = Beam.beams[ctx.getChildCount() - 2];
		phrase.annotate(beam);
	}

	// D E B U G G I N G . . .

	@Override
	public void enterEveryRule(final ParserRuleContext ctx) {
		messages.ctx = ctx;
		if(DEBUG) {
			String name = ctx.getClass().getSimpleName();
			name = name.substring(0, name.length() - 7);
			if(!"Command".contains(name)) {
				System.out.println(name);
			}
		}
	}

	// P A R S I N G   E R R O R   H A N D L I N G . . .

	@Override
	public String toString() {
		return score.toString();
	}
}
