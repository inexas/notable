/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.util;

import java.util.*;

public class StringU {
	public static String stripQuotes(final String string) {
		assert string.length() >= 2 && string.startsWith("\"") && string.endsWith("\"") : string;
		return string.substring(1, string.length() - 1);
	}

	public static String stripQuotesTrim(final String string) {
		assert string.length() >= 2 && string.startsWith("\"") && string.endsWith("\"") : string;
		return string.substring(1, string.length() - 1).trim();
	}

	static String toCommaSeparatedList(
			final List<Object> list,
			final String elementDelimiter) {
		final StringBuilder result = new StringBuilder();
		toCommaSeparatedList(list, elementDelimiter, result);
		return result.toString();
	}

	private static void toCommaSeparatedList(
			final List<Object> list,
			final String elementDelimiter,
			final StringBuilder result) {
		if(list != null && list.size() > 0) {
			String tmpDelimiter = "";
			for(final Object object : list) {
				result.append(tmpDelimiter);
				tmpDelimiter = elementDelimiter;
				toString(object, result);
			}
		}
	}

	static String toCommaSeparatedList(
			final Map<String, Object> map,
			final String keyValueDelimiter,
			final String elementDelimiter) {
		final StringBuilder result = new StringBuilder();
		toCommaSeparatedList(map, keyValueDelimiter, elementDelimiter, result);
		return result.toString();
	}

	private static void toCommaSeparatedList(
			final Map<String, Object> map,
			final String keyValueDelimiter,
			final String elementDelimiter,
			final StringBuilder result) {
		final List<String> keyValues = new ArrayList<>();

		if(map != null && map.size() > 0) {
			for(final Map.Entry<String, Object> entry : map.entrySet()) {
				final StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey());
				sb.append(':');
				toString(entry.getValue(), sb);
				keyValues.add(sb.toString());
			}
		}

		Collections.sort(keyValues);
		String delimiter = "";
		for(final String keyValue : keyValues) {
			result.append(delimiter);
			delimiter = elementDelimiter;
			result.append(keyValue);
		}
	}

	private static void toString(final Object object, final StringBuilder result) {
		if(object == null) {
			result.append("null");
		} else if(object instanceof String) {
			// todo Escape string contents
			result.append('"');
			result.append(object.toString());
			result.append('"');
		} else if(object instanceof Boolean) {
			result.append(((Boolean) object).toString());
		} else {
			result.append(object.toString());
		}
	}

	/**
	 * Given a string, trim it and if there's nothing left, return null.
	 *
	 * @param string
	 * @return Null or trimmed text
	 */
	public static String nullOrText(final String string) {
		final String result;

		if(string == null) {
			result = null;
		} else {
			final String tmp = string.trim();
			result = tmp.length() == 0 ? null : tmp;

			return result;
		}

		return result;
	}
}
