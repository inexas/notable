/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation.parser;

import org.antlr.v4.runtime.*;
import org.inexas.notable.notation.model.*;
import org.inexas.notable.util.*;

import java.util.*;
import java.util.regex.*;

public class ToScoreListener extends MusicBaseListener {
	private final static int noteCount = 12;
	private final static Pattern keySignaturePattern = Pattern.compile("" +
			"([A-G])" +         // Tonic
			"([b#n])?" +        // Accidental
			"([Mm])?");         // Major/minor
	private final static Pattern notePattern = Pattern.compile("" +
			"([A-Grx])" +       // Tonic, including rest & ghost
			"([b#n])?" +        // Accidental
			"([0-9]+,*\\*?)?" + // Duration
			"([._!fg]+)?");     // Articulation
	private final static Pattern noteGroupEndPattern = Pattern.compile("" +
			"]" +               // Closing ]
			"([0-9]+,*\\*?)?" + // Duration
			"([._!fg]+)?");     // Articulation
	private final Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
	private final String filename;
	private Phrase currentPhrase;
	private Duration defaultDuration;
	private int absoluteOctave;
	private int relativeOctave;
	private int lastNoteIndex;
	private int lastNoteOctave;
	private Part currentPart;
	private boolean settingDefaults = true;
	/**
	 * The number of clicks counted so far in the current measure
	 */
	private int clicksSoFar;
	private int measureSize;
	// C  .  .  .  E  .  .  G
	//       D  .  .  F  .  .  .  F
	//                                  B  .  .  D  .  .  F
	// C  Cs D  Ds E  F  Fs G  Gs A  As B  C  Cs D  Ds E  F  Fs G  Gs A  As B  C
	// 3                                   4                                   5
	// 0  1  2  3  4  5  6  7  8  9  10 11 0  1  2  3  4  5  6  7  8  9  10 11 1
	// 0  1  2  3  4  5  6  7  8  9  10 11 13 14 15 16 17 18 19 20 21 22 23 24 25
	@SuppressWarnings("unused")
	private TimeSignature timeSignature;
	private List<Event> events;
	private boolean inNoteGroup;
	@SuppressWarnings("FieldCanBeLocal")
	private final boolean debug = false;
	/**
	 * This is used to save the state of the duration when a Note group is being
	 * processed. For example, in the miki "C4* [t C E G]8 A" the duration is defaulted
	 * to 4 then in the Note group it's set to eighths but without a default '*'
	 * so the trailing A will have a duration of 4. In this case durationStack
	 * will save the 'quarter' duration set with "C4*"
	 */
	private Duration saveDuration;
	public final Score score = new Score();
	public final List<String> messages = score.messages;

	public ToScoreListener(final String filename) {
		this.filename = filename;
	}

	@Override
	public void enterScore(final MusicParser.ScoreContext ctx) {
		currentPart = score.getFirstPart();
		currentPhrase = currentPart.getFirstPhrase();
		events = currentPhrase.events;
		// Set the defaults...
		setTimeSignature(TimeSignature.DEFAULT);
		defaultDuration = Duration.quarter;
		lastNoteIndex = 0;
		lastNoteOctave = 4;
	}

	@Override
	public void exitScore(final MusicParser.ScoreContext ctx) {
		// todo Pad all the Phrases the end
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
		settingDefaults = false;
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
		settingDefaults = false;
		// todo Reset everything
		clicksSoFar = 0;
		events = currentPhrase.events;
	}

	@Override
	public void exitPhrase(final MusicParser.PhraseContext ctx) {
		currentPhrase = null;
	}

	@Override
	public void enterStaff(final MusicParser.StaffContext ctx) {
		final String text = ctx.getStop().getText();
		final Staff staff = Staff.getStaff(text);
		if(settingDefaults) {
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
			// tempo 1 / 4 = 120
			// 0     1 2 3 4 5
			assert ctx.getChildCount() == 6;
			final int denominator = Integer.parseInt(ctx.getChild(3).getText());
			final Duration duration = Duration.getByDenominator(denominator);
			final int bpm = Integer.parseInt(ctx.getChild(5).getText());
			tempo = Tempo.getTempo(duration, bpm);
		}
		annotate(tempo);
	}

	@Override
	public void enterKey(final MusicParser.KeyContext ctx) {
		final String text = ctx.getStop().getText();
		final KeySignature keySignature = parseKeySignature(text);
		if(settingDefaults) {
			score.keySignature = keySignature;
		} else {
			annotate(keySignature);
		}
	}

	@Override
	public void enterTime(final MusicParser.TimeContext ctx) {
		// time: TIME ( COUNT SLASH COUNT | COMMON | CUT ) ;
		final int count = ctx.getChildCount();
		final TimeSignature timeSignature;
		if (count == 4) {
			timeSignature = new TimeSignature(
					Integer.parseInt(ctx.getChild(1).getText()),
					Integer.parseInt(ctx.getChild(3).getText()));
		} else {
			timeSignature = ctx.getStop().getType() == MusicParser.CUT ?
					TimeSignature.CUT : TimeSignature.COMMON;
		}
		if (settingDefaults) {
			score.timeSignature = timeSignature;
		} else {
			annotate(timeSignature);
		}
		clicksSoFar = 0;
		setTimeSignature(timeSignature);
	}

	@Override
	public void enterPickup(final MusicParser.PickupContext ctx) {
		// pickup: COUNT '/' COUNT ;
		score.pickupMeasure = new PickupMeasure(
				Integer.parseInt(ctx.getChild(1).getText()),
				Integer.parseInt(ctx.getChild(3).getText()));
	}

	@Override
	public void enterBarline(final MusicParser.BarlineContext ctx) {
		final String text = ctx.getStop().getText();
		final Barline barline = Barline.getBarline(text);
		annotate(barline);
	}

	@Override
	public void enterLine(final MusicParser.LineContext ctx) {
		final String text = ctx.getStop().getText();
		final LineParser parser = new LineParser(text);
		annotate(parser.line);
	}

	@Override
	public void enterOctave(final MusicParser.OctaveContext ctx) {
		// E.g. o[1-8] or > on <
		final String text = ctx.getStop().getText();
		if(ctx.children.size() == 1) {
			// Relative modifier
			relativeOctave = text.charAt(0) == '>' ? 1 : -1;
			if(absoluteOctave > 0) {
				messages.add("Possibly some confusion over octaves");
				absoluteOctave = 0;
			}
		} else {
			absoluteOctave = Integer.parseInt(text);
			if(relativeOctave != 0) {
				messages.add("Possibly some confusion over octaves");
				relativeOctave = 0;
			}
		}
	}

	@Override
	public void enterDynamic(final MusicParser.DynamicContext ctx) {
		final String text = ctx.getStop().getText();
		final Dynamic dynamic = Dynamic.getDynamic(text);
		annotate(dynamic);
	}

	@Override
	public void enterText(final MusicParser.TextContext ctx) {
		final String text = StringU.stripQuotes(ctx.getStop().getText());
		final TextAnnotation textAnnotation = new TextAnnotation(text);
		annotate(textAnnotation);
	}

	@Override
	public void enterFingering(final MusicParser.FingeringContext ctx) {
		final String text = ctx.getStop().getText();
		final Fingering fingering = new Fingering(text.substring(1));
		annotate(fingering);
	}

	@Override
	public void enterEvent(final MusicParser.EventContext ctx) {
		// Leading barline...
		final boolean barAnnotated = annotationMap.containsKey(Barline.class);
		if(clicksSoFar == 0) {
			// We're at the start of a new measure, make sure it's annotated...
			if(!barAnnotated) {
				annotate(Barline.bar);
			}
		} else if(barAnnotated) {
			// todo Removing a repeat bar may cause other issues
			annotationMap.remove(Barline.class);
			warning(ctx, "Bar annotation not at start of measure; removed.");
		}
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
		saveDuration = defaultDuration;
		events = new ArrayList<>();
		inNoteGroup = true;
	}

	@Override
	public void exitTuplet(final MusicParser.TupletContext ctx) {
		defaultDuration = saveDuration;
		final String text = ctx.getStop().getText();
		Duration duration;
		if(text.length() > 1) {
			final Matcher matcher = noteGroupEndPattern.matcher(text);
			if(!matcher.matches()) {
				throw new RuntimeException("Recognizer/event parser mismatch");
			}
			if(matcher.group(2) != null) {
				warning(ctx, "Tuplet articulation not supported, ignored: " + text);
			}

			// Group 1: Duration...
			final String group1 = matcher.group(1);
			if(group1 != null) {
				duration = Duration.getByMiki(group1);
				if(!duration.setDefault) {
					saveDuration = defaultDuration;
				}
				defaultDuration = duration;
			} else {
				// No duration but an erroneous articulation
				duration = defaultDuration;
			}
		} else {
			duration = defaultDuration;
		}
		final int total = clicksSoFar + duration.clicks;
		if(total > measureSize) {
			warning(ctx, "Tuplet duration to long, reduced");
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
		final NamedChord namedChord = NamedChord.parse(text, defaultDuration, annotationMap);
		final Event event = account(namedChord, ctx);
		events.add(event);
		if(namedChord.duration.setDefault) {
			defaultDuration = namedChord.duration;
		}
	}

	@Override
	public void enterEveryRule(final ParserRuleContext ctx) {
		if(debug) {
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
			duration = defaultDuration;
		} else {
			duration = Duration.getByMiki(group1);
			if(duration.setDefault) {
				defaultDuration = duration;
			}
		}
		// todo Take care of duration overflow

		// Group 2: Articulation...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			final Articulation articulation = Articulation.getByMiki(group2);
			annotate(articulation);
		}

		result = new Chord(duration, events, annotationMap);
		result = (Chord)account(result, ctx);

		return result;
	}

	private void buildEvent(final ParserRuleContext ctx) {
		final Event event;

		final String input = ctx.stop.getText();
		final Matcher matcher = notePattern.matcher(input);
		if(!matcher.matches()) {
			throw new RuntimeException("Recognizer/event parser mismatch");
		}

		// Group 1: Tonic...
		String tonic = matcher.group(1);

		// Group 2: Accidental...
		final String group2 = matcher.group(2);
		if(group2 != null) {
			switch(group2.charAt(0)) {
				case 'b':
					tonic += "b";
					break;
				case '#':
					tonic += "#";
					break;
				case 'n':
					break;
				default:
					throw new RuntimeException("Should never get here");
			}
		}

		// Group 3: Duration...
		final Duration duration;
		final String group3 = matcher.group(3);
		if(group3 == null) {
			duration = defaultDuration;
		} else {
			duration = Duration.getByMiki(group3);
			if(duration.setDefault) {
				defaultDuration = duration;
			}
		}

		// Group 4: Articulation...
		final String group5 = matcher.group(4);
		if(group5 != null) {
			final Articulation articulation = Articulation.getByMiki(group5);
			annotate(articulation);
		}

		final char c = tonic.charAt(0);
		if(c == 'R') {
			event = new Rest(duration, annotationMap);
		} else if(c == 'X') {
			event = new Ghost(duration, annotationMap);
		} else {
			final int octave = calculateOctave();
			event = new Note(tonic, octave, duration, annotationMap);
		}

		if(event instanceof Note) {
			// It's a note so remember the index and octave
			final Note note = (Note)event;
			lastNoteIndex = note.index;
			lastNoteOctave = note.octave;
		}

		// Have we reached the end of a measure?
		if(!inNoteGroup) {
			account(event, ctx);
		}
		events.add(event);
	}

	private void padToEnd() {
		if(clicksSoFar > 0) {
			// End of phrase and we have an incomplete measure, pad to the end
			// with rests
			final int padSize = measureSize - clicksSoFar;
			final Duration[] durations = Duration.getByClicks(padSize);
			for(final Duration duration : durations) {
				events.add(new Rest(duration, annotationMap));
			}
		}
	}

	/**
	 * Warnings can be corrected whilst building the Score but perhaps not with the
	 * results that the user expected.
	 *
	 * @param ctx     Parser context
	 * @param message Warning message
	 */
	private void warning(final ParserRuleContext ctx, final String message) {
		final Token token = ctx.start;
		messages.add("WARNING " + filename + ' ' +
				token.getLine() + ':' + token.getStartIndex() +
				' ' + message);
	}

	/**
	 * Errors result in a failure to create a score
	 *
	 * @param ctx     Parser context
	 * @param message Error message
	 */
	@SuppressWarnings("unused")
	private void error(final ParserRuleContext ctx, final String message) {
		final Token token = ctx.start;
		messages.add("ERROR " + filename + ' ' +
				token.getLine() + ':' + token.getStartIndex() +
				' ' + message);
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
				warning(ctx, "Event overflowed measure, rewritten");
			}
		}
		return result;
	}

	private KeySignature parseKeySignature(final String input) {
		final KeySignature result;

		final Matcher matcher = keySignaturePattern.matcher(input);
		String tonic;
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
			mode = group3;
		}

		result = KeySignature.getKeySignature(tonic, mode);

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

	private int calculateOctave() {
		int result;

		if(lastNoteOctave > 0) {
			// The octave was explicitly set in the tonic, e.g. "B3"
			result = lastNoteOctave;
		} else if(absoluteOctave > 0) {
			// The octave was explicitly set as an annotation, e.g. "o3"
			result = absoluteOctave;
		} else {
			// Else calculateOctave it
			result = lastNoteOctave;
			// Find nearest note to last...
			int difference = (lastNoteIndex + noteCount - lastNoteIndex) % noteCount;
			if(difference > 5) {
				difference -= noteCount;
			}

			// Have we crossed an octave...
			final int offset = lastNoteIndex + difference;
			if(offset > 11) {
				result++;
			} else if(offset < 0) {
				result--;
			}

			// Add any relativeOctave adjustments to apply, e.g. ">A"
			result += relativeOctave;
		}

		// Check in bounds
		if(result < 1) {
			result = 1;
		} else if(result > 8) {
			result = 8;
		}

		// Reset
		absoluteOctave = relativeOctave = 0;

		return result;
	}

	private void annotate(final Annotation annotation) {
		if(annotationMap.put(annotation.getClass(), annotation) != null) {
			messages.add("Annotation overwrites previous value: " + annotation);
		}
	}

	private void setTimeSignature(final TimeSignature timeSignature) {
		this.timeSignature = timeSignature;
		measureSize = timeSignature.getMeasureSize();
	}
}
