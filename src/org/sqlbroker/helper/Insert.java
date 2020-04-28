package org.sqlbroker.helper;

import root.lang.StringExtractor;

public final class Insert extends SQL {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private String sql;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Insert(final String tableName) {
		buf = new StringExtractor(325);
		buf.append("INSERT INTO ").append(tableName).append(" (");
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Insert column(final String columnName, final Object value) {
		if (params.getSize() > 0) {
			buf.addSeparator();
		}

		buf.append(columnName);
		params.add(value);
		return this;
	}

	@Override
	public final String toString() {
		if (sql == null) {
			buf.append(") VALUES (");
			appendParams(params.getSize());
			sql = buf.append(')').toString();
			buf = null;
		}

		return sql;
	}

	/**
	 * TODO: Delete this method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final Insert stmt = new Insert("Foo");

		stmt.column("BAR", 1)
			.column("XYZ", "FIG")
			.column("ABC", 3);

		System.out.println(stmt);
	}

} // End Insert
