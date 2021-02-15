package org.inexas.notable.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MappedListTest {
	private static class Example implements MappedList.Named {
		private final String name;

		Example(final String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	@Test
	void create() {
		final MappedList<Example> ml = new MappedList<>();
		assertEquals(0, ml.size());
	}

	@Test
	void add() {
		final MappedList<Example> ml = new MappedList<>();
		ml.add(new Example(""));
		assertEquals(1, ml.size());
		final Example example = ml.getFirst();
	}

	@Test
	void get() {
		MappedList<Example> ml = new MappedList<>();
		ml.add(new Example("0"));
		ml.add(new Example("1"));
		ml.add(new Example("2"));
		assertEquals(3, ml.size());

		assertEquals("0", ml.getFirst().getName());
		assertEquals("0", ml.get(0).getName());
		assertEquals("1", ml.get(1).getName());
		assertEquals("2", ml.get(2).getName());
		assertEquals("2", ml.getLast().getName());

		ml.add(new Example(""));
		ml.add(new Example(null));
		assertEquals(5, ml.size());

		assertNull(ml.get(null).getName());
		assertEquals("", ml.get("").getName());

		assertEquals(0, ml.indexOf("0"));
		assertEquals(2, ml.indexOf("2"));
		assertEquals(3, ml.indexOf(""));
		assertEquals(4, ml.indexOf(null));

		ml = new MappedList<>();
		ml.add(new Example(""));
		assertEquals(0, ml.indexOf(""));
	}

	@Test
	void exceptions() {
		final MappedList<Example> ml = new MappedList<>();
		assertThrows(RuntimeException.class, ml::getFirst);
		assertThrows(RuntimeException.class, ml::getLast);
		assertThrows(RuntimeException.class, () -> ml.get(0));

		ml.add(new Example("0"));
		assertEquals("0", ml.get(0).getName());
		assertThrows(RuntimeException.class, () -> ml.get(1));
	}

	@Test
	void expand() {
		final MappedList<Example> ml = new MappedList<>();
		for(int i = 0; i < 500; i++) {
			ml.add(new Example(Integer.toString(i)));
		}
		for(int i = 0; i < 500; i++) {
			assertEquals(Integer.toString(i), ml.get(i).getName());
		}
	}

	@Test
	void iterator() {
		final MappedList<Example> ml = new MappedList<>();
		ml.add(new Example("0"));
		ml.add(new Example("1"));
		ml.add(new Example("2"));
		int count = 0;
		for(final Example example : ml) {
			count++;
		}
		assertEquals(3, count);
	}

	private int count = 0;

	@Test
	void forEach() {
		final MappedList<Example> ml = new MappedList<>();
		ml.add(new Example("0"));
		ml.add(new Example("1"));
		ml.add(new Example("2"));
		ml.forEach((example) -> {
			count++;
		});
		assertEquals(3, count);
	}

	@Test
	void missing() {
		final MappedList<Example> ml = new MappedList<>();
		assertThrows(RuntimeException.class, () -> ml.get(0));
		assertThrows(RuntimeException.class, () -> ml.getFirst());
		assertThrows(RuntimeException.class, () -> ml.getLast());
		assertNull(ml.get(null));
		assertNull(ml.get(""));

		ml.add(new Example("0"));
		assertNull(ml.get(null));
		assertNull(ml.get(""));
	}
}
