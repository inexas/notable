/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

/**
 * The ABC of all objects in the model
 */
abstract class Miki implements Visited {

	@Override
	public String toString() {
		final ToMikiVisitor result = new ToMikiVisitor();
		accept(result);
		return result.toString();
	}
}
