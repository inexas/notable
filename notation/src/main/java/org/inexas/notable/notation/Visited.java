/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation;

public interface Visited {
	void accept(Visitor visitor);
}
