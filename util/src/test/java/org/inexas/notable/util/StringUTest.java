/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */
package org.inexas.notable.util;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StringUTest {

	@Test
	void stripQuotes() {
		assertEquals("x", StringU.stripQuotes("\"x\""));
		assertEquals("", StringU.stripQuotes("\"\""));
	}

	@Test
	void listToCommaSeparatedList() {
		final List<Object> list = new ArrayList<>();
		assertEquals("", StringU.toCommaSeparatedList(list, ","));
		list.add("x");
		assertEquals("\"x\"", StringU.toCommaSeparatedList(list, ","));
		list.add(9);
		assertEquals("\"x\",9", StringU.toCommaSeparatedList(list, ","));
		list.add(Boolean.TRUE);
		assertEquals("\"x\",9,true", StringU.toCommaSeparatedList(list, ","));
		list.add(null);
		assertEquals("\"x\",9,true,null", StringU.toCommaSeparatedList(list, ","));
	}

	@Test
	void mapToCommaSeparatedList() {
		final Map<String, Object> map = new HashMap<>();
		assertEquals("", StringU.toCommaSeparatedList(map, ":", ","));
		map.put("n", null);
		map.put("i", 3);
		map.put("b", Boolean.TRUE);
		assertEquals(
				"b:true,i:3,n:null",
				StringU.toCommaSeparatedList(map, ":", ","));
	}
}