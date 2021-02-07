package org.inexas.notable.util;

import java.io.*;

public class ImplementMeException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6670550900370060879L;

	public ImplementMeException(final String... strings) {
		super(generateMessage(strings));
	}

	private static String generateMessage(final String... strings) {
		final StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
		final StringBuilder sb = new StringBuilder("Implement me: ");
		sb.append(
				caller.getClassName()).append('.')
				.append(caller.getMethodName())
				.append("()");
		if(strings.length > 0) {
			for(final String string : strings) {
				sb.append(' ');
				sb.append(string);
			}
		}
		return sb.toString();
	}
}
