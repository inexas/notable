package org.inexas.notable.notation.render;

import org.inexas.notable.util.*;

import java.util.*;

/**
 * See https://w3c.github.io/smufl/gitbook/tables/individual-notes.html
 */
public class Glyph {
	private static final String[][] metadataSource = {
			// Key, LB, RB
			{"clefs", "b:0,0.2,0.3,0"},
			{"noteheads", "b:0,0,1.8,0"},
			{"rests", "b:0,0,1.8,0"},
			{"restWhole", "x:y=y+spaceHeight"},
			{"accidentals", "b:0,0,1.2,0"},
	};

	private static class Metadata {
		double lBearing, rBearing, tBearing, bBearing;
		List<Expression.Node> pre = new ArrayList<>();
		List<Expression.Node> post = new ArrayList<>();

		Metadata(final String commandString) {
			// Something like "b:0,0.2,0.3,0"
			final char descriminator = commandString.charAt(0);
			assert commandString.charAt(1) == ':';
			final String commands = commandString.substring(2);

			switch(descriminator) {
				case 'b' -> {   // Bearings
					// b:Bearings N, W, E, S
					final double[] d = ArrayU.parseDoubles(commands);
					assert d.length == 4;
					tBearing = d[0];
					lBearing = d[1];
					rBearing = d[2];
					bBearing = d[3];
				}
				case 'X', 'x' -> {   // Expressions
					// x:expr(,expr)
					final String[] strings = commands.split(",");
					for(final String string : strings) {
						if(descriminator == 'X') {
							pre.add(new Expression(string).ast);
						} else {
							post.add(new Expression(string).ast);
						}
					}
				}
				default -> throw new ImplementMeException(commandString);
			}
		}
	}

	private static final Map<String, Metadata> metadataMap = new HashMap<>();

	static {
		for(final String[] array : metadataSource) {
			metadataMap.put(array[0], new Metadata(array[1]));
		}
	}

	public final String name;
	public final String description;
	public final String codepoint;
	public final String c;
	final double xNE;
	final double yNE;
	final double xSW;
	final double ySW;
	final double height;
	final double width;
	/**
	 * The amount of space in pixels to be left of the glyph clear
	 */
	double lBearing;
	/**
	 * The amount of space in pixels to be right of the glyph clear
	 */
	double rBearing;
	private double tBearing;
	private double bBearing;
	/**
	 * lBearing + width + rBearing
	 */
	final double advance;

	Glyph(final String name, final double scaleFactor) {
		this.name = name;

		// Identity
		final GlyphNamesMetadataFile.GlyphIdentity identity =
				GlyphNamesMetadataFile.instance.glyphs.get(name);

		codepoint = identity.codepoint;

		// Convert something like "U+1D100" to character in a String...
		c = Character.toString(Integer.parseInt(identity.codepoint.substring(2), 16));

		description = identity.description;

		// Bounding box
		final Map<String, Double[]> box = FontMetadataFile.instance.glyphBBoxes.get(name);
		xNE = box.get("bBoxNE")[0] * scaleFactor;
		yNE = box.get("bBoxNE")[1] * scaleFactor;
		xSW = box.get("bBoxSW")[0] * scaleFactor;
		ySW = box.get("bBoxSW")[1] * scaleFactor;
		// Bounding box dimensions
		height = (yNE - ySW);
		width = (xNE - xSW);

//		// Anchor points...
//		final Map<String, Double[]> anchors =
//				FontMetadataFile.instance.glyphsWithAnchors.get(name);
//		if(anchors != null) {
//			System.out.println("---------" + name);
//			for(final Map.Entry<String, Double[]> entry : anchors.entrySet()) {
//				final Double[] values = entry.getValue();
//				System.out.println(entry.getKey() + ": (" +
//						values[0] + ", " + values[1] + ")");
//			}
//		}

		final Metadata metadata = getMetadata(name);
		if(metadata != null) {
			tBearing = height * metadata.tBearing;
			lBearing = width * metadata.lBearing;
			rBearing = width * metadata.rBearing;
			bBearing = height * metadata.bBearing;

			metadata.pre.forEach(x -> x.evaluate(this));
		}

		advance = lBearing + width + rBearing;
	}

	private Metadata getMetadata(final String glyphName) {
		Metadata result;

		result = metadataMap.get(glyphName);
		if(result == null) {
			final String clazz = ClassesMetadataFile.instance.getClass(glyphName);
			if(clazz != null) {
				result = metadataMap.get(clazz);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return "Glyph {" +
				name + ": " + description + " " + codepoint + ' ' + c +
				", xNE=" + xNE +
				", yNE=" + yNE +
				", xSW=" + xSW +
				", ySW=" + ySW +
				", h=" + height +
				", w=" + width +
				", l=" + lBearing +
				", r=" + rBearing +
				'}';
	}
}
