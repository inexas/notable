package org.inexas.notable.notation.parser;

import org.antlr.v4.runtime.*;

import java.util.*;

/**
 * Error and warning handling infrastructure
 */
public class Messages {
	private final boolean isFile;
	private final String source;

	boolean containExcerpt(final String excerpt) {
		boolean returnValue = false;

		for(final Message message : messages) {
			if(message.message.contains(excerpt)) {
				returnValue = true;
				break;
			}
		}

		return returnValue;
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
			final Token token = ctx.start;
			return type + ": " + token.getLine() + ':' + token.getStartIndex() + ' ' + message;
		}
	}

	private final List<Message> messages = new ArrayList<>();

	Messages(final boolean isFile, final String source) {
		this.isFile = isFile;
		this.source = source;
	}

	boolean hasMessages() {
		return messages.size() > 0;
	}

	void error(final ParserRuleContext ctx, final String text) {
		messages.add(new Message(Message.Type.Error, ctx, text));
	}

	void warn(final ParserRuleContext ctx, final String text) {
		messages.add(new Message(Message.Type.Warning, ctx, text));
	}
}
