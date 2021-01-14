/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

class LineParser {
	private final char[] ca;
	private int index;
	Line line;

	LineParser(final String string) {
		// We can be pretty gung ho here as we know the string is valid
		ca = string.toCharArray();
		final char key = ca[1];
		index = 2; // Past {[} and first char
		consumeLetters();

		int noteCount = 0;
		int volta = 0;
		if(key == 'o') {
			// octave-8
			if(ca[index] == '-') {
				noteCount = -1;
				index++;
			} else {
				noteCount = 1;
			}
			noteCount *= consumeNumber();
		} else if(key == 'v') {
			volta = consumeNumber();
		}

		consumeWs();
		final int bars = consumeNumber();
		final int beats = consumeDot() ? consumeNumber() : 0;

		switch(key) {
			case 'b', 'l' -> line = new Bind(bars);
			case 'c' -> line = new Crescendo(bars, beats);
			case 'd' -> line = new Decrescendo(bars, beats);
			case 'o' -> line = new Octave(noteCount, bars, beats);
			case 'p' -> line = new Pedal(bars, beats);
			case 'r' -> line = new BarRest(bars, beats);
			case 'v' -> line = new Volta(volta, bars, beats);
			default -> throw new RuntimeException("Case note handled: " + key);
		}
	}

	private void consumeWs() {
		while(true) {
			final char c = ca[index];
			if(c != ' ' && c != '\t') {
				break;
			}
			index++;
		}
	}

	private int consumeNumber() {
		int result = ca[index++] - '0';
		while(true) {
			final int x = ca[index] - '0';
			if(x < 0 || x > 9) {
				break;
			}
			index++;
			result = result * 10 + x;
		}
		return result;
	}

	private void consumeLetters() {
		while(true) {
			final char c = ca[index];
			if(c < 'a' || c > 'z') {
				break;
			}
			index++;
		}
	}

	private boolean consumeDot() {
		final boolean result;
		if(ca[index] == '.') {
			index++;
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
