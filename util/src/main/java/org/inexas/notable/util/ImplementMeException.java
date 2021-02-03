package org.inexas.notable.util;

import java.io.*;

public class ImplementMeException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6670550900370060879L;

	public ImplementMeException() {
		super(generateMessage());
	}

	private static String generateMessage() {
		final StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
		return "Implement me: " + caller.getClassName() + '.' + caller.getMethodName();
	}
}
