package org.inexas.notable.notation.render;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class RangesMetadataFile extends MetadataFile {
	public static final String filename = "ranges.json";
	public final Map<String, Range> ranges = new HashMap<>();

	@JsonAnySetter
	@SuppressWarnings("unused")
	public void setDetail(String key, Object value) {
		@SuppressWarnings("unchecked") final Map<String, Object> map = (Map<String, Object>) value;
		@SuppressWarnings("unchecked") final List<String> list = (List<String>) map.get("glyphs");
		final String[] array = new String[list.size()];
		list.toArray(array);
		final Range range = new Range(
				(String) map.get("description"),
				(String) map.get("range_start"),
				(String) map.get("range_end"),
				array);
		ranges.put(key, range);
	}

	public static class Range {
		public final String description;
		public final String rangeStart;
		public final String rangeEnd;
		public final String[] glyphs;

		public Range(
				final String description,
				final String rangeStart,
				final String rangeEnd,
				final String[] glyphs) {
			this.description = description;
			this.rangeStart = rangeStart;
			this.rangeEnd = rangeEnd;
			this.glyphs = glyphs;
		}
	}
}
