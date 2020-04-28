package org.sqlbroker.helper;

import root.lang.StringExtractor;

public final class Delete extends Conditional<Delete> {

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Delete(final String tableName) {
		buf = new StringExtractor(325);
		buf.append("DELETE FROM ").append(tableName);
	}

	/**
	 * TODO: Delete this method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final Delete stmt = new Delete("Foo");

		stmt.where("Primary_Key").equalTo(4);

		System.out.println(stmt);
	}

} // End Delete
