package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

abstract class Modifier implements Visited {
	@Override
	public String toString() {
		final ToMikiVisitor result = new ToMikiVisitor();
		accept(result);
		return result.toString();
	}
}
