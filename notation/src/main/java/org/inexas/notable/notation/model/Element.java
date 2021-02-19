/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * The ABC of all elements in the model
 */
abstract class Element implements Visited {
	@Override
	public final String toString() {
		final ToMikiVisitor result = new ToMikiVisitor();
		accept(result);
		return result.toString();
	}
}
