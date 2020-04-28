package org.sqlbroker.helper;

import root.adt.ListArray;
import root.lang.StringExtractor;
import root.util.Root;

public final class Update extends Conditional<Update> {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final ListArray<Audit> audits;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Update(final String table) {
		buf = new StringExtractor(325);
		buf.append("UPDATE ").append(table).append(" SET ");
		audits = new ListArray<Audit>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final boolean contains(final String columnName) {
		for (Audit a : audits) {
			if (a.getColumnName().equals(columnName)) {
				return true;
			}
		}

		return false;
	}

	public final ListArray<Audit> getAudits() {
		return audits;
	}

	public final boolean hasChanges() {
		return params.getSize() > 0;
	}

	public final Update set(final String columnName, final Object value) {
		if (params.getSize() > 0) {
			buf.addSeparator();
		}

		buf.append(columnName).append(" = ?");
		params.add(value);

		return this;
	}

	public final Update set(final String columnName, final Object oldVal, final Object newVal) {
		if (Root.notEqual(oldVal, newVal)) {
			audits.add(new Audit(columnName, oldVal, newVal));
			return set(columnName, newVal);
		}

		return this;
	}

} // End Update
