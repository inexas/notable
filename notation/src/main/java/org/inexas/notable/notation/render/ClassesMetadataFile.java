package org.inexas.notable.notation.render;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class ClassesMetadataFile extends MetadataFile {
	public static final String filename = "classes.json";

	public static final ClassesMetadataFile instance =
			ClassesMetadataFile.load(ClassesMetadataFile.class, filename);

	private final Map<String, String> classMap = new HashMap<>();

	/**
	 * @param glyphName Get class of this name
	 * @return SMuFL class name, e.g. "clefs", or null if unclassified
	 */
	String getClass(final String glyphName) {
		return classMap.get(glyphName);
	}

	@JsonAnySetter
	@SuppressWarnings("unused")
	public void setDetail(final String key, final Object glyphNames) {
		@SuppressWarnings("unchecked") final List<String> glyphNameList = (List<String>) glyphNames;
		if(key.equals("clefs")
				|| key.equals("accidentals")
				|| key.equals("noteheads")
				|| key.equals("rests")) {
			for(final String glyphName : glyphNameList) {
				classMap.put(glyphName, key);
			}
		}
	}
}
