/*
 * Open source license text goes here.
 */

package org.sqlbroker;

import root.log.Log;
import root.util.Root;

public class Parameters_Old {

	private static final Log log = new Log(Parameters.class);

	int			size;
	Object[]	values;

	public Parameters_Old(final int capacity) {
		values = new Object[capacity];
	}

	public void add(final Object o) {
		if (size == values.length) {
			final Object[] z = new Object[size << 1];
			log.warn("add(): Increasing capacity from {P} to {P}", size, z.length);
			System.arraycopy(values, 0, z, 0, size);
			values = z;
		}

		values[size++] = o;
	}

	public void add(final Object... objs) {
		final int newSize = size + objs.length;

		if (newSize > values.length) {
			final Object[] z = new Object[newSize + (newSize >> 2)];
			log.warn("affix(): Increasing parameter capacity from {P} to {P}", size, z.length);
			System.arraycopy(values, 0, z, 0, size);
			values = z;
		}

		System.arraycopy(objs, 0, values, size, objs.length);
		size += objs.length;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Parameters_Old))
			return false;

		return Root.equals(values, ((Parameters_Old) o).values);
	}

	@Override
	public int hashCode() {
		return Root.hashCode(values);
	}

	public int size() {
		return size;
	}

	public Object[] toArray() {
		if (size == values.length)
			return values;

		log.warn("toArray(): Mapping values array from length {P} to actual size {P}", values.length, size);
		final Object[] z = new Object[size];
		System.arraycopy(values, 0, z, 0, size);
		return z;
	}

	@Override
	public String toString() {
		return Root.toString(values, size);
	}

} // End Parameters_Old
