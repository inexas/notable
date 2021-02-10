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

public class MikiParser extends MusicBaseListener {
	@SuppressWarnings("FieldCanBeLocal")
	private final boolean DEBUG = true;
	private final static Pattern notePattern = Pattern.compile("" +
			"([A-GRX])" +       // Tonic, including rest & ghost
			"([0-9]+.*\\*?)?" + // Duration
			"([b#n])?" +        // Accidental
			"([._!fg]+)?");     // Articulation
	private final static Pattern noteGroupEndPattern = Pattern.compile("" +
			"]" +               // Closing ]
			"([._!fg]+)?" +     // Articulation
			"([0-9]+.*\\*?)?"); // Duration
	// To collect Annotations for the current or next Event
	private Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
	private static final Map<Class<? extends Annotation>, Annotation> mtAnnotationMap = Map.of();
	/**
	 * The current part. May be null.
	 */
	private Part part;
	private Phrase phrase;
	private Measure measure;
	/**
	 * Set by an o8 command
	 */
	private int absoluteOctave = -1;
	private int relativeOctave;
	private int lastNote;
	/**
	 * The number of clicks counted so far in the current measure
	 */
	public Score score;
	/**
	 * This is used to save the state of the duration when a Note group is being
	 * processed. For example, in the miki "C4* [t C E G]8 A" the duration is defaulted
	 * to 4 then in the Note group it's set to eighths but without a default '*'
	 * so the trailing A will have a duration of 4. In this case durationStack
	 * will save the 'quarter' duration set with "C4*"
	 */
	private Duration saveDuration;
	private Venue venue;
	private Venue saveVenue;
	private Duration duration;

	private void push(final Venue venue) {
		assert saveVenue == null;
		saveVenue = this.venue;
		this.venue = venue;
		saveDuration = duration;
	}

	private void pop() {
		assert saveVenue != null;
		venue = saveVenue;
		saveDuration = null;
		duration = saveDuration;
	}

	final Messages messages;

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
		score = new Score();
		part = score.getOrCreatePart("");
		phrase = part.getOrCreatePhrase("");
		score.anonymousFicMeasure = measure = phrase.getOpenMeasure();
		venue = measure;
	}

	@Override
	public void exitScore(final MusicParser.ScoreContext ctx) {
		messages.ctx = ctx;
		// todo Terminate all the Phrases
		// todo Pad to end?
		// todo Check everything is the same length
		// fixme
//		final BeamStylizer stylizer = new BeamStylizer(measure.size);
//		currentPhrase.events = stylizer.process(currentPhrase.events);
//		final RestStylizer restStylizer = new RestStylizer(measure.size);
//		currentPhrase.events = restStylizer.process(currentPhrase.events);
	}

	@Override
	public void enterPart(final MusicParser.PartContext ctx) {
		messages.ctx = ctx;
		// Get existing or create a new Part
		final String name = StringU.stripQuotesTrim(ctx.getStop().getText());
		final Part part = score.getOrCreatePart(name);
		if(this.part != part) {
			// The Part has indeed changed so we don't know which Phrase
			phrase = null;
			measure = null;
			venue = null;
			assert annotationMap.isEmpty() : annotationMap.toString();
		}
	}

	@Override
	public void enterPhrase(final MusicParser.PhraseContext ctx) {
		messages.ctx = ctx;
		final String name = StringU.stripQuotesTrim(ctx.getStop().getText());
		final Phrase phrase = part.getOrCreatePhrase(name);
		if(this.phrase != phrase) {
			// This is indeed a change
			this.phrase = phrase;
			measure = phrase.getOpenMeasure();
			venue = measure;
			assert annotationMap.isEmpty() : annotationMap.toString();
		}
	}

	@Override
	public void enterBarline(final MusicParser.BarlineContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Barline barline = Barline.getBarline(text);

		final int clicksSoFar = measure.clicksSoFar;
		if(clicksSoFar > 0) {   // Ignore barlines at beginning of measure
			if(!measure.isComplete()) {
				error("Barline before end of measure");
			} else {
				score.accountFor(measure);
				measure = phrase.newMeasure();
			}
		}
	}

	// S C O R E   D E T A I L S . . .

	@Override
	public void enterHeader(final MusicParser.HeaderContext ctx) {
		messages.ctx = ctx;
		score.setHeader(messages, ctx.getStop().getText());
	}

	@Override
	public void enterTitle(final MusicParser.TitleContext ctx) {
		messages.ctx = ctx;
		score.setTitle(messages, ctx.getStop().getText());
	}

	@Override
	public void enterSub(final MusicParser.SubContext ctx) {
		messages.ctx = ctx;
		score.setSubtitle(messages, ctx.getStop().getText());
	}

	@Override
	public void enterComposer(final MusicParser.ComposerContext ctx) {
		messages.ctx = ctx;
		score.setComposer(messages, ctx.getStop().getText());
	}

	// M O D I F I E R S . . .

	@Override
	public void enterClef(final MusicParser.ClefContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		measure.setClef(messages, new Clef(text));
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
		annotate(tempo);
	}

	@Override
	public void enterKey(final MusicParser.KeyContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final KeySignature key = KeySignature.parseKeySignature(text);
		measure.setKeySignature(messages, key);
	}

	@Override
	public void enterTime(final MusicParser.TimeContext ctx) {
		messages.ctx = ctx;
		// time: TIME ( COUNT SLASH COUNT | COMMON | CUT ) ;
		final int count = ctx.getChildCount();
		final TimeSignature timeSignature;
		if(count == 4) {
			timeSignature = new TimeSignature(
					Integer.parseInt(ctx.getChild(1).getText()),
					Integer.parseInt(ctx.getChild(3).getText()));
		} else {
			timeSignature = ctx.getStop().getType() == MusicParser.CUT ?
					TimeSignature.CUT : TimeSignature.COMMON;
		}
		measure.setTimeSignature(messages, timeSignature);
	}

	@Override
	public void enterCpm(final MusicParser.CpmContext ctx) {
		messages.ctx = ctx;
		// e.g. cpm 15 ;
		final int cpm = Integer.parseInt(ctx.getChild(1).getText());
		if(cpm < 1) {
			error("CPM out of range: " + cpm);
		}
		measure.setCpm(messages, cpm);
	}

	// E V E N T S . . .

	//	@Override
//	public void enterEvent(final MusicParser.EventContext ctx) {
//		messages.ctx = ctx;
//		// Leading barline...
//		final boolean barAnnotated = annotationMap.containsKey(Barline.class);
//		if(measure.clicksSoFar == 0) {
//			// We're at the start of a new measure, make sure it's annotated...
//			if(!barAnnotated) {
//				annotate(Barline.bar);
//			}
//		} else if(barAnnotated) {
//			// todo Removing a repeat bar may cause other issues
//			annotationMap.remove(Barline.class);
//			warn("Bar annotation not at start of measure; removed.");
//		}
//	}
//
//	@Override
//	public void exitEvent(final MusicParser.EventContext ctx) {
//		messages.ctx = ctx;
//		annotationMap.clear();
//	}
//
	@Override
	public void enterOctave(final MusicParser.OctaveContext ctx) {
		messages.ctx = ctx;
		// E.g. o[1-8] or + or -
		final String text = ctx.getStop().getText();
		if(ctx.children.size() == 1) {
			// Relative octaves
			// We have ('+'+ | '-'+), may also be "+ + +"
			final int direction = text.charAt(0) == '+' ? 1 : -1;
			if(direction > 0) {
				if(absoluteOctave >= 0 || relativeOctave < 0) {
					warn("Possible confusion over octaves?");
				}
				relativeOctave += text.length();
			} else { // direction < 0;
				if(absoluteOctave >= 0 || relativeOctave > 0) {
					warn("Possible confusion over octaves?");
				}
				relativeOctave -= text.length();
			}
		} else {
			// Absolute octave
			absoluteOctave = Integer.parseInt(text);
			if(relativeOctave != 0) {
				warn("Possible confusion over octaves?");
				relativeOctave = 0;
			}
		}
	}

	@Override
	public void enterChord(final MusicParser.ChordContext ctx) {
		push(new Chord(duration));
	}

	@Override
	public void exitChord(final MusicParser.ChordContext ctx) {
		messages.ctx = ctx;

		final Chord chord = (Chord) venue;
		final String closeText = ctx.stop.getText();
		final Matcher matcher = noteGroupEndPattern.matcher(closeText);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 2: Articulation...
		final String group1 = matcher.group(1);
		if(group1 != null) {
			final Articulation articulation = Articulation.getByMiki(group1);
			if(annotationMap.put(articulation.getClass(), articulation) != null) {
				warn("Annotation overwrites previous value: " + articulation);
			}
		}

		// Group 2: Duration...
		final String group2 = matcher.group(2);
		final Duration duration;
		if(group2 == null) {
			duration = this.duration;
		} else {
			duration = Duration.getByMiki(group2);
			if(duration.setDefault) {
				this.duration = duration;
			}
		}
		chord.duration = duration;

		venue.add(chord);

		pop();
	}

	@Override
	public void enterNamedChord(final MusicParser.NamedChordContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final NamedChord namedChord = NamedChord.parse(text, duration, annotationMap);
		venue.add(namedChord);
	}

	@Override
	public void enterTuplet(final MusicParser.TupletContext ctx) {
		messages.ctx = ctx;
		push(new Tuplet(duration));
	}

	@Override
	public void exitTuplet(final MusicParser.TupletContext ctx) {
		messages.ctx = ctx;

		final Tuplet tuplet = (Tuplet) venue;
		final String text = ctx.getStop().getText();
		if(text.length() > 1) {
			final Matcher matcher = noteGroupEndPattern.matcher(text);
			if(!matcher.matches()) {
				throw new RuntimeException("Recognizer/event parser mismatch");
			}

			if(matcher.group(1) != null) {
				warn("Tuplet articulation not supported, ignored: " + text);
			}

			// Group 2: Duration...
			final String group2 = matcher.group(2);
			final Duration duration;
			if(group2 != null) {
				duration = Duration.getByMiki(group2);
				if(duration.setDefault) {
					saveDuration = duration;
				}
			} else {
				// No duration but an erroneous articulation
				duration = this.duration;
			}
			tuplet.duration = duration;
		}
		final int total = measure.clicksSoFar + duration.clicks;
		if(total > measure.size) {
			warn("Tuplet duration to long, reduced");
			final Duration[] durations = Duration.getByClicks(total - measure.size);
			duration = durations[durations.length - 1];
		}
		pop();
		venue.add(tuplet);
	}

	@Override
	public void enterNote(final MusicParser.NoteContext ctx) {
		messages.ctx = ctx;
		buildEvent(ctx);
	}

	private void buildEvent(final ParserRuleContext ctx) {
		final Event event;

		final String input = ctx.stop.getText();
		// Split up the Event into its parts..
		final Matcher matcher = notePattern.matcher(input);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch: '" + input + '\'');
		}

		// Group 1: Tonic...
		final String tonic = matcher.group(1);

		// Group 2: Duration...
		final Duration duration;
		final String group2 = matcher.group(2);
		if(group2 == null) {
			duration = this.duration;
		} else {
			duration = Duration.getByMiki(group2);
			if(duration.setDefault) {
				this.duration = duration;
			}
		}

		// Group 2: Accidental...
		final String group3 = matcher.group(3);
		if(group3 != null) {
			switch(group3.charAt(0)) {
				case 'b' -> annotate(Accidental.flat);
				case 'n' -> annotate(Accidental.natural);
				case '#' -> annotate(Accidental.sharp);
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 4: Articulation...
		final String group5 = matcher.group(4);
		if(group5 != null) {
			final Articulation articulation = Articulation.getByMiki(group5);
			annotate(articulation);
		}

		final char c = tonic.charAt(0);
		final Map<Class<? extends Annotation>, Annotation> annotations;
		if(annotationMap.isEmpty()) {
			annotations = mtAnnotationMap;
		} else {
			annotations = annotationMap;
			annotationMap = new HashMap<>();
		}
		if(c == 'R') {
			event = new Rest(duration, annotations);
		} else if(c == 'X') {
			event = new Ghost(duration, annotations);
		} else {
			final int number = Note.next(
					lastNote,
					absoluteOctave, relativeOctave,
					tonic);
			event = new Note(number, duration, false, annotations);
		}

		if(event instanceof Note) {
			lastNote = event.slot;
		}

		venue.add(event);

		// Ready for next
		relativeOctave = 0;
		absoluteOctave = -1;
	}

	private void handle(final Event event) {
		venue.add(event);
	}

	// A N N O T A T I O N S . . .

	@Override
	public void enterLine(final MusicParser.LineContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final LineParser parser = new LineParser(text);
		annotate(parser.line);
	}

	@Override
	public void enterDynamic(final MusicParser.DynamicContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Dynamic dynamic = Dynamic.getDynamic(text);
		annotate(dynamic);
	}

	@Override
	public void enterText(final MusicParser.TextContext ctx) {
		messages.ctx = ctx;
		final String text = StringU.stripQuotes(ctx.getStop().getText());
		final TextAnnotation textAnnotation = new TextAnnotation(text);
		annotate(textAnnotation);
	}

	@Override
	public void enterFingering(final MusicParser.FingeringContext ctx) {
		messages.ctx = ctx;
		final String text = ctx.getStop().getText();
		final Fingering fingering = new Fingering(text.substring(1));
		annotate(fingering);
	}

	@Override
	public void enterBind(final MusicParser.BindContext ctx) {
		messages.ctx = ctx;
		// todo There's a stack of work to do here, different types of bind,
		// how far they can extend, ...
		final Beam beam = Beam.beams[ctx.getChildCount() - 2];
		annotate(beam);
	}

	private void annotate(final Annotation annotation) {
		if(annotationMap.put(annotation.getClass(), annotation) != null) {
			warn("Annotation overwrites previous value: " + annotation);
		}
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

	private void error(final String message) {
		messages.error(message);
	}

	private void warn(final String message) {
		messages.warn(message);
	}

	@Override
	public String toString() {
		return score.toString();
	}
}
