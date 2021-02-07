package org.inexas.notable.notation.render;

import org.inexas.notable.notation.render.Expression.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTest {
	private static class Target {
		private double x;
		private double y;

		void reset() {
			x = y = -1.0;
		}

		@Override
		public String toString() {
			return "Example{" +
					"x=" + x +
					", y=" + y +
					'}';
		}
	}

	private double test(final String toTest, final Target target) {
		final Expression expression = new Expression(toTest);
		expression.ast.evaluate(target);
		return target.x;
	}

	@Test
	void math() {
		final Target target = new Target();
		target.reset();
		assertEquals(1., test("x=1", target), 0.0001);
		assertEquals(4., test("x=1.5+2.5", target), 0.0001);
		assertEquals(4., test("x=1.5+2.5", target), 0.0001);
		assertEquals(6., test("x = 2 * 3", target), 0.0001);
		assertEquals(2., test("x = 6 / 3", target), 0.0001);
		assertEquals(2., test("x = 6 / 3", target), 0.0001);
		target.reset();
		assertEquals(1., test("x = -y", target), 0.0001);
	}

	@Test
	void parenthesis() {
		final Target target = new Target();
		target.reset();
		assertEquals(10., test("x=2*3+4", target), 0.0001);
		assertEquals(10., test("x=(2*3)+4", target), 0.0001);
		assertEquals(14., test("x=2*(3+4)", target), 0.0001);
		assertEquals(14., test("x=(2*(3+4))", target), 0.0001);
	}

	@Test
	void read() {
		final Target target = new Target();
		target.reset();
		target.y = 100.0;
		assertEquals(100., test("x=y", target), 0.0001);
	}

	@Test
	void exceptions() {
		final Target target = new Target();
		target.reset();
		assertThrows(AssertionError.class, () -> new Expression(null));
		assertThrows(ParsingException.class, () -> new Expression(""));
		assertThrows(ParsingException.class, () -> new Expression("x=2+"));
		final Node ast = new Expression("p=2+q").ast;
		assertThrows(ExecutionException.class, () -> ast.evaluate(target));
	}
}

