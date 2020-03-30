package org.sqlbroker.helper;

public final class Audit {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final String columnName;
	private final String oldVal;
	private final String newVal;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	Audit(final String columnName, final Object oldVal, final Object newVal) {
		this.columnName = columnName;
		this.oldVal = (oldVal == null) ? null : oldVal.toString();
		this.newVal = (newVal == null) ? null : newVal.toString();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final String getColumnName() {
		return columnName;
	}

	public final String getOldValue() {
		return oldVal;
	}

	public final String getNewValue() {
		return newVal;
	}

}
