package org.inexas.notable.notation.parser;

import org.antlr.v4.runtime.*;

import java.io.*;
import java.util.*;

/**
 * Error and warning handling infrastructure
 */
public class Messages {
	private final boolean isFile;
	private final String source;
	ParserRuleContext ctx;
	private final List<Message> messages = new ArrayList<>();
	private final List<Message> errors = new ArrayList<>();
	private final List<Message> warnings = new ArrayList<>();


	boolean containExcerpt(final String excerpt) {
		boolean result = false;

		for(final Message message : messages) {
			if(message.message.contains(excerpt)) {
				result = true;
				break;
			}
		}

		return result;
	}

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

	Messages(final boolean isFile, final String source) {
		this.isFile = isFile;
		this.source = source;
	}

	boolean hasMessages() {
		return messages.size() > 0;
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

	public int count() {
		return messages.size();
	}

	public String getError(final int index) {
		return errors.get(index).message;
	}

	public int getErrorCount() {
		return errors.size();
	}
}
