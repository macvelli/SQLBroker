package org.sqlbroker;

import root.lang.ParamStr;

public final class UpdateResult {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final int rowCount;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	UpdateResult(final int rowCount) {
		this.rowCount = rowCount;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void expect(final int rowCount) {
		if (this.rowCount != rowCount)
			throw new AssertionError(ParamStr.format("Expected: {P} Actual: {P}", rowCount, this.rowCount));
	}

	public final int rowCount() {
		return rowCount;
	}

}
