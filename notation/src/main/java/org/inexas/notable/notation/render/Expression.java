package org.inexas.notable.notation.render;

import java.io.*;
import java.lang.reflect.*;

/**
 * This is a small, purpose build expression engine that is used
 * to set the value of double class members according to the expression.
 * For example: "rBearing = rBearing * 1.2". Be careful that fields
 * that are updated are not final - there seems to be a bug in Java.
 * <p>
 * See: https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
 */
class Expression {
	class ParsingException extends RuntimeException {
		@Serial
		private static final long serialVersionUID = 2069710410446355886L;

		ParsingException(final String message) {
			super("Expression: '" + string + "', column: " + (cursor + 1) + ' ' + message);
		}
	}

	class ExecutionException extends RuntimeException {
		@Serial
		private static final long serialVersionUID = -2674014326439933723L;

		ExecutionException(final String message, final Exception e) {
			super("Expression: '" + string + "': " + message, e);
		}
	}

	static abstract class Node {
		abstract double evaluate(final Object object);
	}

	private class AssignmentNode extends Node {
		String identifier;
		Node rhs;

		AssignmentNode(final String identifier, final Node rhs) {
			this.identifier = identifier;
			this.rhs = rhs;
		}

		@Override
		double evaluate(final Object object) {
			try {
				final double value = rhs.evaluate(object);
				final Field field = object.getClass().getDeclaredField(identifier);
				field.setAccessible(true);
				field.set(object, value);
				return value;
			} catch(final IllegalAccessException | NoSuchFieldException e) {
				throw new ExecutionException("Error setting value: '" + identifier + '\'', e);
			}
		}
	}

	private class IdentifierNode extends Node {
		String identifier;

		IdentifierNode(final String identifier) {
			this.identifier = identifier;
		}

		@Override
		double evaluate(final Object object) {
			final double result;

			try {
				final Field field = object.getClass().getDeclaredField(identifier);
				field.setAccessible(true);
				result = (Double) field.get(object);
			} catch(final Exception e) {
				throw new ExecutionException("Error reading "
						+ object.getClass().getName() + '.' + identifier, e);
			}

			return result;
		}
	}

	private static class UnaryMinusNode extends Node {
		Node child;

		UnaryMinusNode(final Node child) {
			this.child = child;
		}

		@Override
		double evaluate(final Object object) {
			return -child.evaluate(object);
		}
	}

	private static class MultiplyNode extends Node {
		Node lhs, rhs;

		MultiplyNode(final Node lhs, final Node rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		double evaluate(final Object object) {
			return lhs.evaluate(object) * rhs.evaluate(object);
		}
	}

	private static class DivideNode extends Node {
		Node lhs, rhs;

		DivideNode(final Node lhs, final Node rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		double evaluate(final Object object) {
			return lhs.evaluate(object) / rhs.evaluate(object);
		}
	}

	private static class PlusNode extends Node {
		Node lhs, rhs;

		PlusNode(final Node lhs, final Node rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		double evaluate(final Object object) {
			return lhs.evaluate(object) + rhs.evaluate(object);
		}
	}

	private static class MinusNode extends Node {
		Node lhs, rhs;

		MinusNode(final Node lhs, final Node rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		double evaluate(final Object object) {
			return lhs.evaluate(object) - rhs.evaluate(object);
		}
	}

	private static class LiteralNode extends Node {
		double x;

		LiteralNode(final double x) {
			this.x = x;
		}

		@Override
		double evaluate(final Object object) {
			return x;
		}
	}

	private final static char EOF = (char) -1;
	private int cursor = -1;
	private char c;
	private final char[] ca;
	private final int length;
	private String eaten;
	private final String string;
	Node ast;

	Expression(final String string) {
		assert string != null;
		this.string = string;

		ca = string.toCharArray();
		length = ca.length;

		next();
		ast = assignment();
		if(cursor < length) {
			throw new ParsingException("Unexpected: " + c);
		}
	}

	private Node assignment() {
		final Node result;

		if(eatIdentifier()) {
			if(eat('=')) {
				result = new AssignmentNode(eaten, expression());
			} else {
				throw new ParsingException("Missing assignment");
			}
		} else {
			throw new ParsingException("Missing assignment");
		}

		return result;
	}

	private Node expression() {
		Node x = term();
		while(true) {
			if(eat('+')) {   // addition
				x = new PlusNode(x, factor());
			} else if(eat('-')) {   // subtraction
				x = new MinusNode(x, factor());
			} else {
				return x;
			}
		}
	}

	private Node term() {
		Node x = factor();
		for(; ; ) {
			if(eat('*')) { // multiplication
				x = new MultiplyNode(x, factor());
			} else if(eat('/')) {  // division
				x = new DivideNode(x, factor());
			} else {
				return x;
			}
		}
	}

	private Node factor() {
		final Node x;

		if(eat('+')) {
			return factor(); // unary plus
		}
		if(eat('-')) { // unary minus
			return new UnaryMinusNode(factor());
		}

		if(eat('(')) { // parentheses
			x = expression();
			eat(')');
		} else if(eatDouble()) {
			x = new LiteralNode(Double.parseDouble(eaten));
		} else if(eatIdentifier()) {
			x = new IdentifierNode(eaten);
		} else {
			throw new ParsingException("Unexpected: " + c);
		}

		return x;
	}

	private boolean eatDouble() {
		final boolean result;

		final int startPos = cursor;
		if(isNumeric(c)) {
			next();
			while(isNumeric(c)) {
				next();
			}
			eaten = new String(ca, startPos, cursor - startPos);
			result = true;
		} else {
			result = false;
		}

		return result;
	}


	private boolean eatIdentifier() {
		final boolean result;

		final int startPos = cursor;
		if(isAlphabetic(c)) {
			next();
			while(isAlphanumeric(c)) {
				next();
			}
			eaten = new String(ca, startPos, cursor - startPos);
			result = true;
		} else {
			result = false;
		}

		return result;
	}

	private boolean isAlphanumeric(final char c) {
		return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
	}

	private boolean isNumeric(final char c) {
		return c >= '0' && c <= '9' || c == '.';
	}

	private boolean isAlphabetic(final char c) {
		return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
	}

	private boolean eat(final char eatMe) {
		final boolean result;
		while(Character.isWhitespace(c)) {
			next();
		}
		if(c == eatMe) {
			next();
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	private void next() {
		c = ++cursor < length ? ca[cursor] : EOF;
	}
}
