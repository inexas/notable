package org.inexas.notable.notation.parser;

import org.antlr.v4.runtime.*;

import java.io.*;
import java.util.*;

/**
 * Error and warning handling class
 */
public class Messages {
	public static class Message {
		public enum Type {
			Error, Warning, Info
		}

		public final Type type;
		final ParserRuleContext ctx;
		public final String message;

		Message(final Type type, final ParserRuleContext ctx, final String message) {
			this.type = type;
			this.ctx = ctx;
			this.message = message;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			toString(sb);
			return sb.toString();
		}

		public void toString(final StringBuilder sb) {
			final Token token = ctx.start;
			sb.append(type);
			sb.append(": ");
			sb.append(token.getLine());
			sb.append(':');
			sb.append(token.getCharPositionInLine());
			sb.append(' ');
			sb.append(message);
		}
	}

	private final List<Message> messages = new ArrayList<>();
	private final List<Message> errors = new ArrayList<>();
	private final List<Message> warnings = new ArrayList<>();
	private final boolean isFile;
	private final String source;
	/**
	 * This is used to set the current Parser context which can be used
	 * to point at which miki caused the problem. Note, it may remained
	 * set well after the context has passed. Set it to null for post
	 * parsing tracking.
	 */
	ParserRuleContext ctx;

	Messages(final boolean isFile, final String source) {
		this.isFile = isFile;
		this.source = source;
	}

	public void error(final String text) {
		final Message message = new Message(Message.Type.Error, ctx, text);
		messages.add(message);
		errors.add(message);
	}

	public void warn(final String text) {
		final Message message = new Message(Message.Type.Warning, ctx, text);
		messages.add(message);
		warnings.add(message);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		if(isFile) {
			sb.append(new File(source).getAbsolutePath());
			sb.append('\n');
		} else {
			if(messages.size() > 1) {
				sb.append("--------------\n");
				sb.append(source);
				if(sb.charAt(sb.length() - 1) != '\n') {
					sb.append('\n');
				}
				sb.append("--------------\n");
			}
		}

		for(final Message message : messages) {
			message.toString(sb);
			sb.append('\n');
		}

		return sb.toString();
	}

	boolean hasMessages() {
		return messages.size() > 0;
	}

	boolean contains(final String excerpt) {
		boolean result = false;
		for(final Message message : messages) {
			if(message.message.contains(excerpt)) {
				result = true;
				break;
			}
		}
		return result;
	}

	String getError(final int index) {
		return errors.get(index).message;
	}

	String getWarning(final int index) {
		return warnings.get(index).message;
	}

	int getErrorCount() {
		return errors.size();
	}

	int getWarningCount() {
		return warnings.size();
	}
}
