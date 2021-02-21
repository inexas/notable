package org.inexas.notable.util;

import java.util.*;
import java.util.function.*;

public class MappedList<V extends MappedList.Named> implements Iterable<V> {
	public interface Named {
		String getName();
	}

	private class MyIterator implements Iterator<V> {
		int i = 0;

		@Override
		public boolean hasNext() {
			return i < cursor;
		}

		@Override
		public V next() {
			//noinspection unchecked
			return (V) elements[i++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("MappedList.MyIterator.remove()");
		}

		@Override
		public void forEachRemaining(final Consumer<? super V> consumer) {
			for(int j = i; j < cursor; j++) {
				//noinspection unchecked
				consumer.accept((V) elements[j]);
			}
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new MyIterator();
	}

	private int extent = 8;
	private Object[] names = new Object[extent];
	private Object[] elements = new Object[extent];
	private int cursor = 0;

	public V getFirst() {
		if(cursor == 0) {
			throw new RuntimeException("Empty: MappedList.getFirst()");
		}
		//noinspection unchecked
		return (V) elements[0];
	}

	public V get(final int index) {
		if(index >= cursor) {
			throw new RuntimeException("Out of range: MappedList.get(" + index + ')');
		}
		//noinspection unchecked
		return (V) elements[index];
	}

	public V getLast() {
		if(cursor == 0) {
			throw new RuntimeException("Empty: MappedList.getLast()");
		}
		//noinspection unchecked
		return (V) elements[cursor - 1];
	}

	int indexOf(final String name) {
		int result = -1;
		for(int i = 0; i < cursor; i++) {
			if(name == null) {
				if(names[i] == null) {
					result = i;
					break;
				}
			} else if(name.equals(names[i])) {
				result = i;
				break;
			}
		}
		return result;
	}

	public V get(final String name) {
		final V result;
		if(cursor == 0) {
			result = null;
		} else {
			final int index = indexOf(name);
			if(index < 0) {
				result = null;
			} else {
				//noinspection unchecked
				result = (V) elements[index];
			}
		}
		return result;
	}

	public void add(final V element) {
		if(indexOf(element.getName()) >= 0) {
			throw new RuntimeException("Duplicate element, MappedList(" + element.getName() + ')');
		}

		ensureCapacity(cursor + 1);

		names[cursor] = element.getName();
		elements[cursor] = element;
		cursor++;
	}

	public int size() {
		return cursor;
	}

	private void ensureCapacity(final int capacity) {
		if(extent < capacity) {
			extent *= 2;
			names = resize(names);
			elements = resize(elements);
		}
	}

	private Object[] resize(final Object[] source) {
		final Object[] result = new Object[extent];
		System.arraycopy(source, 0, result, 0, source.length);
		return result;
	}

	@Override
	public int hashCode() {
		return getClass().getSimpleName().hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		final boolean result;

		if(this == object) {
			result = true;
		} else {
			if(object == null || getClass() != object.getClass()) {
				result = false;
			} else {
				@SuppressWarnings("unchecked") final MappedList<V> rhs = (MappedList<V>) object;
				if(cursor == rhs.cursor) {
					result = Arrays.equals(names, rhs.names)
							&& Arrays.equals(elements, rhs.elements);
				} else {
					result = false;
				}
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return "MappedList{" + cursor + '}';
	}
}
