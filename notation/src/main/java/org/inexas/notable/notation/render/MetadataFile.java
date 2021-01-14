package org.inexas.notable.notation.render;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * This class uses Jackson to read in the metadata files. The file should be
 * in SMuFL format and is documented here:
 *
 * https://w3c.github.io/smufl/gitbook/specification/engravingdefaults.html
 *
 * The files themselves come with the font installation and, on a Mac, are installed
 * in /Library/Application/SMuFL/Fonts/Bravura.
 * <p>
 * Documentation for the individual aspects can be found here
 * https://w3c.github.io/smufl/gitbook/specification/font-specific-metadata.html
 */
abstract class MetadataFile {
	public static <T extends MetadataFile> T load(Class<T> t, String pathName) {
		final T returnValue;

		try {
			final ObjectMapper mapper = new ObjectMapper();
			final ClassLoader classLoader = t.getClassLoader();
			final InputStream is = classLoader.getResourceAsStream(pathName);
			returnValue = mapper.readValue(is, t);
		} catch(final IOException e) {
			throw new RuntimeException("Error loading " + t.getSimpleName(), e);
		}

		return returnValue;
	}
}
