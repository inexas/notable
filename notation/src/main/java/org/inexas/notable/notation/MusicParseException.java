/*
 * Copyright (C) 2020 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

import org.antlr.v4.runtime.*;

public class MusicParseException extends RuntimeException {
	@SuppressWarnings("unused")
	private ParserRuleContext ctx;

	public MusicParseException(final String message, final ParserRuleContext ctx) {
		super(message);
		this.ctx = ctx;
	}

	@SuppressWarnings("unused")
	public MusicParseException(final String message) {
		super(message);
	}
}

