package org.inexas.notable.notation.render;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class ClassesMetadataFile extends MetadataFile {
	public static final String filename = "classes.json";

	public final Map<String, String[]> classMap = new HashMap<>();

	@JsonAnySetter
	@SuppressWarnings("unused")
	public void setDetail(String key, Object value) {
		@SuppressWarnings("unchecked") final List<String> list = (List<String>) value;
		final String[] array = new String[list.size()];
		list.toArray(array);
		classMap.put(key, array);
	}
}
