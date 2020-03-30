package org.sqlbroker;

import root.log.Log;

public class BatchParams {

	private static final Log log = new Log(BatchParams.class);

	int			size;
	Object[][]	values;

	public BatchParams(final int capacity) {
		values = new Object[capacity][];
	}

	public void add(final Object... objs) {
		if (size == values.length) {
			final Object[][] z = new Object[size << 1][];
			log.warn("add(): Increasing capacity from {P} to {P}", size, z.length);
			System.arraycopy(values, 0, z, 0, size);
			values = z;
		}

		values[size++] = objs;
	}

	public int size() {
		return size;
	}

}	// End BatchParams
