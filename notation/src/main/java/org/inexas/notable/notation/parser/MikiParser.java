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
	private final boolean DEBUG = false;
	private final static Pattern keySignaturePattern = Pattern.compile("" +
			"([A-G])" +         // Tonic
			"([b#n])?" +        // Accidental
			"([Mm])?");         // Major/minor
	private final static Pattern notePattern = Pattern.compile("" +
			"([A-GRX])" +       // Tonic, including rest & ghost
			"([b#n])?" +        // Accidental
			"([0-9]+,*\\*?)?" + // Duration
			"([._!fg]+)?");     // Articulation
	private final static Pattern noteGroupEndPattern = Pattern.compile("" +
			"]" +               // Closing ]
			"([0-9]+,*\\*?)?" + // Duration
			"([._!fg]+)?");     // Articulation
	// To collect Annotations for the current or next Event
	private final Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
	private final Map<Class<? extends Annotation>, Annotation> mtAnnotationMap = Map.of();
	private Phrase currentPhrase;
	/**
	 * Set by an o8 command
	 */
	private int absoluteOctave = -1;
	private int relativeOctave;
	private int lastNote = Note.C4;
	private Part currentPart;
	private KeySignature currentKey;
	private boolean settingScoreDefaults = true;
	/**
	 * The number of clicks counted so far in the current measure
	 */
	private int clicksSoFar;
	/**
	 * Number of clicks in a measure
	 */
	private int measureSize;
	@SuppressWarnings("unused")
	private TimeSignature timeSignature;
	private List<Event> events;
	private boolean inNoteGroup;
	private Duration currentDuration;
	/**
	 * This is used to save the state of the duration when a Note group is being
	 * processed. For example, in the miki "C4* [t C E G]8 A" the duration is defaulted
	 * to 4 then in the Note group it's set to eighths but without a default '*'
	 * so the trailing A will have a duration of 4. In this case durationStack
	 * will save the 'quarter' duration set with "C4*"
	 */
	private Duration saveDuration;
	public final Score score = new Score();
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

	@Override
	public void enterScore(final MusicParser.ScoreContext ctx) {
		currentPart = score.getFirstPart();
		currentPhrase = currentPart.getFirstPhrase();
		events = currentPhrase.events;
		// Set the defaults...
		setTimeSignature(TimeSignature.COMMON);
		currentDuration = Duration.quarter;
		score.key = currentKey = KeySignature.C;
	}

	@Override
	public void exitScore(final MusicParser.ScoreContext ctx) {
		padToEnd();
	}

	@Override
	public void enterTitle(final MusicParser.TitleContext ctx) {
		score.title = normalizeString(ctx.getStop().getText());
	}

	@Override
	public void enterComposer(final MusicParser.ComposerContext ctx) {
		score.composer = normalizeString(ctx.getStop().getText());
	}

	@Override
	public void enterHeader(final MusicParser.HeaderContext ctx) {
		score.header = normalizeString(ctx.getStop().getText());
	}

	@Override
	public void enterPart(final MusicParser.PartContext ctx) {
		final String name = ctx.getStop().getText();
		currentPart = score.getPart(StringU.stripQuotes(name));
		settingScoreDefaults = false;
	}

	@Override
	public void enterPhrase(final MusicParser.PhraseContext ctx) {
		String name = ctx.getStop().getText();
		name = StringU.stripQuotes(name);

		final int colon = name.indexOf(':');
		if(colon >= 0) {
			final String partName = name.substring(0, colon);
			currentPart = score.getPart(partName);
			name = name.substring(colon + 1);
		}
		currentPhrase = currentPart.getPhrase(name);
		events = currentPhrase.events;
		settingScoreDefaults = false;
		lastNote = Note.C4;
		clicksSoFar = 0;
	}

	@Override
	public void exitPhrase(final MusicParser.PhraseContext ctx) {
		padToEnd();
		currentPhrase = null;
	}

	@Override
	public void enterStaff(final MusicParser.StaffContext ctx) {
		final String text = ctx.getStop().getText();
		final Staff staff = new Staff(text);
		if(settingScoreDefaults) {
			score.staff = staff;
		} else {
			currentPart.staff = staff;
		}
	}

	@Override
	public void enterTempo(final MusicParser.TempoContext ctx) {
		final Tempo tempo;
		if(ctx.getChildCount() == 2) {
			final String text = ctx.getStop().getText();
			final String noQuotes = text.charAt(0) == '"' ? StringU.stripQuotes(text) : text;
			tempo = Tempo.getTempo(noQuotes);
		} else {
			// tempo 1/ 4 = 120
			// 0     1  2 3 4
			assert ctx.getChildCount() == 5;
			final int denominator = Integer.parseInt(ctx.getChild(2).getText());
			final Duration duration = Duration.getByDenominator(denominator);
			final int bpm = Integer.parseInt(ctx.getChild(4).getText());
			tempo = Tempo.getTempo(duration, bpm);
		}
		annotate(ctx, tempo);
	}

	@Override
	public void enterKey(final MusicParser.KeyContext ctx) {
		final String text = ctx.getStop().getText();
		currentKey = parseKeySignature(text);
		if(settingScoreDefaults) {
			score.key = currentKey;
		} else {
			annotate(ctx, currentKey);
		}
	}

	@Override
	public void enterTime(final MusicParser.TimeContext ctx) {
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
		if(settingScoreDefaults) {
			score.timeSignature = timeSignature;
		} else {
			annotate(ctx, timeSignature);
		}
		clicksSoFar = 0;
		setTimeSignature(timeSignature);
	}

	@Override
	public void enterPickup(final MusicParser.PickupContext ctx) {
		// pickup FRACTION ;
		score.pickupMeasure = new PickupMeasure(ctx.getChild(1).getText());
	}

	@Override
	public void enterBarline(final MusicParser.BarlineContext ctx) {
		final String text = ctx.getStop().getText();
		final Barline barline = Barline.getBarline(text);
		annotate(ctx, barline);
	}

	@Override
	public void enterLine(final MusicParser.LineContext ctx) {
		final String text = ctx.getStop().getText();
		final LineParser parser = new LineParser(text);
		annotate(ctx, parser.line);
	}

	@Override
	public void enterOctave(final MusicParser.OctaveContext ctx) {
		// E.g. o[1-8] or + or -
		final String text = ctx.getStop().getText();
		if(ctx.children.size() == 1) {
			// Relative octaves
			// We have ('+'+ | '-'+), may also be "+ + +"
			final int direction = text.charAt(0) == '+' ? 1 : -1;
			if(direction > 0) {
				if(absoluteOctave >= 0 || relativeOctave < 0) {
					messages.warn(ctx, "Possible confusion over octaves?");
				}
				relativeOctave += text.length();
			} else { // direction < 0;
				if(absoluteOctave >= 0 || relativeOctave > 0) {
					messages.warn(ctx, "Possible confusion over octaves?");
				}
				relativeOctave -= text.length();
			}
		} else {
			// Absolute octave
			absoluteOctave = Integer.parseInt(text);
			if(relativeOctave != 0) {
				messages.warn(ctx, "Possible confusion over octaves?");
				relativeOctave = 0;
			}
		}
	}

	@Override
	public void enterDynamic(final MusicParser.DynamicContext ctx) {
		final String text = ctx.getStop().getText();
		final Dynamic dynamic = Dynamic.getDynamic(text);
		annotate(ctx, dynamic);
	}

	@Override
	public void enterText(final MusicParser.TextContext ctx) {
		final String text = StringU.stripQuotes(ctx.getStop().getText());
		final TextAnnotation textAnnotation = new TextAnnotation(text);
		annotate(ctx, textAnnotation);
	}

	@Override
	public void enterFingering(final MusicParser.FingeringContext ctx) {
		final String text = ctx.getStop().getText();
		final Fingering fingering = new Fingering(text.substring(1));
		annotate(ctx, fingering);
	}

	@Override
	public void enterEvent(final MusicParser.EventContext ctx) {
		// Leading barline...
		final boolean barAnnotated = annotationMap.containsKey(Barline.class);
		if(clicksSoFar == 0) {
			// We're at the start of a new measure, make sure it's annotated...
			if(!barAnnotated) {
				annotate(ctx, Barline.bar);
			}
		} else if(barAnnotated) {
			// todo Removing a repeat bar may cause other issues
			annotationMap.remove(Barline.class);
			messages.warn(ctx, "Bar annotation not at start of measure; removed.");
		}
	}

	@Override
	public void exitEvent(final MusicParser.EventContext ctx) {
		annotationMap.clear();
	}

	@Override
	public void enterNote(final MusicParser.NoteContext ctx) {
		buildEvent(ctx);
	}

	@Override
	public void enterRest(final MusicParser.RestContext ctx) {
		buildEvent(ctx);
	}

	@Override
	public void enterGhost(final MusicParser.GhostContext ctx) {
		buildEvent(ctx);
	}

	@Override
	public void enterChord(final MusicParser.ChordContext ctx) {
		events = new ArrayList<>();
		inNoteGroup = true;
	}

	@Override
	public void exitChord(final MusicParser.ChordContext ctx) {
		final Event event = buildChord(ctx);
		inNoteGroup = false;
		events = currentPhrase.events;
		events.add(event);
	}

	@Override
	public void enterTuplet(final MusicParser.TupletContext ctx) {
		saveDuration = currentDuration;
		events = new ArrayList<>();
		inNoteGroup = true;
	}

	@Override
	public void exitTuplet(final MusicParser.TupletContext ctx) {
		currentDuration = saveDuration;
		final String text = ctx.getStop().getText();
		Duration duration;
		if(text.length() > 1) {
			final Matcher matcher = noteGroupEndPattern.matcher(text);
			if(!matcher.matches()) {
				throw new RuntimeException("Recognizer/event parser mismatch");
			}
			if(matcher.group(2) != null) {
				messages.warn(ctx, "Tuplet articulation not supported, ignored: " + text);
			}

			// Group 1: Duration...
			final String group1 = matcher.group(1);
			if(group1 != null) {
				duration = Duration.getByMiki(group1);
				if(!duration.setDefault) {
					saveDuration = currentDuration;
				}
				currentDuration = duration;
			} else {
				// No duration but an erroneous articulation
				duration = currentDuration;
			}
		} else {
			duration = currentDuration;
		}
		final int total = clicksSoFar + duration.clicks;
		if(total > measureSize) {
			messages.warn(ctx, "Tuplet duration to long, reduced");
			final Duration[] durations = Duration.getByClicks(total - measureSize);
			duration = durations[durations.length - 1];
		}
		final Tuplet tuplet = new Tuplet(duration, events);
		events = currentPhrase.events;
		account(tuplet, ctx);
		events.add(tuplet);
		inNoteGroup = false;
	}

	@Override
	public void enterNamedChord(final MusicParser.NamedChordContext ctx) {
		final String text = ctx.getStop().getText();
		final NamedChord namedChord = NamedChord.parse(text, currentDuration, annotationMap);
		final Event event = account(namedChord, ctx);
		events.add(event);
		if(namedChord.duration.setDefault) {
			currentDuration = namedChord.duration;
		}
	}

	@Override
	public void enterEveryRule(final ParserRuleContext ctx) {
		if(DEBUG) {
			String name = ctx.getClass().getSimpleName();
			name = name.substring(0, name.length() - 7);
			if(!"Command".contains(name)) {
				System.out.println(name);
			}
		}
	}

	@Override
	public String toString() {
		return score.toString();
	}

	private Event buildChord(final MusicParser.ChordContext ctx) {
		Chord result;

		final Duration duration;
		final String closeText = ctx.stop.getText();
		final Matcher matcher = noteGroupEndPattern.matcher(closeText);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 1: Duration...
		final String group1 = matcher.group(1);
		if(group1 == null) {
			duration = currentDuration;
		} else {
			duration = Duration.getByMiki(group1);
			if(duration.setDefault) {
				currentDuration = duration;
			}
		}
		// todo Take care of duration overflow

		// Group 2: Articulation...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			final Articulation articulation = Articulation.getByMiki(group2);
			annotate(ctx, articulation);
		}

		result = new Chord(duration, events, annotationMap);
		result = (Chord) account(result, ctx);

		return result;
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

		// Group 2: Accidental...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			switch(group2.charAt(0)) {
				case 'b' -> annotate(ctx, Accidental.flat);
				case 'n' -> annotate(ctx, Accidental.natural);
				case '#' -> annotate(ctx, Accidental.sharp);
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 3: Duration...
		final Duration duration;
		final String group3 = matcher.group(3);
		if(group3 == null) {
			duration = currentDuration;
		} else {
			duration = Duration.getByMiki(group3);
			if(duration.setDefault) {
				currentDuration = duration;
			}
		}

		// Group 4: Articulation...
		final String group5 = matcher.group(4);
		if(group5 != null) {
			final Articulation articulation = Articulation.getByMiki(group5);
			annotate(ctx, articulation);
		}

		final char c = tonic.charAt(0);
		final Map<Class<? extends Annotation>, Annotation> annotations = inNoteGroup ?
				mtAnnotationMap : annotationMap;
		if(c == 'R') {
			event = new Rest(duration, annotations);
		} else if(c == 'X') {
			event = new Ghost(duration, annotations);
		} else {
			final int number = Note.next(
					lastNote,
					absoluteOctave, relativeOctave,
					tonic);
			event = Note.get(number, duration, annotations);
		}

		if(event instanceof Note) {
			lastNote = ((Note) event).slot;
			score.staff.accountFor(lastNote);
		}

		// Have we reached the end of a measure?
		if(!inNoteGroup) {
			account(event, ctx);
		}
		events.add(event);

		// Ready for next
		relativeOctave = 0;
		absoluteOctave = -1;
	}

	private void padToEnd() {
		if(clicksSoFar > 0) {
			// End of phrase and we have an incomplete measure, pad to the end
			// with rests
			annotationMap.clear();
			final int padSize = measureSize - clicksSoFar;
			final Duration[] durations = Duration.getByClicks(padSize);
			for(final Duration duration : durations) {
				events.add(new Rest(duration, annotationMap));
			}
		}
	}

	private Event account(final Event event, final ParserRuleContext ctx) {
		Event result = event;
		// Keep a tally of  where we are in the measure...
		clicksSoFar += event.duration.clicks;

		if(clicksSoFar >= 32) {
			// We've come to at least the end of the current measure
			clicksSoFar -= 32;
			if(clicksSoFar > 0) {
				/*
				 * The duration of the current event is too long by thirtySeconds
				 * so split it between this bar to fill it and the remainder in the
				 * next
				 */
				Duration[] durations = Duration.getByClicks(
						event.duration.clicks - clicksSoFar);
				int count = durations.length;
				for(int i = 0; i < durations.length; i++) {
					final Event newEvent = event.copy(durations[i]);
					events.add(newEvent);
					if(i == 0) {
						result = newEvent;
					}
				}

				durations = Duration.getByClicks(clicksSoFar);
				count += durations.length;
				for(int i = 0; i < durations.length; i++) {
					final Event newEvent = event.copy(durations[i]);
					if(i == 0) {
						newEvent.add(Barline.bar);
					}
					events.add(newEvent);
				}

				if(!(event instanceof Rest)) {
					event.add(new Bind(count));
				}
				messages.warn(ctx, "Event overflowed measure, rewritten");
			}
		}
		return result;
	}

	private KeySignature parseKeySignature(final String input) {
		final KeySignature result;

		final Matcher matcher = keySignaturePattern.matcher(input);
		String tonic;
		@SuppressWarnings("unused")
		String mode = null;
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 1: Upper case tonic...
		final String group1 = matcher.group(1);
		tonic = group1.substring(0, 1);

		// Group 2: Accidental...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			switch(group2.charAt(0)) {
				case '-', 'b', 'B' -> tonic += "b";
				case '+', '#' -> tonic += "#";
				default -> throw new RuntimeException("Should never get here");
			}
		}

		// Group 3: Mode...
		final String group3 = matcher.group(3);
		if(group3 != null) {
			//noinspection UnusedAssignment
			mode = group3;
		}

		result = KeySignature.get(tonic);

		return result;
	}

	private String normalizeString(final String string) {
		String result;

		assert string.startsWith("\"") && string.endsWith("\"");

		result = StringU.stripQuotes(string).trim();
		if(result.length() == 0) {
			result = null;
		}

		return result;
	}

	private void annotate(final ParserRuleContext ctx, final Annotation annotation) {
		if(annotationMap.put(annotation.getClass(), annotation) != null) {
			messages.warn(ctx, "Annotation overwrites previous value: " + annotation);
		}
	}

	private void setTimeSignature(final TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
		measureSize = timeSignature.getMeasureSize();
	}
}
